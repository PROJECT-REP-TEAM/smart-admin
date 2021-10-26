package net.lab1024.smartadmin.service.module.support.datatracer.anno;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

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
public @interface FieldSqlValue {

    /**
     * 关联字段名称
     * @return
     */
    String relateColumn() default "id";

    /**
     * 关联显示的字段
     * @return
     */
    String relateDisplayColumn() default "";
    /**
     * 是否关联字段查询Mapper
     * @return
     */
    Class<? extends BaseMapper> relateMapper() default BaseMapper.class;

}
