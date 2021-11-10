package net.lab1024.smartadmin.service.module.system.role.domain.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.smartadmin.service.common.domain.PageParam;

/**
 * [  ]
 *
 * @author yandanyang
 * @date
 */
@Data
public class RoleQueryForm extends PageParam {

    @ApiModelProperty("角色名称")
    private String roleName;

    @ApiModelProperty("角色id")
    private String roleId;
}
