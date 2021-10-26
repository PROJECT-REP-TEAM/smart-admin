package net.lab1024.smartadmin.service.module.support.datatracer.anno;

import net.lab1024.smartadmin.service.common.enumeration.BaseEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * [  ]
 *
 * @author 罗伊
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface FieldEnumValue {

    Class<? extends BaseEnum> enumClass() default BaseEnum.class;

}
