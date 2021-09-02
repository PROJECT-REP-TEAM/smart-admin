package net.lab1024.smartadmin.service.filters;

import net.lab1024.smartadmin.service.common.constant.CommonConst;
import net.lab1024.smartadmin.service.module.system.login.EmployeeLoginTokenService;
import net.lab1024.smartadmin.service.module.system.login.domain.EmployeeLoginBO;
import net.lab1024.smartadmin.service.module.system.login.domain.EmployeeLoginInfoDTO;
import net.lab1024.smartadmin.service.util.SmartBeanUtil;
import net.lab1024.smartadmin.service.util.SmartEmployeeTokenUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * token过滤器
 */
@Component
public class SmartTokenFilter extends OncePerRequestFilter {
    private static final String TOKEN_NAME = "x-access-token";

    @Autowired
    private EmployeeLoginTokenService loginTokenService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        //需要做token校验, 消息头的token优先于请求query参数的token
        String xHeaderToken = request.getHeader(TOKEN_NAME);
        String xRequestToken = request.getParameter(TOKEN_NAME);
        String xAccessToken = null != xHeaderToken ? xHeaderToken : xRequestToken;
        if (StringUtils.isBlank(xAccessToken)) {
            // 若未给予spring security上下文用户授权 则会授权失败 进入AuthenticationEntryPointImpl
            chain.doFilter(request, response);
            return;
        }

        // 先清理spring security上下文
        SecurityContextHolder.clearContext();

        // 判断请求分组
        String requestURI = request.getRequestURI();
        if (StringUtils.startsWithIgnoreCase(requestURI, CommonConst.ApiUrl.API_PREFIX_ADMIN)) {
            // 后管 获取用户信息
            EmployeeLoginBO loginBO = loginTokenService.getEmployeeLoginBO(xAccessToken);
            // 若获取到了登陆信息 则把用户信息设置到上下文中
            if (null != loginBO) {
                SmartEmployeeTokenUtil.setUser(SmartBeanUtil.copy(loginBO, EmployeeLoginInfoDTO.class));
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginBO, null, loginBO.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }

        // 若未给予spring security上下文用户授权 则会授权失败 进入AuthenticationEntryPointImpl
        chain.doFilter(request, response);
    }
}
