package net.lab1024.smartadmin.service.module.support.idgenerator.constant;

/**
 * [  ]
 *
 * @author 罗伊
 */
public enum IdGeneratorRuleTypeEnum {
    /**
     * 没有周期
     */
    NO_CYCLE(""),
    /**
     * 年周期
     */
    YEAR_CYCLE("yyyy"),
    /**
     * 月周期
     */
    MONTH_CYCLE("yyyyMM"),
    /**
     * 日周期
     */
    DAY_CYCLE("yyyyMMdd");

    private String ext;

    IdGeneratorRuleTypeEnum(String ext) {
        this.ext = ext;
    }

    public String getExt() {
        return ext;
    }
}
