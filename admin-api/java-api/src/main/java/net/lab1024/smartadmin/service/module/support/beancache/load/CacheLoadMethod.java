package net.lab1024.smartadmin.service.module.support.beancache.load;

import lombok.Data;

import java.lang.reflect.Method;

/**
 * [  ]
 *
 * @author 罗伊
 * @date 2021/4/22 17:28
 */
@Data
public class CacheLoadMethod {

    /**
     * 缓存模块
     */
    private String cacheModule;
    /**
     * 缓存过期时间
     */
    private long expireSecond;

    /**
     * 缓存加载方法
     */
    private Method loadMethod;
}
