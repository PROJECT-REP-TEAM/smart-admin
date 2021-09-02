package net.lab1024.smartadmin.service.module.support.redismq;

import net.lab1024.smartadmin.service.common.constant.BaseEnum;

/**
 * [  ]
 *
 * @author 罗伊
 * @date 2021/5/10 11:44
 */
public enum RedisMqTopicEnum implements BaseEnum {


    SMART_ADMIN("smartAdmin","主题"),

    ;

    private String type;

    private String desc;

    RedisMqTopicEnum(String type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    /**
     * 获取枚举类的值
     *
     * @return Integer
     */
    @Override
    public String getValue() {
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