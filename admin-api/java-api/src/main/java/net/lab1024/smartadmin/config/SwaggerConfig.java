package net.lab1024.smartadmin.config;

import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.smartadmin.common.constant.RequestHeaderConst;
import net.lab1024.smartadmin.common.enumeration.SystemEnvironmentEnum;
import net.lab1024.smartadmin.common.swagger.SwaggerApiModelPropertyEnumPlugin;
import net.lab1024.smartadmin.constant.SwaggerTagConst;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.RequestHandler;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.common.SwaggerPluginSupport;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * [ 根据SwaggerTagConst内部类动态生成Swagger group ]
 *
 * @author zhuoda
 */
@Slf4j
@EnableSwagger2
@Configuration
@Profile({
        SystemEnvironmentEnum.SystemEnvironmentNameConst.DEV,
        SystemEnvironmentEnum.SystemEnvironmentNameConst.SIT,
        SystemEnvironmentEnum.SystemEnvironmentNameConst.PRE
})
public class SwaggerConfig implements EnvironmentAware, BeanDefinitionRegistryPostProcessor {

    /**
     * 分组名称
     */
    private String apiGroupName;

    /**
     * 文档标题
     */
    private String title;

    /**
     * 文档描述
     */
    private String description;

    /**
     * api版本
     */
    private String version;

    /**
     * team url
     */
    private String teamUrl;

    /**
     * controller 包路径
     */
    private String packAge;

    /**
     * host
     */
    private String host;

    private int groupIndex = 0;

    private String groupName = "default";

    private List<String> groupList = Lists.newArrayList();

    private Map<String, List<String>> groupMap = Maps.newHashMap();


    @Bean
    @Order(SwaggerPluginSupport.SWAGGER_PLUGIN_ORDER + 1)
    public SwaggerApiModelPropertyEnumPlugin swaggerEnum() {
        return new SwaggerApiModelPropertyEnumPlugin();
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.apiGroupName = environment.getProperty("swagger.api-group-name");
        this.title = environment.getProperty("swagger.title");
        this.description = environment.getProperty("swagger.description");
        this.version = environment.getProperty("swagger.version");
        this.teamUrl = environment.getProperty("swagger.team-url");
        this.packAge = environment.getProperty("swagger.package");
        this.host = environment.getProperty("swagger.host");
    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        this.groupBuild();
        for (Map.Entry<String, List<String>> entry : groupMap.entrySet()) {
            String group = entry.getKey();
            BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(Docket.class, this::baseDocket);
            BeanDefinition beanDefinition = builder.getRawBeanDefinition();
            registry.registerBeanDefinition(group + "Api", beanDefinition);
        }
    }

    private void groupBuild() {
        Class clazz = SwaggerTagConst.class;
        Class[] innerClazz = clazz.getDeclaredClasses();
        for (Class cls : innerClazz) {
            String group = cls.getSimpleName();
            List<String> apiTags = Lists.newArrayList();
            Field[] fields = cls.getDeclaredFields();
            for (Field field : fields) {
                boolean isFinal = Modifier.isFinal(field.getModifiers());
                if (isFinal) {
                    try {
                        apiTags.add(field.get(null).toString());
                    } catch (Exception e) {
                        log.error("", e);
                    }
                }
            }
            groupList.add(group);
            groupMap.put(group, apiTags);
        }
    }

    private Docket baseDocket() {

        // 配置全局参数
        List<Parameter> parameterList = this.generateParameter();

        // 请求类型过滤规则
        Predicate<RequestHandler> controllerPredicate = getControllerPredicate();

        // controller 包路径
        Predicate<RequestHandler> controllerPackage = RequestHandlerSelectors.basePackage(packAge);

        Docket docket = new Docket(DocumentationType.SWAGGER_2)
                .groupName(groupName)
                .forCodeGeneration(true)
                .select()
                .apis(controllerPackage)
                .apis(controllerPredicate)
                .paths(PathSelectors.any())
                .build().apiInfo(this.apiInfo())
                .globalOperationParameters(parameterList);

        if (StringUtils.isNotBlank(host)) {
            docket = docket.host(host);
        }

        return docket;
    }

    private Predicate<RequestHandler> getControllerPredicate() {
        groupName = groupList.get(groupIndex);
        List<String> apiTags = groupMap.get(groupName);
        Predicate<RequestHandler> methodPredicate = (input) -> {
            Api api = null;
            Optional<Api> apiOptional = input.findControllerAnnotation(Api.class);
            if (apiOptional.isPresent()) {
                api = apiOptional.get();
            }
            List<String> tags = Arrays.asList(api.tags());
            if (apiTags.containsAll(tags)) {
                return true;
            }
            return false;
        };
        groupIndex++;
        return Predicates.and(RequestHandlerSelectors.withClassAnnotation(RestController.class), methodPredicate);
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title(title)
                .description(description)
                .version(version)
                .termsOfServiceUrl(teamUrl)
                .contact(new Contact("1024lab", teamUrl, "smartadmin@1024lab.net"))
                .build();
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {

    }

    /**
     * 生成共用请求参数
     *
     * @return
     */
    private List<Parameter> generateParameter() {
        // 配置全局参数 token
        Parameter token = new ParameterBuilder().name(RequestHeaderConst.TOKEN)
                .description("token")
                .modelRef(new ModelRef("string"))
                .parameterType("header").defaultValue("1")
                .required(false)
                .build();

        return Lists.newArrayList(token);
    }
}