package net.lab1024.smartadmin.service.module.support.datatracer.anno;

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
public @interface FieldDoc {
    /**
     * 本属性的注释信息
     * @return
     */
    String value() default "";

}
