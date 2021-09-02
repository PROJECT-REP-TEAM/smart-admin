package net.lab1024.smartadmin.service.common.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标记 需要防止重复提交 的注解
 *
 * @author listen
 * @date 2020年11月25日 10:56:58
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface NoRepeatSubmit {

    /**
     * 重复提交间隔时间/毫秒
     *
     * @return
     */
    int value() default 1200;

    /**
     * 最长间隔30s
     */
    int MAX_INTERVAL = 30000;
}
