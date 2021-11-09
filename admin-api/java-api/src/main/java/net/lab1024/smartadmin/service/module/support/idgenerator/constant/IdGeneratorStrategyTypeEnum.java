package net.lab1024.smartadmin.service.module.support.idgenerator.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.lab1024.smartadmin.service.common.enumeration.BaseEnum;

/**
 * [  ]
 *
 * @author yandanyang
 * @date 2021/11/9 18:49
 */
@AllArgsConstructor
@Getter
public enum IdGeneratorStrategyTypeEnum implements BaseEnum {

    INTERN(1, "string intern"),

    REDIS(2, "redis"),

    MYSQL_LOCK(2, "mysql排他锁"),
    ;

    private final Integer value;

    private final String desc;

}
