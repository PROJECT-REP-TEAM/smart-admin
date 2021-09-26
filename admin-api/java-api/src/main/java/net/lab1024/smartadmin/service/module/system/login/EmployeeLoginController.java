package net.lab1024.smartadmin.service.module.system.login;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.lab1024.smartadmin.service.common.anno.NoNeedLogin;
import net.lab1024.smartadmin.service.common.swagger.SwaggerTagConst;
import net.lab1024.smartadmin.service.common.controller.AdminBaseController;
import net.lab1024.smartadmin.service.common.domain.ResponseDTO;
import net.lab1024.smartadmin.service.module.system.employee.domain.dto.EmployeeLoginDTO;
import net.lab1024.smartadmin.service.module.system.login.domain.EmployeeLoginInfoDTO;
import net.lab1024.smartadmin.service.module.system.login.domain.EmployeeLoginVO;
import net.lab1024.smartadmin.service.util.SmartEmployeeTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 后台登录
 *
 * @author 开云
 * @date 2017年12月19日上午11:46:04
 */
@RestController
@Api(tags = {SwaggerTagConst.Admin.MANAGER_EMPLOYEE_LOGIN})
public class EmployeeLoginController extends AdminBaseController {

    @Autowired
    private EmployeeLoginService employeeLoginService;

    @NoNeedLogin
    @ApiOperation("登录 by listen")
    @PostMapping("/login")
    public ResponseDTO<EmployeeLoginVO> login(@Valid @RequestBody EmployeeLoginDTO loginDTO) {
        return employeeLoginService.login(loginDTO);
    }

    @GetMapping("/login/get")
    @ApiOperation("获取登录信息")
    public ResponseDTO<EmployeeLoginVO> getSession() {
        EmployeeLoginInfoDTO requestEmployee = SmartEmployeeTokenUtil.getRequestEmployee();
        return ResponseDTO.succData(employeeLoginService.getSession(requestEmployee));
    }

    @GetMapping("/logout")
    @ApiOperation("退出登陆")
    public ResponseDTO<String> logout() {
        return employeeLoginService.logoutByToken(SmartEmployeeTokenUtil.getRequestEmployeeId());
    }
}
