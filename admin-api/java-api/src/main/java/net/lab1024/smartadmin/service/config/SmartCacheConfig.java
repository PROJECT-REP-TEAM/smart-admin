package net.lab1024.smartadmin.service.config;

import com.github.benmanes.caffeine.cache.LoadingCache;
import com.google.common.collect.Maps;
import net.lab1024.smartadmin.service.module.support.beancache.cache.AbstractCaffeineCache;
import net.lab1024.smartadmin.service.module.support.beancache.cache.AbstractDisableCache;
import net.lab1024.smartadmin.service.module.support.beancache.cache.IBeanCache;
import net.lab1024.smartadmin.service.module.support.beancache.domain.CacheData;
import net.lab1024.smartadmin.service.module.support.beancache.load.CacheLoad;
import net.lab1024.smartadmin.service.module.support.beancache.load.CacheLoadMethod;
import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.util.ConfigurationBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;

/**
 * [  ]
 *
 * @author 罗伊
 * @date 2020/9/6 16:01
 */
@Configuration
public class SmartCacheConfig {

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
            LoadingCache<String, CacheData> cache = this.initCache(cacheMaximumSize,expireDays, scanPath);
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
