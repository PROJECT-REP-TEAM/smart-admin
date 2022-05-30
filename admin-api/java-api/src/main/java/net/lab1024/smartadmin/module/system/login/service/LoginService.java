package net.lab1024.smartadmin.module.system.login.service;

import com.googlecode.concurrentlinkedhashmap.ConcurrentLinkedHashMap;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.smartadmin.common.constant.StringConst;
import net.lab1024.smartadmin.common.domain.ResponseDTO;
import net.lab1024.smartadmin.common.util.SmartBaseEnumUtil;
import net.lab1024.smartadmin.common.util.SmartBeanUtil;
import net.lab1024.smartadmin.constant.RedisKeyConst;
import net.lab1024.smartadmin.module.support.captcha.CaptchaService;
import net.lab1024.smartadmin.module.support.captcha.domain.CaptchaVO;
import net.lab1024.smartadmin.module.support.redis.RedisService;
import net.lab1024.smartadmin.module.system.department.domain.entity.DepartmentEntity;
import net.lab1024.smartadmin.module.system.department.service.DepartmentService;
import net.lab1024.smartadmin.module.system.employee.domain.entity.EmployeeEntity;
import net.lab1024.smartadmin.module.system.employee.service.EmployeePermissionService;
import net.lab1024.smartadmin.module.system.employee.service.EmployeeService;
import net.lab1024.smartadmin.module.system.login.constant.LoginDeviceEnum;
import net.lab1024.smartadmin.module.system.login.domain.LoginForm;
import net.lab1024.smartadmin.module.system.login.domain.LoginResultVO;
import net.lab1024.smartadmin.module.system.login.domain.LoginUserDetail;
import net.lab1024.smartadmin.module.system.login.domain.RequestEmployee;
import net.lab1024.smartadmin.module.system.menu.domain.vo.MenuVO;
import net.lab1024.smartadmin.module.system.systemconfig.SystemConfigKeyEnum;
import net.lab1024.smartadmin.module.system.systemconfig.SystemConfigService;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ConcurrentMap;

/**
 * @author zhuoda
 * @date 2021-11-1
 */
@Slf4j
@Service
public class LoginService {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private CaptchaService captchaService;

    @Autowired
    private EmployeePermissionService employeePermissionService;

    @Autowired
    private SystemConfigService systemConfigService;

    private ConcurrentMap<Long, LoginUserDetail> loginUserDetailCache = new ConcurrentLinkedHashMap.Builder<Long, LoginUserDetail>().maximumWeightedCapacity(1000).build();


    /**
     * 获取验证码
     *
     * @return
     */
    public ResponseDTO<CaptchaVO> getCaptcha() {
        return ResponseDTO.ok(captchaService.generateCaptcha());
    }

    /**
     * 员工登陆
     *
     * @param loginForm
     * @return 返回用户登录信息
     */
    public ResponseDTO<LoginResultVO> login(LoginForm loginForm) {
        LoginDeviceEnum loginDeviceEnum = SmartBaseEnumUtil.getEnumByValue(loginForm.getLoginDevice(), LoginDeviceEnum.class);
        if (loginDeviceEnum == null) {
            return ResponseDTO.userErrorParam("登录设备暂不支持！");
        }


        // 校验 图形验证码
        ResponseDTO<String> checkCaptcha = captchaService.checkCaptcha(loginForm);
        if (!checkCaptcha.getOk()) {
            return ResponseDTO.error(checkCaptcha);
        }

        /**
         * 验证账号和账号状态
         */
        EmployeeEntity employeeEntity = employeeService.getByLoginName(loginForm.getLoginName());
        if (null == employeeEntity) {
            return ResponseDTO.userErrorParam("登录名不存在！");
        }

        if (employeeEntity.getDisabledFlag()) {
            return ResponseDTO.userErrorParam("您的账号已被禁用,请联系工作人员！");
        }

        /**
         * 验证密码：
         * 1、万能密码
         * 2、真实密码
         */
        String superPassword = EmployeeService.getEncryptPwd(systemConfigService.getConfigValue(SystemConfigKeyEnum.SUPER_PASSWORD));
        String requestPassword = EmployeeService.getEncryptPwd(loginForm.getPassword());
        if (!(superPassword.equals(requestPassword) || employeeEntity.getLoginPwd().equals(requestPassword))) {
            return ResponseDTO.userErrorParam("登录名或密码错误！");
        }

        // 是否为万能密码
        boolean isSuperPassword = superPassword.equals(requestPassword);

        // 返回登录结果
        LoginResultVO loginResultVO = SmartBeanUtil.copy(employeeEntity, LoginResultVO.class);

        // 获取前端菜单以及功能权限
        List<MenuVO> menuAndPointsList = employeePermissionService.getEmployeeMenuAndPointsList(employeeEntity.getEmployeeId());
        loginResultVO.setMenuList(menuAndPointsList);
        employeePermissionService.buildPermissionList(loginResultVO, menuAndPointsList);

        // 查询部门
        DepartmentEntity departmentEntity = departmentService.getDepartmentById(employeeEntity.getDepartmentId());
        loginResultVO.setDepartmentName(null == departmentEntity ? StringConst.EMPTY_STR : departmentEntity.getName());

        // 生成 登录token，保存token
        String token = tokenService.generateToken(loginForm.getLoginDevice(), isSuperPassword);
        tokenService.saveToken(token, employeeEntity.getEmployeeId());
        loginResultVO.setToken(token);

        //对于 使用万能密码登录，不需要挤掉人，否则需要挤掉线
        if (!isSuperPassword) {
            // 上次该设备登录的token，并删除
            String userDeviceLastToken = getTokenByUserIdAndDevice(employeeEntity.getEmployeeId(), loginDeviceEnum);
            this.deleteLoginToken(userDeviceLastToken);
            this.deleteUserLoginDevice(employeeEntity.getEmployeeId(), loginDeviceEnum);
            // 保存最新的token设备
            this.saveUserLoginDevice(employeeEntity.getEmployeeId(), loginDeviceEnum, token);
        }

        // 放入缓存
        LoginUserDetail loginUserDetail = SmartBeanUtil.copy(loginResultVO, LoginUserDetail.class);
        loginUserDetail.setLoginPassword(employeeEntity.getLoginPwd());
        loginUserDetailCache.put(employeeEntity.getEmployeeId(), loginUserDetail);

        return ResponseDTO.ok(loginResultVO);
    }


    /**
     * 获取登录结果
     *
     * @param requestEmployee
     * @return
     */
    public LoginResultVO getLoginResult(RequestEmployee requestEmployee) {
        LoginResultVO loginResultVO = SmartBeanUtil.copy(requestEmployee, LoginResultVO.class);
        loginResultVO.setMenuList(employeePermissionService.getEmployeeMenuAndPointsList(requestEmployee.getEmployeeId()));
        return loginResultVO;
    }

    /**
     * 根据登陆token 获取员请求工信息
     *
     * @param
     * @return
     */
    public LoginUserDetail getLoginUserDetail(String token) {
        Long requestUserId = tokenService.getRequestUserId(token);
        if (requestUserId == null) {
            return null;
        }
        // 查询用户信息
        LoginUserDetail loginUserDetail = loginUserDetailCache.get(requestUserId);
        if (loginUserDetail == null) {
            // 查询员工信息
            EmployeeEntity employeeEntity = employeeService.getById(requestUserId);
            // 获取前端菜单以及功能权限
            loginUserDetail = SmartBeanUtil.copy(employeeEntity, LoginUserDetail.class);
            List<MenuVO> menuAndPointsList = employeePermissionService.getEmployeeMenuAndPointsList(employeeEntity.getEmployeeId());
            employeePermissionService.buildPermissionList(loginUserDetail, menuAndPointsList);
            // 查询部门
            DepartmentEntity departmentEntity = departmentService.getDepartmentById(employeeEntity.getDepartmentId());
            loginUserDetail.setDepartmentName(null == departmentEntity ? StringConst.EMPTY_STR : departmentEntity.getName());
            // 密码
            loginUserDetail.setLoginPassword(employeeEntity.getLoginPwd());

            loginUserDetailCache.put(requestUserId, loginUserDetail);
        }

        return loginUserDetail;
    }

    // -------------------------------------------- 以下是退出登录 --------------------------------------------

    /**
     * 退出登陆，清除token缓存
     *
     * @param requestEmployee
     * @return
     */
    public ResponseDTO<String> logout(RequestEmployee requestEmployee) {
        this.deleteLoginToken(requestEmployee.getToken());
        loginUserDetailCache.remove(requestEmployee.getEmployeeId());
        return ResponseDTO.ok();
    }

    /**
     * 删除登录的token
     *
     * @param token
     */
    private void deleteLoginToken(String token) {
        if (token == null) {
            return;
        }
        tokenService.deleteToken(token);
    }


    // -------------------------------------------- 用户登录 失效时间 --------------------------------------------

    public Long getLoginExpireSeconds() {
        // 过期小时
        int expiresHour = NumberUtils.toInt(systemConfigService.getConfigValue(SystemConfigKeyEnum.LOGIN_EXPIRES_HOUR));
        long expireSeconds = expiresHour * 3600L;
        return expireSeconds;
    }


    // -------------------------------------------- 用户登录设备 --------------------------------------------

    /**
     * 保存用户登录设备
     *
     * @param requestUserId
     * @param loginDeviceEnum
     */
    public void saveUserLoginDevice(Long requestUserId, LoginDeviceEnum loginDeviceEnum, String token) {
        redisService.set(getDeviceRedisKey(requestUserId, loginDeviceEnum), token, this.getLoginExpireSeconds());
    }

    /**
     * 获取 用户 本 登录设备的 token
     *
     * @param requestUserId
     * @param loginDeviceEnum
     */
    public String getTokenByUserIdAndDevice(Long requestUserId, LoginDeviceEnum loginDeviceEnum) {
        return redisService.get(getDeviceRedisKey(requestUserId, loginDeviceEnum));
    }

    /**
     * 删除用户的登录设备
     *
     * @param requestUserId
     * @param loginDeviceEnum
     * @return
     */
    public void deleteUserLoginDevice(Long requestUserId, LoginDeviceEnum loginDeviceEnum) {
        redisService.delete(getDeviceRedisKey(requestUserId, loginDeviceEnum));
    }


    private String getDeviceRedisKey(Long requestUserId, LoginDeviceEnum loginDeviceEnum) {
        return RedisKeyConst.System.USER_TOKEN_DEVICE + requestUserId + RedisKeyConst.SEPARATOR + loginDeviceEnum.getValue();
    }

}
