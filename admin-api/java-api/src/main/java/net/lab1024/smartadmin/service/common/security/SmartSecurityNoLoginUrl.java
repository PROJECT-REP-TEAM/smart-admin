package net.lab1024.smartadmin.service.common.security;

import com.google.common.collect.Lists;
import net.lab1024.smartadmin.service.common.anno.NoNeedLogin;
import net.lab1024.smartadmin.service.util.SmartSecurityUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ConfigurationBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;

/**
 * [  ]
 *
 * @author 罗伊
 * @date 2021/8/31 10:20
 */
public class SmartSecurityNoLoginUrl {

    private List<String> noLoginUrlList;

    public SmartSecurityNoLoginUrl(String scanPath){
       this.noLoginUrlList = this.initNoLoginUrlList(scanPath);
    }


    public List<String> getNoLoginUrlList(){
        return noLoginUrlList;
    }

    /**
     * 获取无需登录的url信息
     *
     * @return
     */
    private List<String> initNoLoginUrlList(String scanPath) {
        List<String> noLoginUrlList = Lists.newArrayList();
        //一些常量uri
        noLoginUrlList.add("/swagger-ui.html");
        noLoginUrlList.add("/swagger-resources/**");
        noLoginUrlList.add("/webjars/**");
        noLoginUrlList.add("/*/api-docs");
        //添加无需登录注解的uri
        Reflections reflections = new Reflections(new ConfigurationBuilder().forPackages(scanPath).addScanners(new MethodAnnotationsScanner(), new TypeAnnotationsScanner()));
        Set<Method> methodSet = reflections.getMethodsAnnotatedWith(NoNeedLogin.class);
        Set<Class<?>> classSet = reflections.getTypesAnnotatedWith(NoNeedLogin.class);
        //方法级别无需登录
        for (Method method : methodSet) {
            String uriPrefix = SmartSecurityUtil.getUriPrefix(method);
            List<String> valueList = SmartSecurityUtil.getAnnotationValueList(method, uriPrefix);
            noLoginUrlList.addAll(valueList);
        }
        //类级别无需登录
        for (Class clazz : classSet) {
            Method[] methods = clazz.getMethods();
            for (Method method : methods) {
                String uriPrefix = SmartSecurityUtil.getUriPrefix(method);
                List<String> valueList = SmartSecurityUtil.getAnnotationValueList(method, uriPrefix);
                noLoginUrlList.addAll(valueList);
            }
        }
        return noLoginUrlList;
    }


}
