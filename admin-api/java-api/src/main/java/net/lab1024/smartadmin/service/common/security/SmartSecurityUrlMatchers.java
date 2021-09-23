package net.lab1024.smartadmin.service.common.security;

import com.google.common.collect.Lists;
import net.lab1024.smartadmin.service.common.anno.NoNeedLogin;
import net.lab1024.smartadmin.service.common.constant.CommonConst;
import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ConfigurationBuilder;

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
public class SmartSecurityUrlMatchers {

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
        IGNORE_URL.add(CommonConst.ApiUrl.API_PREFIX_SUPPORT +"/**");

        AUTHENTICATED_URL = new ArrayList<>();
        AUTHENTICATED_URL.add("/admin/**");
    }

    /**
     * 构造函数
     * @param scanPath 需要扫描的类路径
     */
    public SmartSecurityUrlMatchers(String scanPath){
        this.ANONYMOUS_URL = this.initAnonymousUrlList(scanPath);
    }

    /**
     * 获取忽略的URL集合
     * @return
     */
    public List<String> getIgnoreUrlList() {
        return IGNORE_URL;
    }

    public List<String> getPermitUrlList() {
        return ANONYMOUS_URL;
    }

    public List<String> getAuthenticatedUrlList() {
        return AUTHENTICATED_URL;
    }

    /**
     * 不需要权限校验的
     * @return
     */
    public List<String> getNoValidUrlList() {
        List<String> noValidUrl = Lists.newArrayList();
        noValidUrl.addAll(IGNORE_URL);
        noValidUrl.addAll(ANONYMOUS_URL);
        return noValidUrl;
    }

    public String [] getIgnoreUrlArray() {
        String [] ignoreUrlArray = IGNORE_URL.toArray(new String[IGNORE_URL.size()]);
        return ignoreUrlArray;
    }

    public String [] getAnonymousUrlArray() {
        String [] anonymousUrlArray = ANONYMOUS_URL.toArray(new String[ANONYMOUS_URL.size()]);
        return anonymousUrlArray;
    }

    public String [] getAuthenticatedUrlArray() {
        String [] anonymousUrlArray = AUTHENTICATED_URL.toArray(new String[AUTHENTICATED_URL.size()]);
        return anonymousUrlArray;
    }


    /**
     * 获取无需登录可以匿名访问的url信息
     *
     * @return
     */
    private List<String> initAnonymousUrlList(String scanPath) {
        List<String> anonymousUrlList = Lists.newArrayList();
        //添加无需登录注解的uri
        Reflections reflections = new Reflections(new ConfigurationBuilder().forPackages(scanPath).addScanners(new MethodAnnotationsScanner(), new TypeAnnotationsScanner()));
        Set<Method> methodSet = reflections.getMethodsAnnotatedWith(NoNeedLogin.class);
        Set<Class<?>> classSet = reflections.getTypesAnnotatedWith(NoNeedLogin.class);
        //方法级别无需登录
        for (Method method : methodSet) {
            String uriPrefix = SmartSecurityUrl.getUriPrefix(method);
            List<String> valueList = SmartSecurityUrl.getAnnotationValueList(method, uriPrefix);
            anonymousUrlList.addAll(valueList);
        }
        //类级别无需登录
        for (Class clazz : classSet) {
            Method[] methods = clazz.getMethods();
            for (Method method : methods) {
                String uriPrefix = SmartSecurityUrl.getUriPrefix(method);
                List<String> valueList = SmartSecurityUrl.getAnnotationValueList(method, uriPrefix);
                anonymousUrlList.addAll(valueList);
            }
        }
        return anonymousUrlList;
    }


}
