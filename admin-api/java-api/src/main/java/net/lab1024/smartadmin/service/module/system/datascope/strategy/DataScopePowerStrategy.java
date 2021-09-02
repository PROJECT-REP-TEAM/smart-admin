package net.lab1024.smartadmin.service.module.system.datascope.strategy;

import net.lab1024.smartadmin.service.module.system.datascope.constant.DataScopeViewTypeEnum;
import net.lab1024.smartadmin.service.module.system.datascope.domain.dto.DataScopeSqlConfigDTO;

import java.util.Map;

/**
 * [ 数据范围策略 ,使用DataScopeWhereInTypeEnum.CUSTOM_STRATEGY类型，DataScope注解的joinSql属性无用]
 *
 * @author 罗伊
 */
public abstract class DataScopePowerStrategy {

    /**
     * 获取joinsql 字符串
     * @param viewTypeEnum 查看的类型
     * @param sqlConfigDTO
     * @return
     */
    public abstract String getCondition(DataScopeViewTypeEnum viewTypeEnum, Map<String, Object> paramMap, DataScopeSqlConfigDTO sqlConfigDTO);
}
