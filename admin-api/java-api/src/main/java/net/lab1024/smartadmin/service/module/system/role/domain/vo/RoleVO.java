package net.lab1024.smartadmin.service.module.system.role.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * [  ]
 *
 * @author yandanyang
 * @version 1.0
 * @company 1024lab.net
 * @copyright (c) 2018 1024lab.netInc. All rights reserved.
 * @date 2019/3/27 0027 下午 15:27
 * @since JDK1.8
 */
@Data
public class RoleVO {

    @ApiModelProperty("角色ID")
    private Long roleId;

    @ApiModelProperty("角色名称")
    private String roleName;

    @ApiModelProperty("角色备注")
    private String remark;
}
