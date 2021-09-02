package net.lab1024.smartadmin.service.module.system.role.basic.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.smartadmin.service.common.domain.PageBaseDTO;

/**
 * [  ]
 *
 * @author 罗伊
 */
@Data
public class RoleQueryDTO extends PageBaseDTO {

    @ApiModelProperty("角色名称")
    private String roleName;

    @ApiModelProperty("角色id")
    private String roleId;
}
