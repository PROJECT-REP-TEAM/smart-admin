package net.lab1024.smartadmin.service.module.support.datatracer.constant;


import lombok.AllArgsConstructor;
import lombok.Getter;
import net.lab1024.smartadmin.service.common.enumeration.BaseEnum;

/**
 * [ 数据业务类型 ]
 *
 * @author 罗伊
 * @date 2020/8/11 15:56
 */
@AllArgsConstructor
@Getter
public enum DataTracerBusinessTypeEnum implements BaseEnum {

    NOTICE(1, "系统通知"),

    ;

    private final Integer value;

    private final String desc;
}
