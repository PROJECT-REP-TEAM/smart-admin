package net.lab1024.smartadmin.service.module.support.idgenerator.strategy;

import net.lab1024.smartadmin.service.common.util.SmartBaseEnumUtil;
import net.lab1024.smartadmin.service.common.util.SmartRandomUtil;
import net.lab1024.smartadmin.service.module.support.idgenerator.constant.IdGeneratorRuleTypeEnum;
import net.lab1024.smartadmin.service.module.support.idgenerator.dao.IdGeneratorRecordDao;
import net.lab1024.smartadmin.service.module.support.idgenerator.domain.IdGeneratorEntity;
import net.lab1024.smartadmin.service.module.support.idgenerator.domain.IdGeneratorRecordEntity;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * [  ]
 *
 * @author yandanyang
 * @date 2021/11/9 19:12
 */
public abstract class IdGeneratorStrategyBaseService implements IIdGeneratorStrategy{

    @Autowired
    private IdGeneratorRecordDao idGeneratorRecordDao;


    public String generate(IdGeneratorEntity idGeneratorEntity){
        // 校验生成规则
        IdGeneratorRuleTypeEnum ruleTypeEnum = SmartBaseEnumUtil.getEnumByName(idGeneratorEntity.getRuleType(),IdGeneratorRuleTypeEnum.class);
        // 默认起始值
        Long startNumber = idGeneratorEntity.getInitNumber();
        Integer generatorId = idGeneratorEntity.getId();
        // 判断是否有循环规则
        String timeFormat = null;
        DateTimeFormatter timeFormatter = null;
        if (IdGeneratorRuleTypeEnum.YEAR_CYCLE == ruleTypeEnum || IdGeneratorRuleTypeEnum.MONTH_CYCLE == ruleTypeEnum || IdGeneratorRuleTypeEnum.DAY_CYCLE == ruleTypeEnum) {
            timeFormatter = DateTimeFormatter.ofPattern(ruleTypeEnum.getValue());
            timeFormat = LocalDateTime.now().format(timeFormatter);
        }
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
