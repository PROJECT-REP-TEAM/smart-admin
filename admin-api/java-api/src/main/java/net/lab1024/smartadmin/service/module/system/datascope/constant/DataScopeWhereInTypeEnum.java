package net.lab1024.smartadmin.service.module.system.datascope.constant;


import lombok.AllArgsConstructor;
import lombok.Getter;
import net.lab1024.smartadmin.service.common.enumconst.BaseEnum;

/**
 * [  ]
 *
 * @author 罗伊
 */
@AllArgsConstructor
@Getter
public enum DataScopeWhereInTypeEnum implements BaseEnum {

    EMPLOYEE(0, "以员工IN"),

    DEPARTMENT(1, "以部门IN"),

    CUSTOM_STRATEGY(2, "自定义策略");

    private final Integer value;

    private final String desc;
}
