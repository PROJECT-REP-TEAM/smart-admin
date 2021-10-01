package net.lab1024.smartadmin.service.module.system.menu;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.lab1024.smartadmin.service.common.swagger.SwaggerTagConst;
import net.lab1024.smartadmin.service.common.controller.AdminBaseController;
import net.lab1024.smartadmin.service.common.domain.ResponseDTO;
import net.lab1024.smartadmin.service.module.system.menu.domain.*;
import net.lab1024.smartadmin.service.util.SmartEmployeeTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 菜单
 *
 * @author 卓大
 */
@Api(tags = {SwaggerTagConst.Admin.MANAGER_MENU})
@RestController
public class MenuController extends AdminBaseController {

    @Autowired
    private MenuService menuService;

    @ApiOperation(value = "添加菜单")
    @PostMapping("/menu/add")
    public ResponseDTO<String> addMenu(@RequestBody @Valid MenuAddForm menuAddForm) {
        menuAddForm.setCreateUserId(SmartEmployeeTokenUtil.getRequestEmployeeId());
        return menuService.addMenu(menuAddForm);
    }

    @ApiOperation(value = "更新菜单")
    @PostMapping("/menu/update")
    public ResponseDTO<String> updateMenu(@RequestBody @Valid MenuUpdateForm menuUpdateForm) {
        menuUpdateForm.setUpdateUserId(SmartEmployeeTokenUtil.getRequestEmployeeId());
        return menuService.updateMenu(menuUpdateForm);
    }

    @ApiOperation(value = "批量删除菜单")
    @GetMapping("/menu/batchDelete")
    public ResponseDTO<String> batchDeleteMenu(@RequestParam("menuIdList") List<Long> menuIdList) {
        return menuService.batchDeleteMenu(menuIdList, SmartEmployeeTokenUtil.getRequestEmployeeId());
    }

    @ApiOperation(value = "查询菜单列表")
    @GetMapping("/menu/query")
    public ResponseDTO<List<MenuVO>> queryMenuList() {
        return ResponseDTO.ok(menuService.queryMenuList(null));
    }

    @ApiOperation(value = "查询菜单详情")
    @GetMapping("/menu/detail/{menuId}")
    public ResponseDTO<MenuVO> getMenuDetail(@PathVariable Long menuId) {
        return menuService.getMenuDetail(menuId);
    }

    @ApiOperation(value = "查询菜单树")
    @GetMapping("/menu/tree")
    public ResponseDTO<List<MenuTreeVO>> queryMenuTree(@RequestParam("onlyMenu") Boolean onlyMenu) {
        return menuService.queryMenuTree(onlyMenu);
    }

    @ApiOperation(value = "获取所有请求路径", notes = "获取所有请求路径")
    @GetMapping("/menu/getAllUrl")
    public ResponseDTO<List<RequestUrlVO>> getAllUrl() {
        return menuService.getPrivilegeUrlDTOList();
    }
}
