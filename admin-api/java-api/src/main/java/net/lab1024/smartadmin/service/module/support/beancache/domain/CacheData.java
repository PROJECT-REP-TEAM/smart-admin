package net.lab1024.smartadmin.service.module.support.beancache.domain;

import lombok.Data;

/**
 * [  ]
 *
 * @author 罗伊
 * @date 2021/4/22 17:21
 */
@Data
public class CacheData<T> {

    /**
     * 过期时间
     */
    private Long expireTime;

    /**
     * 缓存数据
     */
    private T data;
}
