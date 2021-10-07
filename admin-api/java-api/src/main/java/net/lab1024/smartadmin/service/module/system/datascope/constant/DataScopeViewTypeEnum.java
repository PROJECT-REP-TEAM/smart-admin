package net.lab1024.smartadmin.service.module.system.datascope.constant;


import lombok.AllArgsConstructor;
import lombok.Getter;
import net.lab1024.smartadmin.service.common.enumeration.BaseEnum;

/**
 * [  ]
 *
 * @author 罗伊
 */
@AllArgsConstructor
@Getter
public enum DataScopeViewTypeEnum implements BaseEnum {

    ME(0, 0, "本人"),

    DEPARTMENT(1, 5, "本部门"),

    DEPARTMENT_AND_SUB(2, 10, "本部门及下属子部门"),

    ALL(3, 15, "全部");

    private final Integer value;

    private final Integer level;

    private final String desc;
}
