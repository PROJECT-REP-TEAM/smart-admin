package net.lab1024.smartadmin.service.filter;

import net.lab1024.smartadmin.service.common.constant.RequestHeaderConst;
import net.lab1024.smartadmin.service.module.system.login.service.JwtService;
import net.lab1024.smartadmin.service.module.system.login.domain.EmployeeLoginBO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * [  ]
 * 注意此处不能 加入@Component
 * 否则对应ignoreUrl的相关请求 将会进入此Filter，并会覆盖CorsFilter
 *
 * @author 罗伊
 * @date
 */

public class SecurityTokenFilter extends OncePerRequestFilter {

    private JwtService loginTokenService;

    public SecurityTokenFilter(JwtService loginTokenService) {
        this.loginTokenService = loginTokenService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        //需要做token校验, 消息头的token优先于请求query参数的token
        String xHeaderToken = request.getHeader(RequestHeaderConst.TOKEN);
        String xRequestToken = request.getParameter(RequestHeaderConst.TOKEN);
        String xAccessToken = null != xHeaderToken ? xHeaderToken : xRequestToken;
        if (StringUtils.isBlank(xAccessToken)) {
            chain.doFilter(request, response);
            return;
        }
        //清理spring security
        SecurityContextHolder.clearContext();
        EmployeeLoginBO loginBO = loginTokenService.getEmployeeLoginBO(xAccessToken);
        if (null != loginBO) {
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginBO, null, loginBO.getAuthorities());
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }
        // 若未给予spring security上下文用户授权 则会授权失败 进入AuthenticationEntryPointImpl
        chain.doFilter(request, response);
    }
}
