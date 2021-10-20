package net.lab1024.smartadmin.service.module.support.idgenerator;

import com.google.common.collect.Interner;
import com.google.common.collect.Interners;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.smartadmin.service.common.util.SmartRandomUtil;
import net.lab1024.smartadmin.service.module.support.idgenerator.constant.IdGeneratorEnum;
import net.lab1024.smartadmin.service.module.support.idgenerator.constant.IdGeneratorRuleTypeEnum;
import net.lab1024.smartadmin.service.module.support.idgenerator.domain.IdGeneratorEntity;
import net.lab1024.smartadmin.service.module.support.idgenerator.domain.IdGeneratorRecordEntity;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 全局id生成器
 *
 * @author zhuo
 */
@Slf4j
@Service
public class IdGeneratorService {

    private static final Interner<Integer> POOL = Interners.newWeakInterner();

    @Autowired
    private IdGeneratorDao idGeneratorDao;

    @Autowired
    private IdGeneratorRecordDao idGeneratorRecordDao;

    private Map<Integer, IdGeneratorEntity> idGeneratorMap;

    @PostConstruct
    public void init() {
        List<IdGeneratorEntity> idGeneratorEntityList = idGeneratorDao.selectList(null);
        idGeneratorMap = idGeneratorEntityList.stream().collect(Collectors.toMap(IdGeneratorEntity::getId, Function.identity()));
        log.info("##################### init IdGenerator #####################");
    }

    /**
     * id生成
     *
     * @param idGeneratorEnum 类型
     * @return
     */
    public String generate(IdGeneratorEnum idGeneratorEnum) {
        int generatorId = idGeneratorEnum.getValue();
        IdGeneratorEntity idGeneratorEntity = this.idGeneratorMap.get(generatorId);
        Assert.notNull(idGeneratorEntity, "IdGenerator不存在 " + idGeneratorEntity.getRuleType());

        // 校验生成规则
        IdGeneratorRuleTypeEnum ruleTypeEnum = this.getIdGeneratorRuleTypeEnum(idGeneratorEntity.getRuleType());
        Assert.notNull(ruleTypeEnum, "IdGenerator rule type 不存在 " + idGeneratorEntity.getRuleType());

        // 默认起始值
        Long startNumber = idGeneratorEntity.getInitNumber();

        // 判断是否有循环规则
        String timeFormat = null;
        DateTimeFormatter timeFormatter = null;
        if (IdGeneratorRuleTypeEnum.YEAR_CYCLE == ruleTypeEnum || IdGeneratorRuleTypeEnum.MONTH_CYCLE == ruleTypeEnum || IdGeneratorRuleTypeEnum.DAY_CYCLE == ruleTypeEnum) {
            timeFormatter = DateTimeFormatter.ofPattern(ruleTypeEnum.getValue());
            timeFormat = LocalDateTime.now().format(timeFormatter);
        }

        synchronized (POOL.intern(generatorId)) {
            // 获取最后一次生成记录
            boolean isFirst = false;
            IdGeneratorRecordEntity recordEntity = idGeneratorRecordDao.selectHistoryLastNumber(generatorId, timeFormat);
            if (recordEntity == null) {
                recordEntity = new IdGeneratorRecordEntity();
                recordEntity.setGeneratorId(generatorId);
                recordEntity.setTime(timeFormat);
                recordEntity.setLastNumber(startNumber);
                recordEntity.setCount(1L);
                idGeneratorRecordDao.insert(recordEntity);

                isFirst = true;
            }

            // 没有循环 或 在同个循环周期内，起始值 = 上次id
            if (IdGeneratorRuleTypeEnum.NO_CYCLE == ruleTypeEnum || Objects.equals(recordEntity.getUpdateTime().format(timeFormatter), timeFormat)) {
                startNumber = recordEntity.getLastNumber();
            }

            // 在范围内随机生成此次增加的数值 更新id生成记录
            if (!isFirst) {
                startNumber += SmartRandomUtil.nextInt(1, idGeneratorEntity.getStepRandomRange());
                IdGeneratorRecordEntity updateRecordEntity = new IdGeneratorRecordEntity();
                updateRecordEntity.setId(recordEntity.getId());
                updateRecordEntity.setLastNumber(startNumber);
                updateRecordEntity.setCount(recordEntity.getCount() + 1);
                idGeneratorRecordDao.updateById(updateRecordEntity);
            }

            // 默认 最低长度 1
            int minLength = NumberUtils.max(idGeneratorEntity.getMinLength(), 1);
            // id长度补位
            String finalId = String.format("%0" + minLength + "d", startNumber);
            if (null != timeFormat) {
                finalId = timeFormat + finalId;
            }
            // 前缀
            if (StringUtils.isNotBlank(idGeneratorEntity.getPrefix())) {
                finalId = idGeneratorEntity.getPrefix() + finalId;
            }
            return finalId;
        }

    }

    /**
     * 查询生成规则
     *
     * @param ruleType
     * @return 没有则返回null
     */
    private IdGeneratorRuleTypeEnum getIdGeneratorRuleTypeEnum(String ruleType) {
        return Arrays.stream(IdGeneratorRuleTypeEnum.values())
                     .filter(e -> StringUtils.equalsIgnoreCase(e.name(), ruleType))
                     .findFirst().orElse(null);
    }
}
