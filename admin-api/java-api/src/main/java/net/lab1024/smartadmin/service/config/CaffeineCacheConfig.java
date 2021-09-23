package net.lab1024.smartadmin.service.config;

import com.github.benmanes.caffeine.cache.LoadingCache;
import net.lab1024.smartadmin.service.module.support.beancache.cache.AbstractCaffeineCache;
import net.lab1024.smartadmin.service.module.support.beancache.cache.AbstractDisableCache;
import net.lab1024.smartadmin.service.module.support.beancache.cache.IBeanCache;
import net.lab1024.smartadmin.service.module.support.beancache.domain.CacheData;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * [  ]
 *
 * @author zhuoda
 */
@Configuration
public class CaffeineCacheConfig {
    @Value("${cache.maximumSize:5000}")
    private Integer cacheMaximumSize;

    @Value("${cache.expireDays:5}")
    private Integer expireDays;

    @Value("${cache.scanPath}")
    private String scanPath;

    @Bean
    @Primary
    public IBeanCache beanCache() {
        return new AbstractCaffeineCache() {
            LoadingCache<String, CacheData> cache = this.initCache(cacheMaximumSize, expireDays, scanPath);

            @Override
            public LoadingCache getCache() {
                return cache;
            }
        };
    }

    @Bean
    @ConditionalOnMissingBean(IBeanCache.class)
    public IBeanCache beanDisableCache() {
        return new AbstractDisableCache(scanPath);
    }
}

