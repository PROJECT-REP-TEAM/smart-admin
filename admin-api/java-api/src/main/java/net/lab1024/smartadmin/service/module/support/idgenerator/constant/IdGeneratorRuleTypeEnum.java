package net.lab1024.smartadmin.service.module.support.idgenerator.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.lab1024.smartadmin.service.common.enumconst.BaseEnum;

/**
 * [  ]
 *
 * @author 罗伊
 */
@AllArgsConstructor
@Getter
public enum IdGeneratorRuleTypeEnum implements BaseEnum {
    /**
     * 没有周期
     */
    NO_CYCLE("", "没有周期"),
    /**
     * 年周期
     */
    YEAR_CYCLE("yyyy", "年"),
    /**
     * 月周期
     */
    MONTH_CYCLE("yyyyMM", "年月"),
    /**
     * 日周期
     */
    DAY_CYCLE("yyyyMMdd", "年月日");

    private final String value;

    private final String desc;

}
