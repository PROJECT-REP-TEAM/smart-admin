package net.lab1024.smartadmin.service.config;

import net.lab1024.smartadmin.service.common.security.SecurityUrlMatchers;
import net.lab1024.smartadmin.service.filter.SecurityTokenFilter;
import net.lab1024.smartadmin.service.common.security.SecurityAuthenticationFailHandler;
import net.lab1024.smartadmin.service.module.system.login.service.LoginService;
import net.lab1024.smartadmin.service.module.system.login.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.List;

/**
 * Spring Security
 *
 * @author zhuoda
 * @date 2021/8/3 17:50
 */
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${access-control-allow-origin}")
    private String accessControlAllowOrigin;
    /**
     * url
     */
    @Autowired
    private SecurityUrlMatchers securityUrlMatchers;

    /**
     * 获取TOKEN 解析类
     */
    @Autowired
    private LoginService loginService;

    /**
     * 跨域配置
     *
     * @return
     */
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        // 设置访问源地址
        config.addAllowedOriginPattern(accessControlAllowOrigin);
        // 设置访问源请求头
        config.addAllowedHeader("*");
        // 设置访问源请求方法
        config.addAllowedMethod("*");
        // 对接口配置跨域设置
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry interceptUrlRegistry = httpSecurity
                // CSRF禁用，因为不使用session
                .csrf().disable()
                // 认证失败处理类
                .exceptionHandling().authenticationEntryPoint(new SecurityAuthenticationFailHandler()).and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                // 过滤请求
                .authorizeRequests();
        //可以匿名登录的URL
        String [] anonymousUrlArray = securityUrlMatchers.getAnonymousUrlArray();
        interceptUrlRegistry.antMatchers(anonymousUrlArray).permitAll();

        //登录的URL
        String [] authenticatedUrlArray = securityUrlMatchers.getAuthenticatedUrlArray();
        interceptUrlRegistry.antMatchers(authenticatedUrlArray).authenticated();

        httpSecurity.addFilterBefore(new SecurityTokenFilter(loginService), UsernamePasswordAuthenticationFilter.class);
        httpSecurity.addFilterBefore(corsFilter(), SecurityTokenFilter.class);
    }

    @Override
    public void configure(WebSecurity web) {
        // 忽略url
        WebSecurity.IgnoredRequestConfigurer ignoring = web.ignoring();
        List<String> ignoreUrlListList = securityUrlMatchers.getIgnoreUrl();
        for (String url : ignoreUrlListList) {
            ignoring.antMatchers(url);
        }
    }


}
