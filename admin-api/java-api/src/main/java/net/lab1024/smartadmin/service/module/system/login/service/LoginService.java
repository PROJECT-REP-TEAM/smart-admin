package net.lab1024.smartadmin.service.module.system.login.service;

import com.googlecode.concurrentlinkedhashmap.ConcurrentLinkedHashMap;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.smartadmin.service.common.code.UserErrorCode;
import net.lab1024.smartadmin.service.common.constant.StringConst;
import net.lab1024.smartadmin.service.common.domain.ResponseDTO;
import net.lab1024.smartadmin.service.common.util.SmartBeanUtil;
import net.lab1024.smartadmin.service.module.support.captcha.CaptchaService;
import net.lab1024.smartadmin.service.module.support.captcha.domain.CaptchaVO;
import net.lab1024.smartadmin.service.module.system.department.domain.entity.DepartmentEntity;
import net.lab1024.smartadmin.service.module.system.department.service.DepartmentService;
import net.lab1024.smartadmin.service.module.system.employee.domain.entity.EmployeeEntity;
import net.lab1024.smartadmin.service.module.system.employee.service.EmployeePermissionService;
import net.lab1024.smartadmin.service.module.system.employee.service.EmployeeService;
import net.lab1024.smartadmin.service.module.system.login.domain.LoginForm;
import net.lab1024.smartadmin.service.module.system.login.domain.LoginResultVO;
import net.lab1024.smartadmin.service.module.system.login.domain.LoginUserDetail;
import net.lab1024.smartadmin.service.module.system.login.domain.RequestEmployee;
import net.lab1024.smartadmin.service.module.system.menu.domain.vo.MenuVO;
import net.lab1024.smartadmin.service.module.system.systemconfig.SystemConfigKeyEnum;
import net.lab1024.smartadmin.service.module.system.systemconfig.SystemConfigService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
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
    private TokenService tokenService;

    @Autowired
    private CaptchaService captchaService;

    @Autowired
    private EmployeePermissionService employeePermissionService;

    @Autowired
    private SystemConfigService systemConfigService;

    private ConcurrentMap<Long, LoginUserDetail> loginUserDetailCache = new ConcurrentLinkedHashMap.Builder<Long, LoginUserDetail>().maximumWeightedCapacity(1000).build();


    /**
     * 员工登陆
     *
     * @param loginForm
     * @return 返回用户登录信息
     */
    public ResponseDTO<LoginResultVO> login(LoginForm loginForm) {
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
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "登录名不存在");
        }

        if (employeeEntity.getDisabledFlag()) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "您的账号已被禁用,请联系工作人员");
        }

        /**
         * 验证密码：
         * 1、万能密码
         * 2、真实密码
         */
        String superPassword = EmployeeService.getEncryptPwd(systemConfigService.getConfigValue(SystemConfigKeyEnum.SUPER_PASSWORD));
        String requestPassword = EmployeeService.getEncryptPwd(loginForm.getPassword());
        if (!(superPassword.equals(requestPassword) || employeeEntity.getLoginPwd().equals(requestPassword))) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "登录名或密码错误！");
        }

        // 返回登录结果
        LoginResultVO loginResultVO = SmartBeanUtil.copy(employeeEntity, LoginResultVO.class);

        // 获取前端菜单以及功能权限
        List<MenuVO> menuAndPointsList = employeePermissionService.getEmployeeMenuAndPointsList(employeeEntity.getEmployeeId());
        loginResultVO.setMenuList(menuAndPointsList);
        buildRequestEmployee(loginResultVO, menuAndPointsList);

        // 查询部门
        DepartmentEntity departmentEntity = departmentService.getDepartmentById(employeeEntity.getDepartmentId());
        loginResultVO.setDepartmentName(null == departmentEntity ? StringConst.EMPTY_STR : departmentEntity.getName());

        // 生成 登录token，保存token
        String token = tokenService.generateToken();
        tokenService.saveToken(token, String.valueOf(employeeEntity.getEmployeeId()), loginForm.getLoginDevice());
        loginResultVO.setToken(token);

        // 放入缓存
        LoginUserDetail loginUserDetail = SmartBeanUtil.copy(loginResultVO, LoginUserDetail.class);
        loginUserDetail.setLoginPassword(employeeEntity.getLoginPwd());
        loginUserDetailCache.put(employeeEntity.getEmployeeId(), loginUserDetail);

        return ResponseDTO.ok(loginResultVO);
    }

    /**
     * 构建 权限
     *
     * @param requestEmployee
     * @param menuAndPointsList
     */
    private void buildRequestEmployee(RequestEmployee requestEmployee, List<MenuVO> menuAndPointsList) {
        HashSet<String> permissionList = new HashSet<>();
        for (MenuVO menu : menuAndPointsList) {
            if (StringUtils.isEmpty(menu.getPerms())) {
                continue;
            }

            String[] split = menu.getPerms().split(",");
            for (String perm : split) {
                permissionList.add(perm);
            }
        }
        requestEmployee.setPermissionList(permissionList);
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
            buildRequestEmployee(loginUserDetail, menuAndPointsList);
            // 查询部门
            DepartmentEntity departmentEntity = departmentService.getDepartmentById(employeeEntity.getDepartmentId());
            loginUserDetail.setDepartmentName(null == departmentEntity ? StringConst.EMPTY_STR : departmentEntity.getName());
            // 密码
            loginUserDetail.setLoginPassword(employeeEntity.getLoginPwd());

            loginUserDetailCache.put(requestUserId, loginUserDetail);
        }

        return loginUserDetail;
    }


    /**
     * 退出登陆，清除token缓存
     *
     * @param requestEmployee
     * @return
     */
    public ResponseDTO<String> logoutByToken(RequestEmployee requestEmployee) {
        tokenService.deleteToken(requestEmployee.getToken(), String.valueOf(requestEmployee.getEmployeeId()));
        loginUserDetailCache.remove(requestEmployee.getEmployeeId());
        return ResponseDTO.ok();
    }

    /**
     * 获取验证码
     *
     * @return
     */
    public ResponseDTO<CaptchaVO> getCaptcha() {
        return ResponseDTO.ok(captchaService.generateCaptcha());
    }


}
