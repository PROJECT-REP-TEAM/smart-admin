package net.lab1024.smartadmin.service.module.support.cache;

import com.google.common.collect.Lists;
import net.lab1024.smartadmin.service.module.support.reload.core.annoation.SmartReload;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * [  ]
 *
 * @author yandanyang
 * @date 2021/10/11 20:07
 */
@Service
public class CacheService {

    @Resource
    private CaffeineCacheManager caffeineCacheManager;

    /**
     * 获取所有缓存名称
     * @return
     */
    public List<String> cacheNames(){
        return Lists.newArrayList(caffeineCacheManager.getCacheNames());
    }


    /**
     * 移除某个key
     * @param cacheName
     */
    @SmartReload("removeCache")
    public void removeCache(String cacheName){
        CaffeineCache cache = (CaffeineCache) caffeineCacheManager.getCache(cacheName);
        if(cache != null){
            cache.clear();
        }
    }

    /**
     * 某个缓存下的所有key
     * @param cacheName
     * @return
     */
    public List<String> cacheKey(String cacheName){
        CaffeineCache cache = (CaffeineCache) caffeineCacheManager.getCache(cacheName);
        if(cache == null){
            return Lists.newArrayList();
        }
        Set<Object> cacheKey = cache.getNativeCache().asMap().keySet();
        return cacheKey.stream().map(e->e.toString()).collect(Collectors.toList());
    }
}
