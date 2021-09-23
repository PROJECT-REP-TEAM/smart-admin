package net.lab1024.smartadmin.service.config;

import net.lab1024.smartadmin.service.common.constant.SystemEnvironmentEnum;
import net.lab1024.smartadmin.service.common.domain.SystemEnvironmentBO;
import net.lab1024.smartadmin.service.util.SmartBaseEnumUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * 系统环境
 *
 * @author zhuoda
 * @date 2021/08/13 18:56
 */
@Configuration
public class SystemEnvironmentConfig implements Condition {

    @Value("${spring.profiles.active}")
    private String systemEnvironment;

    @Override
    public boolean matches(ConditionContext conditionContext, AnnotatedTypeMetadata annotatedTypeMetadata) {
        String property = conditionContext.getEnvironment().getProperty("spring.profiles.active");
        return StringUtils.isNotBlank(property) && !SystemEnvironmentEnum.PROD.equalsValue(property);
    }

    @Bean
    public SystemEnvironmentBO initEnvironment() {
        SystemEnvironmentEnum currentEnvironment = SmartBaseEnumUtil.getEnumByValue(systemEnvironment, SystemEnvironmentEnum.class);
        if (currentEnvironment == null) {
            throw new ExceptionInInitializerError("无法获取当前环境！请在 application.properties 或者 application.yaml 配置好参数：spring.profiles.active");
        }
        return new SystemEnvironmentBO(currentEnvironment == SystemEnvironmentEnum.PROD, currentEnvironment);

    }
}
