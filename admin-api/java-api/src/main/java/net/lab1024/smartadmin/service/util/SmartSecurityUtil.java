package net.lab1024.smartadmin.service.util;

import com.google.common.collect.Lists;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.reflect.Method;
import java.util.List;

/**
 * [  ]
 *
 * @author 罗伊
 * @date 2021/8/31 11:30
 */
public class SmartSecurityUtil {

    /**
     * 获取指定方法的uri 前缀
     * 即controller层注解
     *
     * @param method
     * @return
     */
    public static String getUriPrefix(Method method) {
        Class clazz = method.getDeclaringClass();
        return getUriPrefix(clazz);
    }


    public static String getUriPrefix(Class clazz) {
        String uriPrefix = "";
        RequestMapping classRequestMapping = (RequestMapping) clazz.getAnnotation(RequestMapping.class);
        if (classRequestMapping != null) {
            uriPrefix = classRequestMapping.value()[0];
        }
        Class superclass = clazz.getSuperclass();
        if (superclass == null || superclass == Object.class) {
            return uriPrefix;
        }
        RequestMapping superClassRequestMapping = (RequestMapping) superclass.getAnnotation(RequestMapping.class);
        if (superClassRequestMapping != null) {
            uriPrefix = superClassRequestMapping.value()[0] + uriPrefix;
        }
        return uriPrefix;
    }

    /**
     * 获取完整的uri前缀
     *
     * @param method
     * @param uriPrefix
     * @return
     */
    public static List<String> getAnnotationValueList(Method method, String uriPrefix) {
        List<String> valueList = Lists.newArrayList();
        GetMapping getMapping = method.getAnnotation(GetMapping.class);
        if (getMapping != null) {
            valueList.addAll(uriList(uriPrefix, getMapping.value()));
        }
        PostMapping postMapping = method.getAnnotation(PostMapping.class);
        if (postMapping != null) {
            valueList.addAll(uriList(uriPrefix, postMapping.value()));
        }
        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        if (requestMapping != null) {
            valueList.addAll(uriList(uriPrefix, requestMapping.value()));
        }
        return valueList;
    }

    public static List<String> uriList(String uriPrefix, String[] values) {
        List<String> uriList = Lists.newArrayList();
        for (String uri : values) {
            uriList.add(uriPrefix + uri);
        }
        return uriList;
    }
}
