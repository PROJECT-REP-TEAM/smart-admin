package net.lab1024.smartadmin.service.config;

import net.lab1024.smartadmin.service.common.security.SmartSecurityMetadataSource;
import net.lab1024.smartadmin.service.common.security.SmartSecurityUrlMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    /**
     * 无需登录的url
     */
    @Autowired
    private SmartSecurityUrlMatchers smartSecurityUrlMatchers;

    @Override
    public MethodSecurityMetadataSource customMethodSecurityMetadataSource(){
        ExpressionBasedAnnotationAttributeFactory attributeFactory = new ExpressionBasedAnnotationAttributeFactory(this.getExpressionHandler());
        return new SmartSecurityMetadataSource(attributeFactory, smartSecurityUrlMatchers.getNoValidUrlList());
    }
}
