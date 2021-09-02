package net.lab1024.smartadmin.service.module.support.idgenerator;

import net.lab1024.smartadmin.service.common.codeconst.ResponseCodeConst;
import net.lab1024.smartadmin.service.common.constant.RedisKeyConst;
import net.lab1024.smartadmin.service.common.domain.ResponseDTO;
import net.lab1024.smartadmin.service.module.support.idgenerator.constant.IdGeneratorEnum;
import net.lab1024.smartadmin.service.module.support.idgenerator.constant.IdGeneratorRuleTypeEnum;
import net.lab1024.smartadmin.service.module.support.idgenerator.domain.IdGeneratorEntity;
import net.lab1024.smartadmin.service.module.support.idgenerator.domain.IdGeneratorRecordDTO;
import net.lab1024.smartadmin.service.third.SmartRedisService;
import net.lab1024.smartadmin.service.util.SmartRandomUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 全局id生成器
 * zhuo
 */
@Slf4j
@Service
public class IdGeneratorService {

    private static final int MAX_GET_LOCK_COUNT = 5;

    private static final long SLEEP_MILLISECONDS = 500L;

    private static volatile long lastSleepMilliSeconds = SLEEP_MILLISECONDS;

    private ConcurrentHashMap<Integer, IdGeneratorEntity> idGeneratorMap;

    @Autowired
    private IdGeneratorDao idGeneratorDao;

    @Autowired
    private SmartRedisService redisService;

    @PostConstruct
    void init() {
        List<IdGeneratorEntity> idGeneratorEntityList = idGeneratorDao.selectAll();
        idGeneratorMap = idGeneratorEntityList.stream().collect(Collectors.toMap(IdGeneratorEntity::getId, Function.identity(), (x, y) -> y, ConcurrentHashMap::new));
    }

    /**
     * id 生成器
     *
     * @param idGeneratorEnum
     * @return
     */
    public ResponseDTO<String> generate(IdGeneratorEnum idGeneratorEnum) {
        int generatorId = idGeneratorEnum.getId();
        IdGeneratorEntity idGeneratorEntity = this.idGeneratorMap.get(generatorId);
        if (null == idGeneratorEntity) {
            return ResponseDTO.wrapMsg(ResponseCodeConst.ERROR_PARAM, "IdGenerator， 生成器 不存在" + generatorId);
        }

        LocalDateTime now = LocalDateTime.now();
        int year = now.getYear();
        int monthValue = now.getMonthValue();
        int dayOfMonth = now.getDayOfMonth();

        String lockKey = RedisKeyConst.Base.ID_GENERATOR + idGeneratorEnum.getKeyName();

        try {
            boolean lock = false;

            for (int i = 0; i < MAX_GET_LOCK_COUNT; i++) {
                try {
                    //60秒
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
                return ResponseDTO.wrap(ResponseCodeConst.BUSINESS_HANDING);
            }

            long beginTime = System.currentTimeMillis();

            IdGeneratorRecordDTO generatorRecordDTO = idGeneratorDao.selectHistoryLastNumber(generatorId, year, monthValue, dayOfMonth);
            if (generatorRecordDTO == null) {
                generatorRecordDTO = new IdGeneratorRecordDTO();
                generatorRecordDTO.setGeneratorId(generatorId);
                generatorRecordDTO.setYear(year);
                generatorRecordDTO.setMonth(monthValue);
                generatorRecordDTO.setDay(dayOfMonth);
                generatorRecordDTO.setLastNumber(idGeneratorEntity.getInitNumber());
                generatorRecordDTO.setCount(0L);
                generatorRecordDTO.setUpdateTime(now);
            }

            Long lastNumber = generatorRecordDTO.getLastNumber();
            IdGeneratorRuleTypeEnum ruleTypeEnum = this.getIdGeneratorRuleTypeEnum(idGeneratorEntity.getRuleType());
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(ruleTypeEnum.getExt());
            String nowFormat = now.format(formatter);
            if (IdGeneratorRuleTypeEnum.YEAR_CYCLE == ruleTypeEnum || IdGeneratorRuleTypeEnum.MONTH_CYCLE == ruleTypeEnum || IdGeneratorRuleTypeEnum.DAY_CYCLE == ruleTypeEnum) {
                if (!Objects.equals(generatorRecordDTO.getUpdateTime().format(formatter), nowFormat)) {
                    lastNumber = idGeneratorEntity.getInitNumber();
                }
            }

            lastNumber += SmartRandomUtil.nextInt(1, idGeneratorEntity.getStepRandomRange());
            long count = generatorRecordDTO.getCount() + 1;
            idGeneratorDao.replaceIdGeneratorRecord(generatorId, year, monthValue, dayOfMonth, lastNumber, count);

            // 格式化num 不足位数则补零
            int minLength = idGeneratorEntity.getMinLength();
            minLength = minLength <= 0 ? 1 : minLength;
            // 补位
            String finalId = String.format("%0" + minLength + "d", lastNumber);
            String prefix = StringUtils.isBlank(idGeneratorEntity.getPrefix()) ? StringUtils.EMPTY : idGeneratorEntity.getPrefix();

            lastSleepMilliSeconds = System.currentTimeMillis() - beginTime + 100;
            return ResponseDTO.succData(prefix + nowFormat + finalId);
        } catch (Throwable e) {
            log.error(e.getMessage(), e);
            throw e;
        } finally {
            redisService.unLock(lockKey);
        }
    }

    private IdGeneratorRuleTypeEnum getIdGeneratorRuleTypeEnum(String ruleType) {
        for (IdGeneratorRuleTypeEnum en : IdGeneratorRuleTypeEnum.values()) {
            if (en.name().equalsIgnoreCase(ruleType)) {
                return en;
            }
        }
        return null;
    }
}
