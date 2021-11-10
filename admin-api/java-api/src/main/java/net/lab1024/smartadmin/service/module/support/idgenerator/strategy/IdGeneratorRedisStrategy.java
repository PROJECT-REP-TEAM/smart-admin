package net.lab1024.smartadmin.service.module.support.idgenerator.strategy;

import lombok.extern.slf4j.Slf4j;
import net.lab1024.smartadmin.service.common.exception.BusinessException;
import net.lab1024.smartadmin.service.constant.RedisKeyConst;
import net.lab1024.smartadmin.service.module.support.idgenerator.constant.IdGeneratorEnum;
import net.lab1024.smartadmin.service.module.support.idgenerator.constant.IdGeneratorStrategyTypeEnum;
import net.lab1024.smartadmin.service.module.support.idgenerator.domain.IdGeneratorEntity;
import net.lab1024.smartadmin.service.module.support.idgenerator.service.IdGeneratorCacheManager;
import net.lab1024.smartadmin.service.module.support.redis.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * [  ]
 *
 * @author yandanyang
 * @date 2021/11/9 18:48
 */
@Slf4j
@Service
public class IdGeneratorRedisStrategy extends IdGeneratorStrategyBaseService {

    private static final int MAX_GET_LOCK_COUNT = 5;

    private static final long SLEEP_MILLISECONDS = 500L;

    private static volatile long lastSleepMilliSeconds = SLEEP_MILLISECONDS;

    @Autowired
    private IdGeneratorCacheManager idGeneratorCacheManager;
    @Autowired
    private RedisService redisService;

    /**
     * 策略类型
     *
     * @return
     */
    @Override
    public IdGeneratorStrategyTypeEnum getStrategyType() {
        return IdGeneratorStrategyTypeEnum.REDIS;
    }

    /**
     * 生成
     *
     * @param idGeneratorEnum
     * @return
     */
    @Override
    public String generate(IdGeneratorEnum idGeneratorEnum) {
        IdGeneratorEntity idGeneratorEntity = idGeneratorCacheManager.getIdGeneratorEntity(idGeneratorEnum.getValue());
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

            String id = generate(idGeneratorEntity);
            lastSleepMilliSeconds = System.currentTimeMillis() - beginTime + 100;
            return id;
        } catch (Throwable e) {
            log.error(e.getMessage(), e);
            throw e;
        } finally {
            redisService.unLock(lockKey);
        }
    }
}
