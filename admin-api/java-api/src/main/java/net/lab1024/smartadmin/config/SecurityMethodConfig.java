package net.lab1024.smartadmin.config;

import net.lab1024.smartadmin.common.security.SecurityMetadataSource;
import net.lab1024.smartadmin.common.security.SecurityUrlMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.expression.method.ExpressionBasedAnnotationAttributeFactory;
import org.springframework.security.access.method.MethodSecurityMetadataSource;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;

/**
 * 此类用于注入自己的 method校验
 * SmartSecurityMetadataSource
 * @author zhuoda
 * @date 2021-08-31 0:01
 */
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityMethodConfig extends GlobalMethodSecurityConfiguration {

    @Autowired
    private SecurityUrlMatchers securityUrlMatchers;

    @Override
    public MethodSecurityMetadataSource customMethodSecurityMetadataSource(){
        ExpressionBasedAnnotationAttributeFactory attributeFactory = new ExpressionBasedAnnotationAttributeFactory(this.getExpressionHandler());
        return new SecurityMetadataSource(attributeFactory, securityUrlMatchers);
    }
}
