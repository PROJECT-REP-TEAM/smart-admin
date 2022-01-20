package net.lab1024.smartadmin.service.module.support.serialnumber.domain;

import lombok.*;
import net.lab1024.smartadmin.service.module.support.serialnumber.constant.SerialNumberRuleTypeEnum;

import java.time.LocalDateTime;

/**
 * @author zhuoda
 * @Date 2022-01-11
 */
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SerialNumberBO {

    private Integer serialNumberId;

    private Long lastNumber;

    private LocalDateTime lastTime;

    private Long initNumber;

    /**
     * 步长随机数范围
     */
    private Integer stepRandomRange;

    private SerialNumberRuleTypeEnum serialNumberRuleTypeEnum;

    private Integer numberCount;

    private Boolean haveYearFlag;

    private Boolean haveMonthFlag;

    private Boolean haveDayFlag;

    /**
     * 记录id
     */
    private Long serialNumberRecordId;

    public void setSerialNumberRecordId(Long serialNumberRecordId) {
        this.serialNumberRecordId = serialNumberRecordId;
    }

    public void setLastNumber(Long lastNumber) {
        this.lastNumber = lastNumber;
    }

    public void setLastTime(LocalDateTime lastTime) {
        this.lastTime = lastTime;
    }
}
