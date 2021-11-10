package net.lab1024.smartadmin.service.config;

import cn.dev33.satoken.SaManager;
import cn.dev33.satoken.interceptor.SaAnnotationInterceptor;
import cn.dev33.satoken.interceptor.SaRouteInterceptor;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.strategy.SaStrategy;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.smartadmin.service.common.annoation.NoNeedLogin;
import net.lab1024.smartadmin.service.common.constant.UrlPrefixConst;
import net.lab1024.smartadmin.service.common.util.SmartRequestUtil;
import net.lab1024.smartadmin.service.module.system.login.domain.LoginUserDetail;
import net.lab1024.smartadmin.service.module.system.login.service.JwtService;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * [  ]
 *
 * @author yandanyang
 * @date 2021/10/13 16:36
 */
@Slf4j
@Configuration
public class SaTokenConfigure implements WebMvcConfigurer {

    @Value("${access-control-allow-origin}")
    private String accessControlAllowOrigin;

    @Autowired
    private JwtService jwtService;

    /**
     * 注册Sa-Token的注解拦截器，打开注解式鉴权功能
     *
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(new SaRouteInterceptor((req, res, handler) -> {
            //跨域
            this.crossDomainConfig((HttpServletResponse) res.getSource());
            boolean isHandlerMethod = handler instanceof HandlerMethod;
            if (!isHandlerMethod) {
                return;
            }
            HttpServletRequest request = (HttpServletRequest) req.getSource();
            String uri = request.getRequestURI();
            String contextPath = request.getContextPath();
            String target = uri.replaceFirst(contextPath, "");
            //忽略URL
            if (target.startsWith("/swagger")) {
                return;
            }
            //支持服务重启后 token依然有效 ,如果实现SaTokenDao 为redis或第三方持久化存储的话可放弃此部分代码
            String tokenValue = StpUtil.getTokenValue();
            LoginUserDetail employeeLoginInfoDTO = jwtService.getEmployeeLoginBO(tokenValue);
            if (employeeLoginInfoDTO != null) {
                StpUtil.stpLogic.saveTokenToIdMapping(tokenValue, employeeLoginInfoDTO.getEmployeeId(), SaManager.getConfig().getTimeout());
                SmartRequestUtil.setRequestEmployee(employeeLoginInfoDTO);
                return;
            }
            //无需登录
            NoNeedLogin noNeedLogin = ((HandlerMethod) handler).getMethodAnnotation(NoNeedLogin.class);
            if (noNeedLogin != null) {
                return;
            }
            //其他情况验证登录
            StpUtil.checkLogin();
        })).addPathPatterns(UrlPrefixConst.SYSTEM + "/**");
        registry.addInterceptor(new SaAnnotationInterceptor()).addPathPatterns(UrlPrefixConst.SYSTEM + "/**");
    }

    /**
     * 配置跨域
     *
     * @param response
     */
    private void crossDomainConfig(HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", accessControlAllowOrigin);
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, OPTIONS, DELETE, PATCH");
        response.setHeader("Access-Control-Expose-Headers", "*");
        response.setHeader("Access-Control-Allow-Headers", "Authentication,Origin, X-Requested-With, Content-Type, " + "Accept, x-access-token");
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Expires ", "-1");
    }


    @Autowired
    public void rewriteSaStrategy() {
        // 重写 Token 生成策略
        SaStrategy.me.createToken = (loginId, loginType) -> {
            return jwtService.generateJwtToken(NumberUtils.toLong(loginId.toString()));
        };
    }
}
