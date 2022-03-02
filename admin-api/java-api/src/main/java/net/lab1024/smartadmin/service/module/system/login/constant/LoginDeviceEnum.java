package net.lab1024.smartadmin.service.module.system.login.constant;

import net.lab1024.smartadmin.service.common.enumeration.BaseEnum;

/**
 * @author zhuoda
 * @Date 2022-03-01
 */
public enum LoginDeviceEnum implements BaseEnum {

    PC(1, "电脑端"),

    ANDROID(2, "安卓"),

    APPLE(3, "苹果");

    LoginDeviceEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    private Integer value;
    private String desc;

    @Override
    public Integer getValue() {
        return value;
    }

    @Override
    public String getDesc() {
        return desc;
    }
}
