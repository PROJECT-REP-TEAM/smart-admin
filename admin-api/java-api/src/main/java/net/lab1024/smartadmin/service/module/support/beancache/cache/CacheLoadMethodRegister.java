package net.lab1024.smartadmin.service.module.support.beancache.cache;

import net.lab1024.smartadmin.service.module.support.beancache.anno.CacheLoad;
import net.lab1024.smartadmin.service.module.support.beancache.domain.CacheLoadMethod;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * [  ]
 *
 * @author yandanyang
 * @date 2021/9/25 16:32
 */
public class CacheLoadMethodRegister implements BeanPostProcessor {

    private Map<String, CacheLoadMethod> cacheLoadMethodMap = new HashMap<>();

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Method[] methods = ReflectionUtils.getAllDeclaredMethods(bean.getClass());
        if (methods == null) {
            return bean;
        }
        for (Method method : methods) {
            CacheLoad cacheLoad = method.getAnnotation(CacheLoad.class);
            if (cacheLoad != null) {
                String cacheModule = cacheLoad.value();
                net.lab1024.smartadmin.service.module.support.beancache.domain.CacheLoadMethod cacheLoadMethod = new net.lab1024.smartadmin.service.module.support.beancache.domain.CacheLoadMethod();
                cacheLoadMethod.setCacheModule(cacheModule);
                cacheLoadMethod.setExpireSecond(cacheLoad.expireSecond());
                cacheLoadMethod.setLoadMethod(method);
                cacheLoadMethodMap.put(cacheModule, cacheLoadMethod);
            }
        }
        return bean;
    }


    public CacheLoadMethod getCacheLoadMethod(String key){
        return cacheLoadMethodMap.get(key);
    }

}
