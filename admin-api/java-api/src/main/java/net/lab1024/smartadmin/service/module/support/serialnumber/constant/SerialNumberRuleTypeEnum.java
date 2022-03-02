package net.lab1024.smartadmin.service.module.support.serialnumber.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.lab1024.smartadmin.service.common.constant.StringConst;
import net.lab1024.smartadmin.service.common.enumeration.BaseEnum;
import net.lab1024.smartadmin.service.common.util.date.SmartDateFormatterEnum;
import net.lab1024.smartadmin.service.common.util.date.SmartLocalDateUtil;

/**
 * [  ]
 *
 * @author 罗伊
 */
@AllArgsConstructor
@Getter
public enum SerialNumberRuleTypeEnum implements BaseEnum {
    /**
     * 没有周期
     */
    NONE(StringConst.EMPTY_STR, "没有周期", null),
    /**
     * 年周期
     */
    YEAR("[yyyy]", "年", SmartDateFormatterEnum.Y),
    /**
     * 月周期
     */
    MONTH("[mm]", "年月", SmartDateFormatterEnum.YM),
    /**
     * 日周期
     */
    DAY("[dd]", "年月日", SmartDateFormatterEnum.YMD);

    private final String value;

    private final String desc;

    private final SmartDateFormatterEnum dateFormatterEnum;

}
