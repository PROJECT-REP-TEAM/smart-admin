package net.lab1024.smartadmin.service.module.system.datascope.constant;


import net.lab1024.smartadmin.service.common.enumconst.BaseEnum;

/**
 * [  ]
 *
 * @author 罗伊
 */
public enum DataScopeTypeEnum implements BaseEnum {

    NOTICE(7, 7, "系统通知", "系统通知数据范围"),
    ;

    private Integer value;

    private Integer sort;

    private String name;

    private String desc;

    DataScopeTypeEnum(Integer value, Integer sort, String name, String desc) {
        this.value = value;
        this.sort = sort;
        this.name = name;
        this.desc = desc;
    }

    @Override
    public Integer getValue() {
        return value;
    }

    public Integer getSort() {
        return sort;
    }

    @Override
    public String getDesc() {
        return desc;
    }

    public String getName() {
        return name;
    }


}
