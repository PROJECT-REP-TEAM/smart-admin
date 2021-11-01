package net.lab1024.smartadmin.service.module.support.datascope.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * [ 数据范围与角色关系 ]
 *
 * @author yandanyang
 * @version 1.0
 * @company 1024lab.net
 * @copyright (c) 2018 1024lab.netInc. All rights reserved.
 * @date 2019/4/27 0027 下午 14:43
 * @since JDK1.8
 */
@Data
@TableName("t_role_data_scope")
public class DataScopeRoleEntity {
    /**
     * 主键id
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 数据范围id
     * {@link net.lab1024.smartadmin.service.module.support.datascope.constant.DataScopeTypeEnum}
     */
    private Integer dataScopeType;
    /**
     * 数据范围类型
     * {@link net.lab1024.smartadmin.service.module.support.datascope.constant.DataScopeViewTypeEnum}
     */
    private Integer viewType;
    /**
     * 角色id
     */
    private Long roleId;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
