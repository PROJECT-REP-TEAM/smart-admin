package net.lab1024.smartadmin.service.module.support.datatracer.constant;


import net.lab1024.smartadmin.service.common.enumconst.BaseEnum;

/**
 * [ 数据业务类型 ]
 *
 * @author 罗伊
 * @date 2020/8/11 15:56
 */
public enum DataTracerBusinessTypeEnum implements BaseEnum {

    NOTICE(1, "系统通知"),

    ;

    private Integer value;

    private String desc;

    DataTracerBusinessTypeEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    @Override
    public Integer getValue() {
        return value;
    }

    @Override
    public String getDesc() {
        return desc;
    }

}
