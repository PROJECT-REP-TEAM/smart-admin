package net.lab1024.smartadmin.service.config;

import net.lab1024.smartadmin.service.common.security.SmartSecurityNoLoginUrl;
import net.lab1024.smartadmin.service.filters.SmartTokenFilter;
import net.lab1024.smartadmin.service.handler.AuthenticationFailHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.List;

/**
 * Spring Security
 *
 * @author 善逸
 * @date 2021/8/3 17:50
 */
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${access-control-allow-origin}")
    private String accessControlAllowOrigin;

    /**
     * 认证失败处理类
     */
    @Autowired
    private AuthenticationFailHandler authenticationFailHandler;

    /**
     * 无需登录的url
     */
    @Autowired
    private SmartSecurityNoLoginUrl smartSecurityNoLoginUrl;

    /**
     * token过滤器
     */
    @Autowired
    private SmartTokenFilter smartTokenFilter;

    /**
     * 跨域配置
     *
     * @return
     */
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

    /**
     * anyRequest          |   匹配所有请求路径
     * access              |   SpringEl表达式结果为true时可以访问
     * anonymous           |   匿名可以访问
     * denyAll             |   用户不能访问
     * fullyAuthenticated  |   用户完全认证可以访问（非remember-me下自动登录）
     * hasAnyAuthority     |   如果有参数，参数表示权限，则其中任何一个权限可以访问
     * hasAnyRole          |   如果有参数，参数表示角色，则其中任何一个角色可以访问
     * hasAuthority        |   如果有参数，参数表示权限，则其权限可以访问
     * hasIpAddress        |   如果有参数，参数表示IP地址，如果用户IP和参数匹配，则可以访问
     * hasRole             |   如果有参数，参数表示角色，则其角色可以访问
     * permitAll           |   用户可以任意访问
     * rememberMe          |   允许通过remember-me登录的用户访问
     * authenticated       |   用户登录后可访问
     */
    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                // CSRF禁用，因为不使用session
                .csrf().disable()
                // 认证失败处理类
                .exceptionHandling().authenticationEntryPoint(authenticationFailHandler).and()
                // 基于token，所以不需要session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                // 过滤请求
                .authorizeRequests()
                .antMatchers("/admin/**").authenticated();

        httpSecurity.addFilterBefore(smartTokenFilter, UsernamePasswordAuthenticationFilter.class);
        httpSecurity.addFilterBefore(corsFilter(), SmartTokenFilter.class);
    }

    @Override
    public void configure(WebSecurity web) {
        // 忽略url
        WebSecurity.IgnoredRequestConfigurer ignoring = web.ignoring();
        List<String> noLoginUrlList = smartSecurityNoLoginUrl.getNoLoginUrlList();
        for (String url : noLoginUrlList) {
            ignoring.antMatchers(url);
        }
    }


}
