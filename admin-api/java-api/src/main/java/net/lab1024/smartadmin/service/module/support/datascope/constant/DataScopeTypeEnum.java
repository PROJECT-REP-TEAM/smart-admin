package net.lab1024.smartadmin.service.module.support.datascope.constant;


import net.lab1024.smartadmin.service.common.enumeration.BaseEnum;

/**
 * [  ]
 *
 * @author yandanyang
 * @version 1.0
 * @company 1024lab.net
 * @copyright (c) 2018 1024lab.netInc. All rights reserved.
 * @date 2019/4/28 0028 下午 15:37
 * @since JDK1.8
 */
public enum DataScopeTypeEnum implements BaseEnum {

    CLUE_USER(1, 1, "线索", "线索数据范围"),
    CLUE_USER_TRACK(2, 2, "线索跟进", "线索跟进记录"),
    CLUE_PUBLIC_USER(3, 3, "公海线索", "公海线索数据范围"),
    CLUE_INVALID_USER(4, 4, "无效线索", "无效线索数据范围"),

    CONTRACT(11, 5, "合同", "合同数据范围"),

    ORDER(21, 10, "订单", "订单数据范围"),
    RECEIVE_ORDER(22, 11, "收款单", "收款单数据范围"),
    REFUND_ORDER(23, 12, "退款单", "退款单数据范围"),

    EMPLOYEE_SALES_RANK(31, 15, "员工业绩排行", "员工业绩排行数据范围"),

    NOTICE(41, 20, "系统通知", "系统通知数据范围"),
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
