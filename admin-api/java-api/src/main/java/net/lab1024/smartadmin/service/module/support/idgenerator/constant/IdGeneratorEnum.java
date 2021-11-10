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

    ORDER(1, "订单id"),

    CONTRACT(2, "合同id"),

    ;

    private final Integer idGeneratorId;

    private final String desc;

    @Override
    public Integer getValue() {
        return idGeneratorId;
    }

    @Override
    public String toString() {
        return "IdGeneratorEnum{" +
                "idGeneratorId=" + idGeneratorId +
                ", desc='" + desc + '\'' +
                '}';
    }
}
