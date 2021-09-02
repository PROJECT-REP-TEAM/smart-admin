package net.lab1024.smartadmin.service.module.support.redismq;


import net.lab1024.smartadmin.service.common.constant.BaseEnum;

/**
 * [  ]
 *
 * @author 罗伊
 * @date 2021/5/10 11:03
 */
public enum RedisMsgTypeEnum implements BaseEnum {

    CACHE_CLEAR(1,"清除缓存"),

    CACHE_KEY_CLEAR(2,"清除缓存key")
    ;

    private Integer type;

    private String desc;

    RedisMsgTypeEnum(Integer type, String desc) {
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