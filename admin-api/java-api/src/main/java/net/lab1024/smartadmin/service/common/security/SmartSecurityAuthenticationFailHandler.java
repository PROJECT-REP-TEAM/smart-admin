package net.lab1024.smartadmin.service.common.security;

import com.alibaba.fastjson.JSONObject;
import net.lab1024.smartadmin.service.common.codeconst.LoginResponseCodeConst;
import net.lab1024.smartadmin.service.common.codeconst.ResponseCodeConst;
import net.lab1024.smartadmin.service.common.domain.ResponseDTO;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 认证失败处理
 */
public class SmartSecurityAuthenticationFailHandler implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException {
        this.outputResult(response, LoginResponseCodeConst.LOGIN_ERROR);
    }

    /**
     * 输出
     *
     * @param response
     * @param respCode
     * @throws IOException
     */
    private void outputResult(HttpServletResponse response, ResponseCodeConst respCode) throws IOException {
        String msg = JSONObject.toJSONString(ResponseDTO.wrap(respCode));
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(msg);
        response.flushBuffer();
    }
}
