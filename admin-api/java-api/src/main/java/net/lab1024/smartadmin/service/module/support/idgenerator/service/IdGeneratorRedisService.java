package net.lab1024.smartadmin.service.module.support.idgenerator.service;

import lombok.extern.slf4j.Slf4j;
import net.lab1024.smartadmin.service.common.exception.BusinessException;
import net.lab1024.smartadmin.service.constant.RedisKeyConst;
import net.lab1024.smartadmin.service.module.support.idgenerator.constant.IdGeneratorEnum;
import net.lab1024.smartadmin.service.module.support.idgenerator.domain.IdGeneratorEntity;
import net.lab1024.smartadmin.service.module.support.redis.RedisService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author zhuoda
 * @Date 2021-11-10
 */

@Slf4j
public class IdGeneratorRedisService extends IdGeneratorBaseService {

    private static final int MAX_GET_LOCK_COUNT = 5;

    private static final long SLEEP_MILLISECONDS = 500L;

    private static volatile long lastSleepMilliSeconds = SLEEP_MILLISECONDS;

    @Autowired
    private RedisService redisService;

    @Override
    List<String> tryGenerator(IdGeneratorEnum idGeneratorEnum, int count) {
        String lockKey = RedisKeyConst.Support.ID_GENERATOR + idGeneratorEnum.getValue();
        try {
            boolean lock = false;
            for (int i = 0; i < MAX_GET_LOCK_COUNT; i++) {
                try {
                    lock = redisService.getLock(lockKey, 60 * 1000L);
                    if (lock) {
                        break;
                    }
                    Thread.sleep(Math.max(SLEEP_MILLISECONDS, lastSleepMilliSeconds));
                } catch (Throwable e) {
                    log.error(e.getMessage(), e);
                }
            }
            if (!lock) {
                throw new BusinessException("系统繁忙");
            }
            long beginTime = System.currentTimeMillis();

            List<String> list = generate(idGeneratorEnum, count);
            lastSleepMilliSeconds = System.currentTimeMillis() - beginTime + 100;
            return list;
        } catch (Throwable e) {
            log.error(e.getMessage(), e);
            throw e;
        } finally {
            redisService.unLock(lockKey);
        }
    }
}
