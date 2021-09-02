package net.lab1024.smartadmin.service.module.system.role.rolemenu.domain;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 角色-菜单
 *
 * @author 善逸
 * @date 2021/7/30 17:18
 */
@Data
public class RoleMenuDTO {

    /**
     * 角色id
     */
    @ApiModelProperty("角色id")
    @NotNull(message = "角色id不能为空")
    private Long roleId;

    /**
     * 菜单ID 集合
     */
    @ApiModelProperty("菜单ID集合")
    @NotNull(message = "菜单ID不能为空")
    private List<Long> menuIdList;

}
