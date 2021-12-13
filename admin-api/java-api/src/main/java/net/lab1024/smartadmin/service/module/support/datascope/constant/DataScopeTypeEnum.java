package net.lab1024.smartadmin.service.module.support.datascope.constant;


import net.lab1024.smartadmin.service.common.enumeration.BaseEnum;

/**
 * [  ]
 *
 * @author 罗伊
 * @date 2019/4/28 0028 下午 15:37
 */
public enum DataScopeTypeEnum implements BaseEnum {


    NOTICE(1, 20, "系统通知", "系统通知数据范围"),
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
