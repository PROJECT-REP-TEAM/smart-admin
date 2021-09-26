package net.lab1024.smartadmin.service.module.business.category.constant;


import net.lab1024.smartadmin.service.common.enumconst.BaseEnum;

/**
 * 分类类型 枚举
 *
 * @author listen
 * @date 2021/08/05 15:26
 */
public enum CategoryTypeEnum implements BaseEnum {

    /**
     * 1 商品
     */
    GOODS(1, "商品"),

    /**
     * 2 测试分类
     */
    DEMO(2, "测试分类"),

    ;

    private final Integer type;

    private final String desc;

    CategoryTypeEnum(Integer type, String desc) {
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
