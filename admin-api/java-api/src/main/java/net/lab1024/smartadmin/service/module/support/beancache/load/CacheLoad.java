package net.lab1024.smartadmin.service.module.support.beancache.load;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * [  ]
 *
 * @author 罗伊
 * @date 2020/9/6 15:53
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CacheLoad {

    String value();

    /**
     * 过期时间 默认不过期依据总体过期时间定义
     * @return
     */
    long expireSecond() default 0L;

}
