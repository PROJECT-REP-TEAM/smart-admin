package net.lab1024.smartadmin.service.common.security;

import net.lab1024.smartadmin.service.common.anno.NoValidPrivilege;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.prepost.*;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * 此类用于默认给所有接口添加权限 @privilegeCheck.checkPermission('%s')
 * %s 为类名.方法名
 * 和使用@PreAuthorize("@privilegeCheck.checkPermission('%s')") 效果一致
 * 避免所有接口都添加一遍 减轻工作量
 *
 * @author 罗伊
 * @date 2021-08-30 23:08
 */
public class SmartSecurityMetadataSource extends PrePostAnnotationSecurityMetadataSource {

    public static final String PRIVILEGE_CHECK_NAME = "privilegeCheck";

    private static String EXPRESSION_FORMAT = "@privilegeCheck.checkPermission('%s')";

    private final PrePostInvocationAttributeFactory attributeFactory;

    private SmartSecurityUrlMatchers smartSecurityUrlMatchers;

    public SmartSecurityMetadataSource(PrePostInvocationAttributeFactory attributeFactory, SmartSecurityUrlMatchers smartSecurityUrlMatchers) {
        super(attributeFactory);
        this.attributeFactory = attributeFactory;
        this.smartSecurityUrlMatchers = smartSecurityUrlMatchers;
    }


    @Override
    public Collection<ConfigAttribute> getAttributes(Method method, Class<?> targetClass) {

        //只对固定的包的所有接口进行控制
        if (!targetClass.getName().startsWith(smartSecurityUrlMatchers.getValidPackage())) {
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
        NoValidPrivilege methodNoValidPrivilege = method.getAnnotation(NoValidPrivilege.class);
        if (methodNoValidPrivilege != null) {
            return Collections.emptyList();
        }
        NoValidPrivilege classNoValidPrivilege = targetClass.getAnnotation(NoValidPrivilege.class);
        if (classNoValidPrivilege != null) {
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
        //获取注解值
        String uriPrefix = SmartSecurityUrl.getUriPrefix(method);
        List<String> annotationValueList = SmartSecurityUrl.getAnnotationValueList(method, uriPrefix);
        //判断是否被忽略
        AntPathMatcher antPathMatcher = new AntPathMatcher();
        antPathMatcher.setCaseSensitive(false);
        antPathMatcher.setTrimTokens(true);
        //无需验证的URL集合
        List<String> noValidUrlList = smartSecurityUrlMatchers.getNoValidUrl();
        if (this.contain(antPathMatcher, noValidUrlList, annotationValueList)) {
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

    public Boolean contain(AntPathMatcher antPathMatcher, List<String> ignores, List<String> valueList) {
        if (CollectionUtils.isEmpty(ignores)) {
            return false;
        }
        for (String ignoreUrl : ignores) {
            for (String uri : valueList) {
                if (antPathMatcher.match(ignoreUrl, uri)) {
                    return true;
                }
            }
        }
        return false;
    }

}
