package net.lab1024.smartadmin.service.module.system.login.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.lab1024.smartadmin.service.common.controller.SystemBaseController;
import net.lab1024.smartadmin.service.common.domain.CaptchaVO;
import net.lab1024.smartadmin.service.common.util.SmartRequestUtil;
import net.lab1024.smartadmin.service.module.system.login.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import net.lab1024.smartadmin.service.common.annoation.NoNeedLogin;
import net.lab1024.smartadmin.service.common.annoation.NoValidPrivilege;
import net.lab1024.smartadmin.service.common.domain.ResponseDTO;
import net.lab1024.smartadmin.service.constant.SwaggerTagConst;
import net.lab1024.smartadmin.service.module.system.login.domain.LoginForm;
import net.lab1024.smartadmin.service.module.system.login.domain.RequestEmployee;
import net.lab1024.smartadmin.service.module.system.login.domain.LoginResultVO;

import javax.validation.Valid;

/**
 * 登录
 *
 * @author zhuoda
 */
@RestController
@Api(tags = {SwaggerTagConst.System.MANAGER_EMPLOYEE_LOGIN})
public class LoginController extends SystemBaseController {

    @Autowired
    private LoginService loginService;

    @NoNeedLogin
    @PostMapping("/login")
    @ApiOperation("登录 @author zhuoda")
    public ResponseDTO<LoginResultVO> login(@Valid @RequestBody LoginForm loginDTO) {
        return loginService.login(loginDTO);
    }

    @GetMapping("/login/getLoginInfo")
    @ApiOperation("获取登录信息  @author zhuoda")
    @NoValidPrivilege
    public ResponseDTO<LoginResultVO> getLoginInfo() {
        RequestEmployee requestEmployee = SmartRequestUtil.getRequestEmployee();
        return ResponseDTO.ok(loginService.getLoginInfo(requestEmployee));
    }

    @ApiOperation("退出登陆  @author zhuoda")
    @GetMapping("/login/logout")
    @NoValidPrivilege
    public ResponseDTO<String> logout() {
        return loginService.logoutByToken(SmartRequestUtil.getRequestEmployeeId());
    }

    @NoNeedLogin
    @ApiOperation("获取验证码  @author zhuoda")
    @GetMapping("/login/getCaptcha")
    public ResponseDTO<CaptchaVO> getCaptcha() {
        return loginService.getCaptcha();
    }

}
