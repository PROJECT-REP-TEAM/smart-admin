package net.lab1024.smartadmin.service.module.system.login.constant;

import net.lab1024.smartadmin.service.common.enumeration.BaseEnum;

/**
 * @author zhuoda
 * @Date 2022-03-01
 */
public enum LoginDeviceEnum implements BaseEnum {

    PC("PC", "电脑端"),
    
    APP("App", "移动端");

    LoginDeviceEnum(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    private String value;
    private String desc;

    @Override
    public Object getValue() {
        return value;
    }

    @Override
    public String getDesc() {
        return desc;
    }
}
