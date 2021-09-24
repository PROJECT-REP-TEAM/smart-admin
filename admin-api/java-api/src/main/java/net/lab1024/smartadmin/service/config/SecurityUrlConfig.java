package net.lab1024.smartadmin.service.config;

import net.lab1024.smartadmin.service.common.security.SmartSecurityUrlMatchers;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * [  ]
 *
 * @author zhuoda
 * @date 2021/9/1 21:40
 */
@Configuration
public class SecurityUrlConfig {

    @Bean
    public SmartSecurityUrlMatchers securityUrl() {
        return new SmartSecurityUrlMatchers();
    }
}
