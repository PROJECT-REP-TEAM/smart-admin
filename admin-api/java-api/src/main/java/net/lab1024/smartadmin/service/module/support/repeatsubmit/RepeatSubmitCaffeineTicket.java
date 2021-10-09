package net.lab1024.smartadmin.service.module.support.repeatsubmit;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import net.lab1024.smartadmin.service.module.support.repeatsubmit.annoation.RepeatSubmit;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * [  ]
 *
 * @author yandanyang
 * @date 2021/10/9 19:10
 */
public class RepeatSubmitCaffeineTicket extends AbstractRepeatSubmitTicket {

    /**
     * 限制缓存最大数量 超过后先放入的会自动移除
     * 默认缓存时间
     */
    private static Cache<String, Long> cache = Caffeine.newBuilder()
            .maximumSize(5000)
            .expireAfterWrite(RepeatSubmit.MAX_INTERVAL, TimeUnit.MILLISECONDS).build();


    public RepeatSubmitCaffeineTicket(Function<String, String> ticketFunction) {
        super(ticketFunction);
    }

    @Override
    public Long ticketTimeStamp(String ticket) {
        return cache.getIfPresent(ticket);
    }


    @Override
    public void putTicket(String ticket) {
        cache.put(ticket, System.currentTimeMillis());
    }

    @Override
    void removeTicket(String ticket) {
        cache.invalidate(ticket);
    }
}
