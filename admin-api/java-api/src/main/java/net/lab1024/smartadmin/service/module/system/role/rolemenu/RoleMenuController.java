package net.lab1024.smartadmin.service.module.system.role.rolemenu;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.lab1024.smartadmin.service.common.swagger.SwaggerTagConst;
import net.lab1024.smartadmin.service.common.controller.AdminBaseController;
import net.lab1024.smartadmin.service.common.domain.ResponseDTO;
import net.lab1024.smartadmin.service.module.system.role.rolemenu.domain.RoleMenuDTO;
import net.lab1024.smartadmin.service.module.system.role.rolemenu.domain.RoleMenuTreeVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 角色-菜单
 *
 * @author 善逸
 * @date 2021/7/30 17:06
 */
@RestController
@Api(tags = {SwaggerTagConst.Admin.MANAGER_ROLE_MENU})
public class RoleMenuController extends AdminBaseController {

    @Autowired
    private RoleMenuService roleMenuService;

    @ApiOperation("更新角色权限")
    @PostMapping("/role/menu/updateRoleMenu")
    public ResponseDTO<String> updateRoleMenu(@Valid @RequestBody RoleMenuDTO updateDTO) {
        return roleMenuService.updateRoleMenu(updateDTO);
    }

    @ApiOperation("获取角色关联菜单权限")
    @GetMapping("/role/menu/getRoleSelectedMenu/{roleId}")
    public ResponseDTO<RoleMenuTreeVO> getRoleSelectedMenu(@PathVariable Long roleId) {
        return roleMenuService.getRoleSelectedMenu(roleId);
    }
}
