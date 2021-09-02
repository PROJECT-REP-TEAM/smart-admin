package net.lab1024.smartadmin.service.module.system.datascope.domain.dto;

import lombok.Data;
import net.lab1024.smartadmin.service.module.system.datascope.constant.DataScopeTypeEnum;
import net.lab1024.smartadmin.service.module.system.datascope.constant.DataScopeWhereInTypeEnum;

/**
 * [  ]
 *
 * @author 罗伊
 */
@Data
public class DataScopeSqlConfigDTO {

    /**
     * 数据范围类型
     * {@link DataScopeTypeEnum}
     */
    private DataScopeTypeEnum dataScopeType;

    /**
     * join sql 具体实现类
     */
    private Class joinSqlImplClazz;

    private String joinSql;

    private Integer whereIndex;

    private String paramName;

    /**
     * whereIn类型
     * {@link DataScopeWhereInTypeEnum}
     */
    private DataScopeWhereInTypeEnum dataScopeWhereInType;
}
