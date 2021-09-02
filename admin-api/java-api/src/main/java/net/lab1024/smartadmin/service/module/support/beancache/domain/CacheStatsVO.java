package net.lab1024.smartadmin.service.module.support.beancache.domain;

import lombok.Data;

/**
 * [  ]
 *
 * @author 罗伊
 * @date 2020/9/8 11:14
 */
@Data
public class CacheStatsVO {
    /**
     * 记录缓存请求数量
     */
    private long requestCount;
    /**
     * 记录缓存命中
     */
    private long hitCount;
    /**
     * 记录缓存未命中
     */
    private long missCount;
    /**
     * CacheLoader加载成功
     */
    private long loadSuccessCount;
    /**
     * CacheLoader加载成功加载失败
     */
    private long loadFailureCount;
    /**
     * 总加载时间
     */
    private long totalLoadTime;
    /**
     * 缓存失效的数量
     */
    private long evictionCount;

    /**
     * 返回缓存命中率
     */
    private double hitRate;

    /**
     * 返回缓存命中率
     */
    private double missRate;

    /**
     * 加载新值的平均时间
     */
    private double averageLoadPenalty;
}
