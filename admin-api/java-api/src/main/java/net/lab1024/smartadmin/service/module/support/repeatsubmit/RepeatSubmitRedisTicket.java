package net.lab1024.smartadmin.service.module.support.repeatsubmit;

import net.lab1024.smartadmin.service.module.support.repeatsubmit.annoation.RepeatSubmit;
import org.springframework.data.redis.core.ValueOperations;

import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * [  ]
 *
 * @author yandanyang
 * @date 2021/10/9 19:10
 */
public class RepeatSubmitRedisTicket extends AbstractRepeatSubmitTicket {

    private ValueOperations<String, String> redisValueOperations;

    public RepeatSubmitRedisTicket(ValueOperations<String, String> redisValueOperations,
                                   Function<String, String> ticketFunction) {
        super(ticketFunction);
        this.redisValueOperations = redisValueOperations;
    }

    @Override
    public Long ticketTimeStamp(String ticket) {
        Long timeStamp = System.currentTimeMillis();
        boolean setFlag = redisValueOperations.setIfAbsent(ticket, String.valueOf(timeStamp), RepeatSubmit.MAX_INTERVAL, TimeUnit.MILLISECONDS);
        if(!setFlag){
            timeStamp = Long.valueOf(redisValueOperations.get(ticket));
        }
        return timeStamp;
    }

    @Override
    public void putTicket(String ticket) {
        redisValueOperations.getOperations().delete(ticket);
        this.ticketTimeStamp(ticket);
    }

    @Override
    void removeTicket(String ticket) {
        redisValueOperations.getOperations().delete(ticket);
    }
}
