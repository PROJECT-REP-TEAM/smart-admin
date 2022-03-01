package net.lab1024.smartadmin.service.module.support.serialnumber.service;

import com.google.common.collect.Lists;
import net.lab1024.smartadmin.service.common.exception.BusinessException;
import net.lab1024.smartadmin.service.common.util.SmartBaseEnumUtil;
import net.lab1024.smartadmin.service.common.util.SmartRandomUtil;
import net.lab1024.smartadmin.service.common.util.date.SmartDateFormatterEnum;
import net.lab1024.smartadmin.service.common.util.date.SmartLocalDateUtil;
import net.lab1024.smartadmin.service.module.support.serialnumber.constant.SerialNumberIdEnum;
import net.lab1024.smartadmin.service.module.support.serialnumber.constant.SerialNumberRuleTypeEnum;
import net.lab1024.smartadmin.service.module.support.serialnumber.dao.SerialNumberDao;
import net.lab1024.smartadmin.service.module.support.serialnumber.dao.SerialNumberRecordDao;
import net.lab1024.smartadmin.service.module.support.serialnumber.domain.SerialNumberBO;
import net.lab1024.smartadmin.service.module.support.serialnumber.domain.SerialNumberEntity;
import net.lab1024.smartadmin.service.module.support.serialnumber.domain.SerialNumberRecordEntity;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zhuoda
 * @Date 2021-11-10
 */
public abstract class SerialNumberBaseService implements SerialNumberService {

    @Autowired
    protected SerialNumberRecordDao serialNumberRecordDao;

    @Autowired
    protected SerialNumberDao serialNumberDao;

    private ConcurrentHashMap<Integer, SerialNumberBO> serialNumberMap = new ConcurrentHashMap<>();

    @PostConstruct
    void init() {
        List<SerialNumberEntity> serialNumberEntityList = serialNumberDao.selectList(null);
        if (serialNumberEntityList != null) {
            for (SerialNumberEntity serialNumberEntity : serialNumberEntityList) {
                SerialNumberRuleTypeEnum ruleTypeEnum = SmartBaseEnumUtil.getEnumByValue(serialNumberEntity.getRuleType(), SerialNumberRuleTypeEnum.class);
                if (ruleTypeEnum == null) {
                    throw new ExceptionInInitializerError("cannot find rule type , id : " + serialNumberEntity.getSerialNumberId());
                }

                String format = serialNumberEntity.getFormat();
                int startIndex = format.indexOf("[n");
                int endIndex = format.indexOf("n]");
                if (startIndex == -1 || endIndex == -1 || endIndex <= startIndex) {
                    throw new ExceptionInInitializerError("[nnn] 配置错误，请仔细查看 id : " + serialNumberEntity.getSerialNumberId());
                }

                if (serialNumberEntity.getStepRandomRange() < 1) {
                    throw new ExceptionInInitializerError("random step range must greater than 1 " + serialNumberEntity.getSerialNumberId());
                }

                SerialNumberBO serialNumberBO = SerialNumberBO.builder()
                        .serialNumberRuleTypeEnum(ruleTypeEnum)
                        .haveDayFlag(format.contains("[dd]"))
                        .haveMonthFlag(format.contains("[mm]"))
                        .haveYearFlag(format.contains("[yyyy]"))
                        .numberCount(endIndex - startIndex)
                        .build();

                String recordDate = null;
                SmartDateFormatterEnum dateFormatterEnum = ruleTypeEnum.getDateFormatterEnum();
                if (dateFormatterEnum != null) {
                    recordDate = SmartLocalDateUtil.format(LocalDate.now(), dateFormatterEnum);
                }
                Long recordId = serialNumberRecordDao.selectRecordIdBySerialNumberIdAndDate(serialNumberEntity.getSerialNumberId(), recordDate);
                serialNumberBO.setSerialNumberRecordId(recordId);
                this.serialNumberMap.put(serialNumberEntity.getSerialNumberId(), serialNumberBO);
            }
        }
    }

    abstract List<Long> generateSerialNumberList(SerialNumberBO serialNumber, int count);

    protected List<Long> loopNumberList(SerialNumberBO serialNumberBO, int count) {
        long lastNumber = serialNumberBO.getLastNumber();
        if (isResetInitNumber(serialNumberBO)) {
            lastNumber = serialNumberBO.getInitNumber();
        }

        ArrayList<Long> numberList = Lists.newArrayListWithCapacity(count);
        for (int i = 0; i < count; i++) {
            Integer stepRandomRange = serialNumberBO.getStepRandomRange();
            if (stepRandomRange > 1) {
                lastNumber = lastNumber + RandomUtils.nextInt(1, stepRandomRange + 1);
            } else {
                lastNumber = lastNumber + 1;
            }

            numberList.add(lastNumber);
        }
        // 更新内存数据
        serialNumberBO.setLastNumber(lastNumber);
        serialNumberBO.setLastTime(LocalDateTime.now());
        return numberList;
    }

    /**
     * 若不在规则周期内，重制初始值
     *
     * @param serialNumber
     * @return
     */
    private boolean isResetInitNumber(SerialNumberBO serialNumber) {
        SerialNumberRuleTypeEnum serialNumberRuleTypeEnum = serialNumber.getSerialNumberRuleTypeEnum();
        int lastTimeYear = serialNumber.getLastTime().getYear();
        int lastTimeMonth = serialNumber.getLastTime().getMonthValue();
        int lastTimeDay = serialNumber.getLastTime().getDayOfYear();

        LocalDateTime now = LocalDateTime.now();

        switch (serialNumberRuleTypeEnum) {
            case YEAR_CYCLE:
                return lastTimeYear != now.getYear();
            case MONTH_CYCLE:
                return lastTimeYear != now.getYear() || lastTimeMonth != now.getMonthValue();
            case DAY_CYCLE:
                return lastTimeYear != now.getYear() || lastTimeDay != now.getDayOfYear();
            default:
                return false;
        }
    }

    @Override
    public String generate(SerialNumberIdEnum serialNumberIdEnum) {
        List<String> generateList = this.generate(serialNumberIdEnum, 1);
        if (generateList == null || generateList.isEmpty()) {
            throw new BusinessException("cannot generate : " + serialNumberIdEnum.toString());
        }
        return generateList.get(0);
    }

    @Override
    public List<String> generate(SerialNumberIdEnum serialNumberIdEnum, int count) {
        SerialNumberBO serialNumberBO = serialNumberMap.get(serialNumberIdEnum.getSerialNumberId());
        if (serialNumberBO == null) {
            throw new BusinessException("cannot found SerialNumberId : " + serialNumberIdEnum.toString());
        }
        return this.generateSerialNumberList(serialNumberBO, count);
    }


    protected List<String> buildSerialNumberList(SerialNumberEntity serialNumberEntity, int count) {
        // 校验生成规则
        SerialNumberRuleTypeEnum ruleTypeEnum = SmartBaseEnumUtil.getEnumByName(serialNumberEntity.getRuleType(), SerialNumberRuleTypeEnum.class);
        if (ruleTypeEnum == null) {
            throw new BusinessException("cannot found IdGeneratorRuleTypeEnum,  id generator : " + serialNumberEntity.getBusinessName());
        }

        // 有循环周期
        String timeFormat = null;
        DateTimeFormatter timeFormatter = null;
        if (SerialNumberRuleTypeEnum.YEAR_CYCLE == ruleTypeEnum || SerialNumberRuleTypeEnum.MONTH_CYCLE == ruleTypeEnum || SerialNumberRuleTypeEnum.DAY_CYCLE == ruleTypeEnum) {
            timeFormatter = DateTimeFormatter.ofPattern(ruleTypeEnum.getValue());
            timeFormat = LocalDateTime.now().format(timeFormatter);
        }
        // 上次的值
        Long lastNumber = null;
        SerialNumberRecordEntity recordEntity = serialNumberRecordDao.selectHistoryLastNumber(serialNumberEntity.getSerialNumberId(), timeFormat);
        // 没有循环 或 在同个循环周期内，起始值 = 上次id
        boolean isSameTime = recordEntity != null && Objects.equals(recordEntity.getUpdateTime().format(timeFormatter), timeFormat);
        if (SerialNumberRuleTypeEnum.NO_CYCLE == ruleTypeEnum || isSameTime) {
            lastNumber = recordEntity.getLastNumber();
        } else {
            // 重头开始
            lastNumber = serialNumberEntity.getInitNumber();
        }

        //批量生成
        List<String> list = new ArrayList<>();
        long stepValue = lastNumber;
        boolean isNeedRandom = serialNumberEntity.getStepRandomRange() > 1;
        boolean existPrefix = StringUtils.isNotBlank(serialNumberEntity.getFormat());
        for (int i = 0; i < count; i++) {
            // 需要随机
            if (isNeedRandom) {
                //随机长度
                int randomLength = SmartRandomUtil.nextInt(1, serialNumberEntity.getStepRandomRange());
                stepValue = stepValue + randomLength;
            } else {
                stepValue = stepValue + 1;
            }

            // 默认 最低长度 1
            int minLength = NumberUtils.max(serialNumberEntity.getMinLength(), 1);
            // id长度补位
            String finalId = String.format("%0" + minLength + "d", stepValue);
            if (null != timeFormat) {
                finalId = timeFormat + finalId;
            }
            // 前缀
            if (existPrefix) {
                finalId = serialNumberEntity.getFormat() + finalId;
            }
            list.add(finalId);
        }
        // 保存记录
        saveRecord(serialNumberEntity.getSerialNumberId(), recordEntity, timeFormat, stepValue);
        return list;
    }


    protected void saveRecord(Integer idGeneratorId, SerialNumberRecordEntity recordEntity, String time, long lastNumber) {
        if (recordEntity == null) {
            insertRecord(idGeneratorId, time, lastNumber);
        } else {
            updateRecord(recordEntity, time, lastNumber);
        }
    }

    protected void insertRecord(Integer serialNumberId, String time, long lastNumber) {
        SerialNumberRecordEntity recordEntity = new SerialNumberRecordEntity();
        recordEntity.setSerialNumberId(serialNumberId);
        recordEntity.setLastTime(time);
        recordEntity.setLastNumber(lastNumber);
        recordEntity.setCount(1L);
        serialNumberRecordDao.insert(recordEntity);
    }

    protected void updateRecord(SerialNumberRecordEntity updateRecordEntity, String time, long lastNumber) {
        updateRecordEntity.setTime(time);
        updateRecordEntity.setLastNumber(lastNumber);
        updateRecordEntity.setCount(updateRecordEntity.getCount() + 1);
        serialNumberRecordDao.updateById(updateRecordEntity);
    }

}
