package net.lab1024.smartadmin.service.module.support.beancache.key;

import com.alibaba.fastjson.JSON;

/**
 * [  ]
 *
 * @author 罗伊
 * @date 2020/9/6 17:20
 */
public class CacheKey {

    /**
     * 构建缓存key信息
     *
     * @param cacheModule
     * @param group
     * @param businessId
     * @return
     */
    public static String cacheKey(String cacheModule, String group, String businessId) {
        CacheKeyBuilder cacheKeyBuilder = CacheKeyBuilder.builder()
                .cacheModule(cacheModule)
                .group(group)
                .businessId(businessId).build();
        return cacheKey(cacheKeyBuilder);
    }

    public static String cacheKeyGroup(String cacheModule, String group) {
        CacheKeyBuilder cacheKeyBuilder = CacheKeyBuilder.builder()
                .cacheModule(cacheModule)
                .group(group).build();
        return cacheKey(cacheKeyBuilder);
    }

    /**
     * 构建缓存key信息
     *
     * @param cacheModule
     * @param businessId
     * @return
     */
    public static String cacheKey(String cacheModule, String businessId) {
        CacheKeyBuilder cacheKeyBuilder = CacheKeyBuilder.builder()
                .cacheModule(cacheModule)
                .businessId(businessId).build();
        return cacheKey(cacheKeyBuilder);
    }


    public static String cacheKey(String cacheModule) {
        CacheKeyBuilder cacheKeyBuilder = CacheKeyBuilder.builder()
                .cacheModule(cacheModule).build();
        return cacheKey(cacheKeyBuilder);
    }

    /**
     * 构建缓存key信息
     *
     * @param cacheKeyBuilder
     * @return
     */
    public static String cacheKey(CacheKeyBuilder cacheKeyBuilder) {
        return JSON.toJSONString(cacheKeyBuilder);
    }

    /**
     * 通过缓存key获取对应的method key
     *
     * @param cacheKey
     * @return
     */
    public static String getCacheModeByCacheKey(String cacheKey) {
        CacheKeyBuilder cacheKeyBuilder = JSON.parseObject(cacheKey, CacheKeyBuilder.class);
        return cacheKeyBuilder.getCacheModule();
    }

    /**
     * 通过缓存key获取对应的businessId
     *
     * @param cacheKey
     * @return
     */
    public static String getBusinessIdByCacheKey(String cacheKey) {
        CacheKeyBuilder cacheKeyBuilder = JSON.parseObject(cacheKey, CacheKeyBuilder.class);
        return cacheKeyBuilder.getBusinessId();
    }

    public static String getGroupIdByCacheKey(String cacheKey) {
        CacheKeyBuilder cacheKeyBuilder = JSON.parseObject(cacheKey, CacheKeyBuilder.class);
        return cacheKeyBuilder.getGroup();
    }

}
