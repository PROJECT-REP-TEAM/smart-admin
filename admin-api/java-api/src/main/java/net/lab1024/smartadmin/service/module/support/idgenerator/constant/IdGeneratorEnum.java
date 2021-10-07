package net.lab1024.smartadmin.service.module.support.idgenerator.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.lab1024.smartadmin.service.common.enumeration.BaseEnum;

/**
 * id生成枚举类
 *
 * @author Administrator
 */
@AllArgsConstructor
@Getter
public enum IdGeneratorEnum implements BaseEnum {


    ;

    private final Integer value;

    private final String desc;

    @Override
    public String toString() {
        return "IdGeneratorEnum{" + "id=" + value + ", keyName='" + desc + '\'' + '}';
    }
}
