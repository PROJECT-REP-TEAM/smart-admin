package net.lab1024.smartadmin.service.module.support.datascope.strategy;

import net.lab1024.smartadmin.service.module.support.datascope.constant.DataScopeViewTypeEnum;
import net.lab1024.smartadmin.service.module.support.datascope.domain.dto.DataScopeSqlConfigDTO;

import java.util.Map;

/**
 * [ 数据范围策略 ,使用DataScopeWhereInTypeEnum.CUSTOM_STRATEGY类型，DataScope注解的joinSql属性无用]
 *
 * @author yandanyang
 * @version 1.0
 * @company 1024lab.net
 * @copyright (c) 2018 1024lab.netInc. All rights reserved.
 * @date 2020/11/28 0008 下午 16:00
 * @since JDK1.8
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
