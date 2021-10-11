package net.lab1024.smartadmin.service.module.system.login;

import lombok.extern.slf4j.Slf4j;
import net.lab1024.smartadmin.service.common.code.UserErrorCode;
import net.lab1024.smartadmin.service.common.constant.StringConst;
import net.lab1024.smartadmin.service.common.domain.ResponseDTO;
import net.lab1024.smartadmin.service.common.util.SmartBeanUtil;
import net.lab1024.smartadmin.service.module.support.captcha.CaptchaService;
import net.lab1024.smartadmin.service.module.support.captcha.domain.CaptchaForm;
import net.lab1024.smartadmin.service.module.system.department.DepartmentDao;
import net.lab1024.smartadmin.service.module.system.department.domain.entity.DepartmentEntity;
import net.lab1024.smartadmin.service.module.system.employee.EmployeeDao;
import net.lab1024.smartadmin.service.module.system.employee.EmployeeService;
import net.lab1024.smartadmin.service.module.system.employee.domain.dto.EmployeeLoginDTO;
import net.lab1024.smartadmin.service.module.system.employee.domain.entity.EmployeeEntity;
import net.lab1024.smartadmin.service.module.system.login.domain.EmployeeLoginInfoDTO;
import net.lab1024.smartadmin.service.module.system.login.domain.EmployeeLoginVO;
import net.lab1024.smartadmin.service.module.system.menu.MenuEmployeeService;
import net.lab1024.smartadmin.service.module.system.menu.domain.MenuLoginBO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * [  ]
 *
 * @author 罗伊
 */
@Slf4j
@Service
public class EmployeeLoginService {

    @Autowired
    private EmployeeDao employeeDao;

    @Autowired
    private DepartmentDao departmentDao;

    @Autowired
    private EmployeeLoginTokenService employeeLoginTokenService;

    @Autowired
    private MenuEmployeeService menuEmployeeService;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private CaptchaService captchaService;

    /**
     * 员工登陆
     *
     * @param loginDTO
     * @return 返回用户登录信息
     */
    public ResponseDTO<EmployeeLoginVO> login(EmployeeLoginDTO loginDTO) {
        // 校验图形验证码
        CaptchaForm captcha = loginDTO.getCaptcha();
        if (null != captcha) {
            ResponseDTO<String> res = captchaService.checkCaptcha(captcha.getCaptchaId(), captcha.getCaptcha());
            if (!res.getOk()) {
                return ResponseDTO.error(res);
            }
        }

        String loginPwd = EmployeeService.getEncryptPwd(loginDTO.getLoginPwd());
        EmployeeEntity employeeEntity = employeeDao.selectByLoginNameAndPwd(loginDTO.getLoginName(), loginPwd, false);
        if (null == employeeEntity) {
            return ResponseDTO.error(UserErrorCode.LOGIN_FAILED);
        }

        if (employeeEntity.getDisabledFlag()) {
            return ResponseDTO.error(UserErrorCode.USER_STATUS_ERROR);
        }

        // 生成 登录token
        Long employeeId = employeeEntity.getId();
        String token = employeeLoginTokenService.generateToken(employeeId);

        // 判断是否为超管
        Boolean isSuperman = menuEmployeeService.isSuperman(employeeId);

        // 获取前端菜单以及功能权限
        MenuLoginBO menuLoginBORespDTO = menuEmployeeService.queryMenuTreeByEmployeeId(employeeId);

        // 查询部门
        DepartmentEntity deptEntity = departmentDao.selectById(employeeEntity.getDepartmentId());
        String deptName = null == deptEntity ? StringConst.EMPTY_STR : deptEntity.getName();
        // 返回登录结果
        EmployeeLoginVO loginResultDTO = SmartBeanUtil.copy(employeeEntity, EmployeeLoginVO.class);
        loginResultDTO.setEmployeeId(employeeEntity.getId());
        loginResultDTO.setMenuTree(menuLoginBORespDTO.getMenuTree());
        loginResultDTO.setPointsList(menuLoginBORespDTO.getPointsList());
        loginResultDTO.setDepartmentName(deptName);
        loginResultDTO.setToken(token);
        loginResultDTO.setIsSuperMan(isSuperman);
        return ResponseDTO.ok(loginResultDTO);
    }

    /**
     * 退出登陆，清除token缓存
     *
     * @param employeeId
     * @return
     */
    public ResponseDTO<String> logoutByToken(Long employeeId) {
        return ResponseDTO.ok();
    }

    /**
     * 重新获取前端localStorage信息
     *
     * @param loginInfo
     * @return
     */
    public EmployeeLoginVO getSession(EmployeeLoginInfoDTO loginInfo) {
        Long employeeId = loginInfo.getEmployeeId();

        EmployeeLoginVO loginDTO = SmartBeanUtil.copy(loginInfo, EmployeeLoginVO.class);
        // 获取前端菜单以及功能权限
        MenuLoginBO menuLoginBORespDTO = menuEmployeeService.queryMenuTreeByEmployeeId(employeeId);
        loginDTO.setMenuTree(menuLoginBORespDTO.getMenuTree());
        loginDTO.setPointsList(menuLoginBORespDTO.getPointsList());
        //判断是否为超管
        Boolean isSuperman = menuEmployeeService.isSuperman(loginDTO.getEmployeeId());
        loginDTO.setIsSuperMan(isSuperman);
        return loginDTO;
    }
}
