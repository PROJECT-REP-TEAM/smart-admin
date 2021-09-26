package net.lab1024.smartadmin.service.module.support.beancache.cache;

import java.util.Set;

/**
 * [  ]
 *
 * @author 罗伊
 * @date 2020/9/6 15:55
 */
public interface IBeanCache {

    CacheLoadMethodRegister methodRegister();
    /**
     * 获取缓存
     *
     * @return
     */
    <T> T getCache();

    /**
     * 移除某个缓存
     *
     * @param key
     */
    void remove(String key);

    /**
     * 清除所有
     */
    void clear();

    /**
     * 获取某个缓存对象
     *
     * @param key
     * @return
     */
    <T> T get(String key);

    /**
     * 刷新某个缓存key-异步刷新
     *
     * @param key
     */
    void refresh(String key);

    /**
     * 设置key->value
     *
     * @param key
     * @param obj
     */
    void put(String key, Object obj);

    /**
     * 待过期时间get
     *
     * @param key
     * @param obj
     * @param expireSecond
     */
    void put(String key, Object obj, long expireSecond);

    /**
     * 判断是否包含某个key
     *
     * @param key
     * @return
     */
    boolean containsKey(String key);

    /**
     * 所有key集合
     *
     * @return
     */
    Set<String> keySet();

    /**
     * 移除某个模块的所有key
     *
     * @param module
     */
    void removeByModule(String module);

    /**
     * 移除某个模块 某个分组下的缓存
     *
     * @param module
     * @param group
     */
    void removeByModuleAndGroup(String module, String group);

}