package net.lab1024.smartadmin.service.config;

import net.lab1024.smartadmin.service.common.security.SmartSecurityNoLoginUrl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * [  ]
 *
 * @author 罗伊
 * @date 2021/9/1 21:40
 */
@Configuration
public class SecurityNoLoginUrlConfig {

    @Value("${project.module}")
    private String projectModule;

    @Bean
    public SmartSecurityNoLoginUrl securityNoLoginUrl(){
        return new SmartSecurityNoLoginUrl(projectModule);
    }
}
