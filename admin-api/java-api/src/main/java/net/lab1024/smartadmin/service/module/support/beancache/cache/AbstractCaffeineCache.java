package net.lab1024.smartadmin.service.module.support.beancache.cache;

import com.alibaba.fastjson.JSON;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import net.lab1024.smartadmin.service.module.support.beancache.domain.CacheData;
import net.lab1024.smartadmin.service.module.support.beancache.key.CacheKey;
import net.lab1024.smartadmin.service.module.support.beancache.key.CacheKeyBuilder;
import net.lab1024.smartadmin.service.module.support.beancache.domain.CacheLoadMethod;
import net.lab1024.smartadmin.service.third.SmartApplicationContext;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.compress.utils.Lists;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

/**
 * [  ]
 *
 * @author 罗伊
 * @date 2021/4/14 15:12
 */
public abstract class AbstractCaffeineCache implements IBeanCache {

    /**
     * 获取缓存
     * @return
     */
    @Override
    public abstract LoadingCache<String, CacheData> getCache();

    /**
     * 缓存加载方法
     * @return
     */
    @Override
    public abstract CacheLoadMethodRegister methodRegister();

    /**
     * 移除某个缓存
     * @param key
     */
    @Override
    public void remove(String key) {
        getCache().invalidate(key);
    }

    /**
     * 清除所有
     */
    @Override
    public void clear() {
        getCache().invalidateAll();
    }

    /**
     * 获取某个缓存对象
     * @param key
     * @return
     */
    @Override
    public <T> T get(String key) {
        LoadingCache<String, CacheData> cache = getCache();
        CacheData cacheData = cache.get(key);
        if(cacheData == null){
            return null;
        }
        if(cacheData.getExpireTime() == null){
            return (T)cacheData.getData();
        }
        long expireTime = cacheData.getExpireTime();
        long current = System.currentTimeMillis();
        if(expireTime > current){
            return (T)cacheData.getData();
        }
        //缓存过期 移除key 重新获取
        cache.invalidate(key);
        return (T)cache.get(key).getData();
    }

    /**
     * 刷新某个缓存key-异步刷新
     * @param key
     */
    @Override
    public void refresh(String key){
        LoadingCache<String, CacheData> cache = getCache();
        cache.refresh(key);
    }

    /**
     * 设置key->value
     * @param key
     * @param obj
     */
    @Override
    public void put(String key, Object obj) {
        CacheData cacheData = new CacheData();
        cacheData.setData(obj);
        getCache().put(key, cacheData);
    }


    @Override
    public void put(String key, Object obj, long expireSecond) {
        CacheData cacheData = new CacheData();
        long expireTime = System.currentTimeMillis() + expireSecond * 1000;
        cacheData.setExpireTime(expireTime);
        cacheData.setData(obj);
        getCache().put(key, cacheData);
    }

    /**
     * 判断是否包含某个key
     * @param key
     * @return
     */
    @Override
    public boolean containsKey(String key) {
        ConcurrentMap concurrentMap = getCache().asMap();
        return concurrentMap.containsKey(key);
    }

    /**
     * 所有key集合
     * @return
     */
    @Override
    public Set<String> keySet() {
        ConcurrentMap concurrentMap = getCache().asMap();
        return concurrentMap.keySet();
    }

    /**
     * 移除某个模块的所有key
     * @param module
     */
    @Override
    public void removeByModule(String module) {
        ConcurrentMap concurrentMap = getCache().asMap();
        List<String> removeKeys = Lists.newArrayList();
        Set<String> keySet = concurrentMap.keySet();
        keySet.forEach(e -> {
            CacheKeyBuilder cacheKeyBuilder = JSON.parseObject(e, CacheKeyBuilder.class);
            if (cacheKeyBuilder.getCacheModule().equals(module)){
                removeKeys.add(e);
            }
        });
        if (CollectionUtils.isNotEmpty(removeKeys)) {
            getCache().invalidateAll(removeKeys);
        }
    }

    /**
     * 移除某个模块 某个分组下的缓存
     * @param module
     * @param group
     */
    @Override
    public void removeByModuleAndGroup(String module, String group){
        ConcurrentMap concurrentMap = getCache().asMap();
        List<String> removeKeys = Lists.newArrayList();
        Set<String> keySet = concurrentMap.keySet();
        keySet.forEach(e -> {
            CacheKeyBuilder cacheKeyBuilder = JSON.parseObject(e, CacheKeyBuilder.class);
            if (cacheKeyBuilder.getCacheModule().equals(module) && group.equals(cacheKeyBuilder.getGroup())){
                removeKeys.add(e);
            }
        });
        if (CollectionUtils.isNotEmpty(removeKeys)) {
            getCache().invalidateAll(removeKeys);
        }
    }

    /**
     * 根据缓存个数和过期时间初始化缓存的方法
     * @param expireDays
     * @param maximumSize
     * @param scanPath
     * @return
     */
    public LoadingCache<String, CacheData> initCache(Integer expireDays, Integer maximumSize) {

        //构建缓存对象
        Caffeine<Object, Object> builder = Caffeine.newBuilder();
        if(maximumSize != null){
            builder.maximumSize(maximumSize);
        }
        if(expireDays != null){
            builder.expireAfterAccess(expireDays, TimeUnit.DAYS);
        }
        return builder.recordStats()
                .build(key -> {
                    String cacheModule = CacheKey.getCacheModeByCacheKey(key);

                    CacheLoadMethodRegister methodRegister = this.methodRegister();
                    CacheLoadMethod loadMethod = methodRegister.getCacheLoadMethod(cacheModule);
                    if (loadMethod == null) {
                        return null;
                    }
                    Method method = loadMethod.getLoadMethod();
                    Object object = SmartApplicationContext.getBean(method.getDeclaringClass());
                    //方法返回值
                    Object result = null;
                    if (method.getParameterCount() == 0) {
                        result = method.invoke(object);
                    }else{
                        result = method.invoke(object, key);
                    }
                    if(result == null){
                        return null;
                    }
                    //指定咖啡因缓存返回值
                    Long expireTime = null;
                    if(loadMethod.getExpireSecond()>0){
                        expireTime = System.currentTimeMillis() + loadMethod.getExpireSecond() * 1000;
                    }
                    CacheData cacheData = new CacheData();
                    cacheData.setExpireTime(expireTime);
                    cacheData.setData(result);
                    return cacheData;
                });
    }
}
