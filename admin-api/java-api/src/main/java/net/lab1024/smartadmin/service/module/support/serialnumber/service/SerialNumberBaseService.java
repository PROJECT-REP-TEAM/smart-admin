package net.lab1024.smartadmin.service.module.support.serialnumber.service;

import com.google.common.collect.Lists;
import net.lab1024.smartadmin.service.common.exception.BusinessException;
import net.lab1024.smartadmin.service.common.util.SmartBaseEnumUtil;
import net.lab1024.smartadmin.service.module.support.serialnumber.constant.SerialNumberIdEnum;
import net.lab1024.smartadmin.service.module.support.serialnumber.constant.SerialNumberRuleTypeEnum;
import net.lab1024.smartadmin.service.module.support.serialnumber.dao.SerialNumberDao;
import net.lab1024.smartadmin.service.module.support.serialnumber.dao.SerialNumberRecordDao;
import net.lab1024.smartadmin.service.module.support.serialnumber.domain.*;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @author zhuoda
 * @Date 2021-11-10
 */
public abstract class SerialNumberBaseService implements SerialNumberService {

    @Autowired
    protected SerialNumberRecordDao serialNumberRecordDao;

    @Autowired
    protected SerialNumberDao serialNumberDao;

    private ConcurrentHashMap<Integer, SerialNumberInfoBO> serialNumberMap = new ConcurrentHashMap<>();

    abstract List<String> generateSerialNumberList(SerialNumberInfoBO serialNumber, int count);

    @PostConstruct
    void init() {
        List<SerialNumberEntity> serialNumberEntityList = serialNumberDao.selectList(null);
        if (serialNumberEntityList == null) {
            return;
        }
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

            String numberFormat = format.substring(startIndex, endIndex + 2);

            if (serialNumberEntity.getStepRandomRange() < 1) {
                throw new ExceptionInInitializerError("random step range must greater than 1 " + serialNumberEntity.getSerialNumberId());
            }

            SerialNumberInfoBO serialNumberInfoBO = SerialNumberInfoBO.builder()
                    .serialNumberRuleTypeEnum(ruleTypeEnum)
                    .haveDayFlag(format.contains(SerialNumberRuleTypeEnum.DAY.getValue()))
                    .haveMonthFlag(format.contains(SerialNumberRuleTypeEnum.MONTH.getValue()))
                    .haveYearFlag(format.contains(SerialNumberRuleTypeEnum.YEAR.getValue()))
                    .numberCount(endIndex - startIndex)
                    .numberFormat(numberFormat)
                    .build();

            this.serialNumberMap.put(serialNumberEntity.getSerialNumberId(), serialNumberInfoBO);
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
        SerialNumberInfoBO serialNumberInfoBO = serialNumberMap.get(serialNumberIdEnum.getSerialNumberId());
        if (serialNumberInfoBO == null) {
            throw new BusinessException("cannot found SerialNumberId : " + serialNumberIdEnum.toString());
        }
        return this.generateSerialNumberList(serialNumberInfoBO, count);
    }

    /**
     * 循环生成 number 集合
     *
     * @param lastGenerateBO
     * @param serialNumberInfoBO
     * @param count
     * @return
     */
    protected SerialNumberGenerateResultBO loopNumberList(SerialNumberLastGenerateBO lastGenerateBO, SerialNumberInfoBO serialNumberInfoBO, int count) {
        long lastNumber = lastGenerateBO.getLastNumber();
        boolean isReset = false;
        if (isResetInitNumber(lastGenerateBO, serialNumberInfoBO)) {
            lastNumber = serialNumberInfoBO.getInitNumber();
            isReset = true;
        }

        ArrayList<Long> numberList = Lists.newArrayListWithCapacity(count);
        for (int i = 0; i < count; i++) {
            Integer stepRandomRange = serialNumberInfoBO.getStepRandomRange();
            if (stepRandomRange > 1) {
                lastNumber = lastNumber + RandomUtils.nextInt(1, stepRandomRange + 1);
            } else {
                lastNumber = lastNumber + 1;
            }

            numberList.add(lastNumber);
        }

        return SerialNumberGenerateResultBO
                .builder()
                .lastNumber(lastNumber)
                .lastTime(LocalDateTime.now())
                .numberList(numberList)
                .isReset(isReset)
                .build();
    }

    protected void saveRecord(SerialNumberGenerateResultBO resultBO) {
        Long effectRows = serialNumberRecordDao.updateRecord(resultBO.getSerialNumberId(),
                resultBO.getLastTime().toLocalDate(),
                resultBO.getLastNumber(),
                resultBO.getNumberList().size()
        );

        // 需要插入
        if (effectRows == null || effectRows == 0) {
            SerialNumberRecordEntity recordEntity = SerialNumberRecordEntity.builder()
                    .serialNumberId(resultBO.getSerialNumberId())
                    .lastTime(resultBO.getLastTime())
                    .lastNumber(resultBO.getLastNumber())
                    .count((long) resultBO.getNumberList().size())
                    .build();
        }

    }

    /**
     * 若不在规则周期内，重制初始值
     *
     * @return
     */
    private boolean isResetInitNumber(SerialNumberLastGenerateBO lastGenerateBO, SerialNumberInfoBO serialNumberInfoBO) {
        SerialNumberRuleTypeEnum serialNumberRuleTypeEnum = serialNumberInfoBO.getSerialNumberRuleTypeEnum();
        int lastTimeYear = lastGenerateBO.getLastTime().getYear();
        int lastTimeMonth = lastGenerateBO.getLastTime().getMonthValue();
        int lastTimeDay = lastGenerateBO.getLastTime().getDayOfYear();

        LocalDateTime now = LocalDateTime.now();

        switch (serialNumberRuleTypeEnum) {
            case YEAR:
                return lastTimeYear != now.getYear();
            case MONTH:
                return lastTimeYear != now.getYear() || lastTimeMonth != now.getMonthValue();
            case DAY:
                return lastTimeYear != now.getYear() || lastTimeDay != now.getDayOfYear();
            default:
                return false;
        }
    }

    /**
     * 替换特殊rule，即替换[yyyy][mm][dd][nnn]等规则
     */
    protected List<String> formatNumberList(SerialNumberGenerateResultBO resultBO, SerialNumberInfoBO serialNumberInfoBO) {

        /**
         * 第一步：替换年、月、日
         */
        LocalDate lastTime = resultBO.getLastTime().toLocalDate();
        String year = String.valueOf(lastTime.getYear());
        String month = lastTime.getMonthValue() > 9 ? String.valueOf(lastTime.getMonthValue()) : "0" + lastTime.getMonthValue();
        String day = lastTime.getDayOfMonth() > 9 ? String.valueOf(lastTime.getDayOfMonth()) : "0" + lastTime.getDayOfMonth();

        // 把年月日替换
        String format = serialNumberInfoBO.getFormat();

        if (serialNumberInfoBO.getHaveYearFlag()) {
            format = format.replaceAll(SerialNumberRuleTypeEnum.YEAR.getValue(), year);
        }
        if (serialNumberInfoBO.getHaveMonthFlag()) {
            format = format.replaceAll(SerialNumberRuleTypeEnum.MONTH.getValue(), month);
        }
        if (serialNumberInfoBO.getHaveDayFlag()) {
            format = format.replaceAll(SerialNumberRuleTypeEnum.DAY.getValue(), day);
        }


        /**
         * 第二步：替换数字
         */

        List<String> numberList = Lists.newArrayListWithCapacity(resultBO.getNumberList().size());
        for (Long number : resultBO.getNumberList()) {
            StringBuilder numberStringBuilder = new StringBuilder();
            int currentNumberCount = String.valueOf(number).length();
            //数量不够，前面补0
            if (serialNumberInfoBO.getNumberCount() > currentNumberCount) {
                int remain = serialNumberInfoBO.getNumberCount() - currentNumberCount;
                for (int i = 0; i < remain; i++) {
                    numberStringBuilder.append(0);
                }
            }
            numberStringBuilder.append(number);
            //最终替换
            String finalNumber = format.replaceAll(serialNumberInfoBO.getNumberFormat(), numberStringBuilder.toString());
            numberList.add(finalNumber);
        }
        return numberList;
    }


}
