package net.lab1024.smartadmin.service.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Map;

/**
 * @Description
 * @Author zhuoda
 */
@Configuration
public class AdminWebAppConfig implements WebMvcConfigurer {

    @Autowired
    private Map<String, HandlerInterceptor> interceptorMap;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        for(Map.Entry<String, HandlerInterceptor> entry : interceptorMap.entrySet()){
            registry.addInterceptor(entry.getValue()).addPathPatterns(entry.getKey() + "/**");
        }
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/druidMonitor").setViewName("redirect:druid/index.html");
        registry.addViewController("/swaggerApi").setViewName("redirect:swagger-ui.html");
    }
}
