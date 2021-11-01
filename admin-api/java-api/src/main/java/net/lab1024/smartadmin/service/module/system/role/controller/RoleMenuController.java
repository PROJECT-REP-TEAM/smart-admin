package net.lab1024.smartadmin.service.module.system.role.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import net.lab1024.smartadmin.service.common.controller.AdminBaseController;
import net.lab1024.smartadmin.service.constant.SwaggerTagConst;
import net.lab1024.smartadmin.service.common.domain.ResponseDTO;
import net.lab1024.smartadmin.service.module.system.role.service.RoleMenuService;
import net.lab1024.smartadmin.service.module.system.role.domain.form.RoleMenuUpdateForm;
import net.lab1024.smartadmin.service.module.system.role.domain.vo.RoleMenuTreeVO;

import javax.validation.Valid;

/**
 * 角色-菜单
 *
 * @author lihaifan
 * @date 2021/7/30 17:06
 */
@RestController
@Api(tags = {SwaggerTagConst.System.MANAGER_ROLE_MENU})
public class RoleMenuController extends AdminBaseController {

    @Autowired
    private RoleMenuService roleMenuService;

    @ApiOperation("更新角色权限 @author zhuoda")
    @PostMapping("/role/menu/updateRoleMenu")
    public ResponseDTO<String> updateRoleMenu(@Valid @RequestBody RoleMenuUpdateForm updateDTO) {
        return roleMenuService.updateRoleMenu(updateDTO);
    }

    @ApiOperation("获取角色关联菜单权限 @author zhuoda")
    @GetMapping("/role/menu/getRoleSelectedMenu/{roleId}")
    public ResponseDTO<RoleMenuTreeVO> getRoleSelectedMenu(@PathVariable Long roleId) {
        return roleMenuService.getRoleSelectedMenu(roleId);
    }
}
