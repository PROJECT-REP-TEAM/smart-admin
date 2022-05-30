package net.lab1024.smartadmin.module.support.datascope;


import net.lab1024.smartadmin.module.support.datascope.constant.DataScopeTypeEnum;
import net.lab1024.smartadmin.module.support.datascope.constant.DataScopeWhereInTypeEnum;
import net.lab1024.smartadmin.module.support.datascope.strategy.DataScopePowerStrategy;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * [ 数据范围 ]
 *
 * @author 罗伊
 * @date
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface DataScope {

    DataScopeTypeEnum dataScopeType() ;

    DataScopeWhereInTypeEnum whereInType() default DataScopeWhereInTypeEnum.EMPLOYEE;

    /**
     * DataScopeWhereInTypeEnum.CUSTOM_STRATEGY类型 才可使用joinSqlImplClazz属性
     * @return
     */
    Class<? extends DataScopePowerStrategy> joinSqlImplClazz()  default DataScopePowerStrategy.class;

    /**
     * 多个参数已逗号分隔，本属性主要用于joinSqlImplClazz 实现类跟进参数进行不同的范围控制，如不使用CUSTOM_STRATEGY，可不做配置
     * @return
     */
    String paramName() default "";
    /**
     *
     * 第几个where 条件 从0开始
     * @return
     */
    int whereIndex() default 0;

    /**
     * DataScopeWhereInTypeEnum为CUSTOM_STRATEGY类型时，此属性无效
     * @return
     */
    String joinSql() default "";

}
