package net.lab1024.smartadmin.service.module.support.idgenerator.service;

import net.lab1024.smartadmin.service.common.exception.BusinessException;
import net.lab1024.smartadmin.service.common.util.SmartBaseEnumUtil;
import net.lab1024.smartadmin.service.common.util.SmartRandomUtil;
import net.lab1024.smartadmin.service.module.support.idgenerator.constant.IdGeneratorEnum;
import net.lab1024.smartadmin.service.module.support.idgenerator.constant.IdGeneratorRuleTypeEnum;
import net.lab1024.smartadmin.service.module.support.idgenerator.dao.IdGeneratorDao;
import net.lab1024.smartadmin.service.module.support.idgenerator.dao.IdGeneratorRecordDao;
import net.lab1024.smartadmin.service.module.support.idgenerator.domain.IdGeneratorEntity;
import net.lab1024.smartadmin.service.module.support.idgenerator.domain.IdGeneratorRecordEntity;
import net.lab1024.smartadmin.service.module.support.idgenerator.manager.IdGeneratorCacheManager;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author zhuoda
 * @Date 2021-11-10
 */
public abstract class IdGeneratorBaseService implements IdGeneratorService {

    @Autowired
    protected IdGeneratorCacheManager idGeneratorCacheManager;

    @Autowired
    protected IdGeneratorRecordDao idGeneratorRecordDao;

    @Autowired
    protected IdGeneratorDao idGeneratorDao;

    abstract List<String> tryGenerator(IdGeneratorEnum idGeneratorEnum, int count);

    @Override
    public String generate(IdGeneratorEnum idGeneratorEnum) {
        List<String> generateList = this.generate(idGeneratorEnum, 1);
        if (generateList == null || generateList.isEmpty()) {
            throw new BusinessException("cannot generate id : " + idGeneratorEnum.toString());
        }
        return generateList.get(0);
    }

    @Override
    public List<String> generate(IdGeneratorEnum idGeneratorEnum, int count) {
        IdGeneratorEntity idGeneratorEntity = idGeneratorCacheManager.getIdGeneratorEntity(idGeneratorEnum.getIdGeneratorId());
        if (idGeneratorEntity == null) {
            throw new BusinessException("cannot found id generator : " + idGeneratorEnum.toString());
        }
        return this.tryGenerator(idGeneratorEnum, count);
    }

    protected List<String> generateIdList(IdGeneratorEntity idGeneratorEntity, int count) {
        // 校验生成规则
        IdGeneratorRuleTypeEnum ruleTypeEnum = SmartBaseEnumUtil.getEnumByName(idGeneratorEntity.getRuleType(), IdGeneratorRuleTypeEnum.class);
        if (ruleTypeEnum == null) {
            throw new BusinessException("cannot found IdGeneratorRuleTypeEnum,  id generator : " + idGeneratorEntity.getBusinessName());
        }

        // 有循环周期
        String timeFormat = null;
        DateTimeFormatter timeFormatter = null;
        if (IdGeneratorRuleTypeEnum.YEAR_CYCLE == ruleTypeEnum || IdGeneratorRuleTypeEnum.MONTH_CYCLE == ruleTypeEnum || IdGeneratorRuleTypeEnum.DAY_CYCLE == ruleTypeEnum) {
            timeFormatter = DateTimeFormatter.ofPattern(ruleTypeEnum.getValue());
            timeFormat = LocalDateTime.now().format(timeFormatter);
        }
        // 上次的值
        Long lastNumber = null;
        IdGeneratorRecordEntity recordEntity = idGeneratorRecordDao.selectHistoryLastNumber(idGeneratorEntity.getId(), timeFormat);
        // 没有循环 或 在同个循环周期内，起始值 = 上次id
        boolean isSameTime = recordEntity != null && Objects.equals(recordEntity.getUpdateTime().format(timeFormatter), timeFormat);
        if (IdGeneratorRuleTypeEnum.NO_CYCLE == ruleTypeEnum || isSameTime) {
            lastNumber = recordEntity.getLastNumber();
        } else {
            // 重头开始
            lastNumber = idGeneratorEntity.getInitNumber();
        }

        //批量生成
        List<String> list = new ArrayList<>();
        long stepValue = lastNumber;
        boolean isNeedRandom = idGeneratorEntity.getStepRandomRange() > 1;
        boolean existPrefix = StringUtils.isNotBlank(idGeneratorEntity.getPrefix());
        for (int i = 0; i < count; i++) {
            // 需要随机
            if (isNeedRandom) {
                //随机长度
                int randomLength = SmartRandomUtil.nextInt(1, idGeneratorEntity.getStepRandomRange());
                stepValue = stepValue + randomLength;
            } else {
                stepValue = stepValue + 1;
            }

            // 默认 最低长度 1
            int minLength = NumberUtils.max(idGeneratorEntity.getMinLength(), 1);
            // id长度补位
            String finalId = String.format("%0" + minLength + "d", stepValue);
            if (null != timeFormat) {
                finalId = timeFormat + finalId;
            }
            // 前缀
            if (existPrefix) {
                finalId = idGeneratorEntity.getPrefix() + finalId;
            }
            list.add(finalId);
        }
        // 保存记录
        saveRecord(idGeneratorEntity.getId(), recordEntity, timeFormat, stepValue);
        return list;
    }


    private void saveRecord(Integer idGeneratorId, IdGeneratorRecordEntity recordEntity, String time, long lastNumber) {
        if (recordEntity == null) {
            insertRecord(idGeneratorId, time, lastNumber);
        } else {
            updateRecord(recordEntity, time, lastNumber);
        }
    }

    private void insertRecord(Integer idGeneratorId, String time, long lastNumber) {
        IdGeneratorRecordEntity recordEntity = new IdGeneratorRecordEntity();
        recordEntity.setGeneratorId(idGeneratorId);
        recordEntity.setTime(time);
        recordEntity.setLastNumber(lastNumber);
        recordEntity.setCount(1L);
        idGeneratorRecordDao.insert(recordEntity);
    }

    private void updateRecord(IdGeneratorRecordEntity updateRecordEntity, String time, long lastNumber) {
        updateRecordEntity.setTime(time);
        updateRecordEntity.setLastNumber(lastNumber);
        updateRecordEntity.setCount(updateRecordEntity.getCount() + 1);
        idGeneratorRecordDao.updateById(updateRecordEntity);
    }

}
