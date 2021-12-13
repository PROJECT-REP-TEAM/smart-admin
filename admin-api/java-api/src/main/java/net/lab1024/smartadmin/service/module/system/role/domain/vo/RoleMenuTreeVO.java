package net.lab1024.smartadmin.service.module.system.role.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.smartadmin.service.module.system.menu.domain.vo.MenuSimpleTreeVO;

import java.util.List;

/**
 * 角色菜单树
 *
 * @author 李善逸
 * @date 2021/7/30 17:36
 */
@Data
public class RoleMenuTreeVO {

    @ApiModelProperty("角色ID")
    private Long roleId;

    @ApiModelProperty("菜单列表")
    private List<MenuSimpleTreeVO> menuTreeList;

    @ApiModelProperty("选中的菜单ID")
    private List<Long> selectedMenuId;
}
