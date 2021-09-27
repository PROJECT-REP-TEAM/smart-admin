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
public enum DataScopeTypeEnum implements BaseEnum {

    NOTICE(7, 7, "系统通知", "系统通知数据范围"),
    ;

    private final Integer value;

    private final Integer sort;

    private final String name;

    private final String desc;
}
