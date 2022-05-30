package net.lab1024.smartadmin.module.system.role.domain.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.smartadmin.common.domain.PageParam;

/**
 * [  ]
 *
 * @author 罗伊
 * @date
 */
@Data
public class RoleEmployeeQueryForm extends PageParam {

    @ApiModelProperty("关键字")
    private String keywords;

    @ApiModelProperty("角色id")
    private String roleId;
}
