package net.lab1024.smartadmin.service.common.security;

import com.google.common.collect.Lists;
import net.lab1024.smartadmin.service.common.anno.NoNeedLogin;
import net.lab1024.smartadmin.service.common.constant.CommonConst;
import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ConfigurationBuilder;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * [  ]
 *
 * @author 罗伊
 * @date 2021/8/31 10:20
 */
public class SmartSecurityUrlMatchers implements BeanPostProcessor {

    /**
     * 匿名访问URL
     */
    private List<String> ANONYMOUS_URL;

    /**
     * 忽略的URL(注意，加入忽略的URL，无法进入Security filter)
     */
    private static List<String> IGNORE_URL;

    /**
     * 需要登录的
     */
    private static List<String> AUTHENTICATED_URL;

    static {
        IGNORE_URL = new ArrayList<>();
        IGNORE_URL.add("/swagger-ui.html");
        IGNORE_URL.add("/swagger-resources/**");
        IGNORE_URL.add("/webjars/**");
        IGNORE_URL.add("/*/api-docs");
        IGNORE_URL.add(CommonConst.ApiUrl.API_PREFIX_SUPPORT + "/**");

        AUTHENTICATED_URL = new ArrayList<>();
        AUTHENTICATED_URL.add("/admin/**");
    }

    /**
     * 构造函数
     *
     */
    public SmartSecurityUrlMatchers() {
    }

    /**
     * 获取忽略的URL集合
     *
     * @return
     */
    public List<String> getIgnoreUrlList() {
        return IGNORE_URL;
    }

    /**
     * 获取需要匿名访问的url集合
     *
     * @return
     */
    public List<String> getAnonymousUrlList() {
        return ANONYMOUS_URL;
    }

    /**
     * 获取需要认证的url集合
     *
     * @return
     */
    public List<String> getAuthenticatedUrlList() {
        return AUTHENTICATED_URL;
    }

    /**
     * 不需要权限校验的
     *
     * @return
     */
    public List<String> getNoValidUrlList() {
        List<String> noValidUrl = Lists.newArrayList();
        noValidUrl.addAll(IGNORE_URL);
        noValidUrl.addAll(ANONYMOUS_URL);
        return noValidUrl;
    }

    /**
     * 获取需要忽略的url集合
     *
     * @return
     */
    public String[] getIgnoreUrlArray() {
        String[] ignoreUrlArray = IGNORE_URL.toArray(new String[IGNORE_URL.size()]);
        return ignoreUrlArray;
    }

    /**
     * 获取需要匿名访问的url集合
     *
     * @return
     */
    public String[] getAnonymousUrlArray() {
        String[] anonymousUrlArray = ANONYMOUS_URL.toArray(new String[ANONYMOUS_URL.size()]);
        return anonymousUrlArray;
    }

    /**
     * 获取需要认证的url集合
     *
     * @return
     */
    public String[] getAuthenticatedUrlArray() {
        String[] anonymousUrlArray = AUTHENTICATED_URL.toArray(new String[AUTHENTICATED_URL.size()]);
        return anonymousUrlArray;
    }


    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Method[] methods = ReflectionUtils.getAllDeclaredMethods(bean.getClass());
        if (methods == null) {
            return bean;
        }
        //方法级别无需登录
        for (Method method : methods) {
            NoNeedLogin noNeedLogin = method.getAnnotation(NoNeedLogin.class);
            if(noNeedLogin != null){
                String uriPrefix = SmartSecurityUrl.getUriPrefix(method);
                List<String> valueList = SmartSecurityUrl.getAnnotationValueList(method, uriPrefix);
                this.ANONYMOUS_URL.addAll(valueList);
            }
        }
        return bean;

    }

}
