package net.lab1024.smartadmin.service.module.support.beanrecord;

/**
 * [  ]
 *
 * @author 罗伊
 */
public enum  OperateTypeEnum {

    ADD(1, "新增"),

    UPDATE(2, "修改"),

    DELETE(3, "删除");

    private Integer value;

    private String desc;

    OperateTypeEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public Integer getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }
}
