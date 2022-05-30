package net.lab1024.smartadmin.common.security;

import net.lab1024.smartadmin.common.annoation.NoValidPermission;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.prepost.*;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.reflect.Method;
import java.util.*;

/**
 * 此类用于默认给所有接口添加权限 @employeePermissionService.checkPermission('%s')
 * %s 为类名.方法名
 * 和使用@PreAuthorize("@employeePermissionService.checkPermission('%s')") 效果一致
 * 避免所有接口都添加一遍 减轻工作量
 *
 * @author 罗伊
 * @date 2021-08-30 23:08
 */
public class SecurityMetadataSource extends PrePostAnnotationSecurityMetadataSource {

    public static final String PRIVILEGE_CHECK_NAME = "employeePermissionService";

    private static String EXPRESSION_FORMAT = "@employeePermissionService.checkPermission('%s')";

    private final PrePostInvocationAttributeFactory attributeFactory;

    private SecurityUrlMatchers securityUrlMatchers;

    public SecurityMetadataSource(PrePostInvocationAttributeFactory attributeFactory, SecurityUrlMatchers securityUrlMatchers) {
        super(attributeFactory);
        this.attributeFactory = attributeFactory;
        this.securityUrlMatchers = securityUrlMatchers;
    }


    @Override
    public Collection<ConfigAttribute> getAttributes(Method method, Class<?> targetClass) {

        //只对固定的包的所有接口进行控制
        if (!targetClass.getName().startsWith(securityUrlMatchers.getValidPackage())) {
            return super.getAttributes(method, targetClass);
        }
        //自己的控制
        GetMapping getMapping = method.getAnnotation(GetMapping.class);
        PostMapping postMapping = method.getAnnotation(PostMapping.class);
        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        if (getMapping == null && postMapping == null && requestMapping == null) {
            return super.getAttributes(method, targetClass);
        }

        //是否需要权限
        NoValidPermission methodNoValidPermission = method.getAnnotation(NoValidPermission.class);
        if (methodNoValidPermission != null) {
            return Collections.emptyList();
        }
        NoValidPermission classNoValidPermission = targetClass.getAnnotation(NoValidPermission.class);
        if (classNoValidPermission != null) {
            return Collections.emptyList();
        }
        //是否添加security原有注解
        PreAuthorize preAuthorize = method.getAnnotation(PreAuthorize.class);
        if (preAuthorize != null) {
            return super.getAttributes(method, targetClass);
        }
        PostAuthorize postAuthorize = method.getAnnotation(PostAuthorize.class);
        if (postAuthorize != null) {
            return super.getAttributes(method, targetClass);
        }
        //URL匹配
        AntPathMatcher antPathMatcher = new AntPathMatcher();
        antPathMatcher.setCaseSensitive(false);
        antPathMatcher.setTrimTokens(true);
        //无需验证的URL集合
        List<String> noValidUrlList = securityUrlMatchers.getNoValidUrl();
        //获取方法的请求路径
        Set<String> methodUrl = securityUrlMatchers.getMethodUrl(method);
        if (this.contain(antPathMatcher, noValidUrlList, methodUrl)) {
            return super.getAttributes(method, targetClass);
        }
        ArrayList<ConfigAttribute> configAttributes = new ArrayList(1);
        String classFullName = targetClass.getName();
        String methodName = method.getName();
        String[] classNameArray = StringUtils.split(classFullName, "\\.");
        String controllerName = classNameArray[classNameArray.length - 1];
        String privilegeName = controllerName + "." + methodName;
        String preAuthorizeAttribute = String.format(EXPRESSION_FORMAT, privilegeName);
        PreInvocationAttribute pre = this.attributeFactory.createPreInvocationAttribute(null, null, preAuthorizeAttribute);
        if (pre != null) {
            configAttributes.add(pre);
        }
        return configAttributes;
    }

    public Boolean contain(AntPathMatcher antPathMatcher, List<String> ignores, Set<String> urls) {
        if (CollectionUtils.isEmpty(ignores)) {
            return false;
        }
        for (String ignoreUrl : ignores) {
            for (String url : urls) {
                if (antPathMatcher.match(ignoreUrl, url)) {
                    return true;
                }
            }
        }
        return false;
    }

}
