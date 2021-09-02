package net.lab1024.smartadmin.service.module.support.beancache.cache;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.smartadmin.service.module.support.beancache.key.CacheKey;
import net.lab1024.smartadmin.service.module.support.beancache.load.CacheLoadMethod;
import net.lab1024.smartadmin.service.third.SmartApplicationContext;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;

/**
 * [  ]
 *
 * @author 罗伊
 * @date 2021/4/14 15:27
 */
@Slf4j
public class AbstractDisableCache implements IBeanCache {

    private Map<String, CacheLoadMethod> methodMap;

    /**
     * 构造函数
     * @param scanPath
     */
    public AbstractDisableCache(String scanPath) {
        //加载缓存方法
        this.methodMap = cacheLoadFunction(scanPath);
    }

    @Override
    public Map<String, Object> getCache() {
        log.warn("Cache is disable!");
        return Maps.newHashMap();
    }

    @Override
    public void remove(String key) {
        log.warn("Cache is disable!");
        return;
    }

    @Override
    public void clear() {
        log.warn("Cache is disable!");
        return;
    }

    @Override
    public <T> T get(String key) {
        log.warn("Cache is disable!");

        String cacheModule = CacheKey.getCacheModeByCacheKey(key);
        CacheLoadMethod loadMethod = methodMap.get(cacheModule);
        if (loadMethod == null) {
            throw null;
        }
        Method method = loadMethod.getLoadMethod();
        Object object = SmartApplicationContext.getBean(method.getDeclaringClass());
        Object result = null;
        try {
            if (method.getParameterCount() == 0) {
                result = method.invoke(object);
            }else{
                result = method.invoke(object, key);
            }
        }catch (Exception e){
            log.error("Cache get exception:{}",e);
        }
        return (T) result;

    }

    @Override
    public void refresh(String key) {
        log.warn("Cache is disable!");
        return;
    }

    @Override
    public void put(String key, Object obj) {
        log.warn("Cache is disable!");
        return;
    }

    @Override
    public void put(String key, Object obj, long expireSecond) {
        log.warn("Cache is disable!");
        return;
    }


    @Override
    public boolean containsKey(String key) {
        log.warn("Cache is disable!");
        return false;
    }

    @Override
    public Set<String> keySet() {
        log.warn("Cache is disable!");
        return Sets.newHashSet();
    }

    @Override
    public void removeByModule(String module) {
        log.warn("Cache is disable!");
        return;
    }

    @Override
    public void removeByModuleAndGroup(String module, String group) {
        log.warn("Cache is disable!");
        return;
    }
}
