package net.lab1024.smartadmin.service.module.business.goods.constant;


import net.lab1024.smartadmin.service.common.enumconst.BaseEnum;

/**
 * 商品类型 枚举
 *
 * @author listen
 * @date 2021/08/05 15:26
 */
public enum GoodsTypeEnum implements BaseEnum {

    /**
     * 1 图书
     */
    BOOK(1, "图书"),

    /**
     * 2 课程
     */
    COURSE(2, "课程"),

    ;

    private final Integer type;

    private final String desc;

    GoodsTypeEnum(Integer type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    /**
     * 获取枚举类的值
     *
     * @return Integer
     */
    @Override
    public Integer getValue() {
        return type;
    }

    /**
     * 获取枚举类的说明
     *
     * @return String
     */
    @Override
    public String getDesc() {
        return desc;
    }
}
