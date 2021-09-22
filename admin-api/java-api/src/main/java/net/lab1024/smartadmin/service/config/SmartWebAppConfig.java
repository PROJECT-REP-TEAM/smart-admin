package net.lab1024.smartadmin.service.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Description
 * @Author 善逸
 * @Date Created in 2017/10/24 13:48
 */
@Configuration
public class SmartWebAppConfig implements WebMvcConfigurer {

    @Value("${file.storage.local.path}")
    private String localPath;

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/druidMonitor").setViewName("redirect:druid/index.html");
        registry.addViewController("/swaggerApi").setViewName("redirect:swagger-ui.html");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/META-INF/resources/", "classpath:/resources/", "classpath:/static/", "file:" + localPath);
    }
}
