package net.lab1024.smartadmin.service.common.security;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.smartadmin.service.common.anno.NoNeedLogin;
import net.lab1024.smartadmin.service.common.constant.CommonConst;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * [  ]
 *
 * @author 罗伊
 * @date 2021/8/31 10:20
 */
@Slf4j
@Component
public class SmartSecurityUrlMatchers {

    @Value("${project.module}")
    private String scanPackage;

    @Autowired
    private WebApplicationContext applicationContext;

    /**
     * 匿名访问URL
     */
    private List<String> anonymousUrl = Lists.newArrayList();
    /**
     * 忽略的URL(注意，加入忽略的URL，无法进入Security filter)
     */
    private List<String> ignoreUrl = Lists.newArrayList();

    /**
     * 需要登录的
     */
    private List<String> authenticatedUrl = Lists.newArrayList();

    /**
     * 方法的请求路径
     */
    private Map<Method, Set<String>> methodUrlMap = new HashMap<>();

    /**
     * 获取忽略的URL集合
     *
     * @return
     */
    public synchronized List<String> getIgnoreUrl() {
        if (CollectionUtils.isNotEmpty(ignoreUrl)) {
            return ignoreUrl;
        }
        ignoreUrl.add("/swagger-ui.html");
        ignoreUrl.add("/swagger-resources/**");
        ignoreUrl.add("/webjars/**");
        ignoreUrl.add("/*/api-docs");
        ignoreUrl.add(CommonConst.ApiUrl.API_PREFIX_SUPPORT + "/**");
        log.info("忽略URL：{}", ignoreUrl);
        return ignoreUrl;
    }

    /**
     * 需要登录认证的URL集合
     *
     * @return
     */
    public synchronized List<String> getAuthenticatedUrlList() {
        if (CollectionUtils.isNotEmpty(authenticatedUrl)) {
            return authenticatedUrl;
        }
        authenticatedUrl.add("/admin/**");
        log.info("认证URL：{}", authenticatedUrl);
        return authenticatedUrl;
    }

    /**
     * 获取无需登录可以匿名访问的url信息
     *
     * @return
     */
    private synchronized List<String> getAnonymousUrl() {
        if (CollectionUtils.isNotEmpty(anonymousUrl)) {
            return anonymousUrl;
        }
        Map<Method, Set<String>> methodSetMap = this.getMethodUrlMap();
        for (Entry<Method, Set<String>> entry : methodSetMap.entrySet()) {
            Method method = entry.getKey();
            NoNeedLogin noNeedLogin = method.getAnnotation(NoNeedLogin.class);
            if (null == noNeedLogin) {
                continue;
            }
            anonymousUrl.addAll(entry.getValue());
        }
        log.info("匿名URL：{}", anonymousUrl);
        return anonymousUrl;
    }

    /**
     * 获取每个方法的请求路径
     *
     * @return
     */
    private synchronized Map<Method, Set<String>> getMethodUrlMap() {
        if (MapUtils.isNotEmpty(methodUrlMap)) {
            return methodUrlMap;
        }
        RequestMappingHandlerMapping mapping = applicationContext.getBean(RequestMappingHandlerMapping.class);
        //获取url与类和方法的对应信息
        Map<RequestMappingInfo, HandlerMethod> map = mapping.getHandlerMethods();
        for (Entry<RequestMappingInfo, HandlerMethod> entry : map.entrySet()) {
            RequestMappingInfo requestMappingInfo = entry.getKey();
            Set<String> urls = requestMappingInfo.getPatternsCondition().getPatterns();
            if (CollectionUtils.isEmpty(urls)) {
                continue;
            }
            HandlerMethod handlerMethod = entry.getValue();
            methodUrlMap.put(handlerMethod.getMethod(), urls);
        }
        return methodUrlMap;
    }

    public Set<String> getMethodUrl(Method method) {
        return methodUrlMap.get(method);
    }

    /**
     * 获取需要校验的包路径
     *
     * @return
     */
    public String getValidPackage() {
        return scanPackage;
    }

    /**
     * 不需要权限校验的
     *
     * @return
     */
    public List<String> getNoValidUrl() {
        List<String> noValidUrl = Lists.newArrayList();
        noValidUrl.addAll(this.getIgnoreUrl());
        noValidUrl.addAll(this.getAnonymousUrl());
        return noValidUrl;
    }

    /**
     * 获取需要忽略的url集合
     *
     * @return
     */
    public String[] getIgnoreUrlArray() {
        List<String> ignoreUrl = this.getIgnoreUrl();
        String[] ignoreUrlArray = ignoreUrl.toArray(new String[ignoreUrl.size()]);
        return ignoreUrlArray;
    }

    /**
     * 获取需要匿名访问的url集合
     *
     * @return
     */
    public String[] getAnonymousUrlArray() {
        List<String> anonymousUrl = this.getAnonymousUrl();
        String[] anonymousUrlArray = anonymousUrl.toArray(new String[anonymousUrl.size()]);
        return anonymousUrlArray;
    }

    /**
     * 获取需要认证的url集合
     *
     * @return
     */
    public String[] getAuthenticatedUrlArray() {
        List<String> authenticatedUrl = this.getAuthenticatedUrlList();
        String[] authenticatedUrlArray = authenticatedUrl.toArray(new String[authenticatedUrl.size()]);
        return authenticatedUrlArray;
    }

}
