package net.lab1024.smartadmin.service.module.system.login.service;

import lombok.extern.slf4j.Slf4j;
import net.lab1024.smartadmin.service.common.code.UserErrorCode;
import net.lab1024.smartadmin.service.common.constant.StringConst;
import net.lab1024.smartadmin.service.common.domain.ResponseDTO;
import net.lab1024.smartadmin.service.common.util.SmartBeanUtil;
import net.lab1024.smartadmin.service.module.support.captcha.CaptchaService;
import net.lab1024.smartadmin.service.module.support.captcha.domain.CaptchaVO;
import net.lab1024.smartadmin.service.module.system.department.dao.DepartmentDao;
import net.lab1024.smartadmin.service.module.system.department.domain.entity.DepartmentEntity;
import net.lab1024.smartadmin.service.module.system.department.service.DepartmentService;
import net.lab1024.smartadmin.service.module.system.employee.dao.EmployeeDao;
import net.lab1024.smartadmin.service.module.system.employee.service.EmployeeService;
import net.lab1024.smartadmin.service.module.system.employee.domain.entity.EmployeeEntity;
import net.lab1024.smartadmin.service.module.system.login.domain.LoginForm;
import net.lab1024.smartadmin.service.module.system.login.domain.LoginResultVO;
import net.lab1024.smartadmin.service.module.system.login.domain.RequestEmployee;
import net.lab1024.smartadmin.service.module.system.menu.domain.bo.MenuLoginBO;
import net.lab1024.smartadmin.service.module.system.menu.service.MenuEmployeeService;
import net.lab1024.smartadmin.service.module.system.systemconfig.SystemConfigKeyEnum;
import net.lab1024.smartadmin.service.module.system.systemconfig.SystemConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    private JwtService jwtService;

    @Autowired
    private CaptchaService captchaService;

    @Autowired
    private MenuEmployeeService menuEmployeeService;

    @Autowired
    private SystemConfigService systemConfigService;


    /**
     * 员工登陆
     *
     * @param loginForm
     * @return 返回用户登录信息
     */
    public ResponseDTO<LoginResultVO> login(LoginForm loginForm) {
        // 校验 验证码
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
        if (!(superPassword.equals(loginForm.getPassword()) || requestPassword.equals(superPassword))) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "登录名或密码错误！");
        }

        // 生成 登录token
        String token = jwtService.generateJwtToken(employeeEntity.getEmployeeId());
        // 获取前端菜单以及功能权限
        MenuLoginBO menuLoginBORespDTO = menuEmployeeService.queryMenuTreeByEmployeeId(employeeEntity.getEmployeeId());
        // 查询部门
        DepartmentEntity departmentEntity = departmentService.getDepartmentById(employeeEntity.getDepartmentId());
        // 返回登录结果
        LoginResultVO loginResultDTO = SmartBeanUtil.copy(employeeEntity, LoginResultVO.class);
        loginResultDTO.setEmployeeId(employeeEntity.getEmployeeId());
        loginResultDTO.setMenuTree(menuLoginBORespDTO.getMenuTree());
        loginResultDTO.setMenuList(menuLoginBORespDTO.getMenuList());
        loginResultDTO.setAllMenuList(menuLoginBORespDTO.getAllMenuList());
        loginResultDTO.setPointsList(menuLoginBORespDTO.getPointsList());
        loginResultDTO.setDepartmentName(null == departmentEntity ? StringConst.EMPTY_STR : departmentEntity.getName());
        loginResultDTO.setToken(token);
        loginResultDTO.setAdministratorFlag(employeeService.isAdministrator(employeeEntity.getEmployeeId()));

        return ResponseDTO.ok(loginResultDTO);
    }

    /**
     * 退出登陆，清除token缓存
     *
     * @param employeeId
     * @return
     */
    public ResponseDTO<String> logoutByToken(Long employeeId) {
        //TODO 卓大  清除缓存等
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

    /**
     * 重新获取前端localStorage信息
     *
     * @param loginInfo
     * @return
     */
    public LoginResultVO getLoginInfo(RequestEmployee loginInfo) {
        Long employeeId = loginInfo.getEmployeeId();
        LoginResultVO loginDTO = SmartBeanUtil.copy(loginInfo, LoginResultVO.class);
        // 获取前端菜单以及功能权限
        MenuLoginBO menuLoginBORespDTO = menuEmployeeService.queryMenuTreeByEmployeeId(employeeId);
        loginDTO.setMenuTree(menuLoginBORespDTO.getMenuTree());
        loginDTO.setMenuList(menuLoginBORespDTO.getMenuList());
        loginDTO.setAllMenuList(menuLoginBORespDTO.getAllMenuList());
        loginDTO.setPointsList(menuLoginBORespDTO.getPointsList());
        //判断是否为超管
        Boolean administratorFlag = employeeService.isAdministrator(loginDTO.getEmployeeId());
        loginDTO.setAdministratorFlag(administratorFlag);
        return loginDTO;
    }


}
