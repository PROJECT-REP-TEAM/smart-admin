package net.lab1024.smartadmin.service.module.system.menu.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.lab1024.smartadmin.service.common.controller.SystemBaseController;
import net.lab1024.smartadmin.service.common.util.SmartRequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import net.lab1024.smartadmin.service.constant.SwaggerTagConst;
import net.lab1024.smartadmin.service.common.domain.ResponseDTO;
import net.lab1024.smartadmin.service.module.system.menu.domain.form.MenuAddForm;
import net.lab1024.smartadmin.service.module.system.menu.domain.form.MenuUpdateForm;
import net.lab1024.smartadmin.service.module.system.menu.domain.vo.MenuTreeVO;
import net.lab1024.smartadmin.service.module.system.menu.domain.vo.MenuVO;
import net.lab1024.smartadmin.service.module.system.menu.domain.vo.MenuUrlVO;
import net.lab1024.smartadmin.service.module.system.menu.service.MenuService;

import javax.validation.Valid;
import java.util.List;

/**
 * 菜单
 *
 * @author zhuoda
 */
@Api(tags = {SwaggerTagConst.System.SYSTEM_MENU})
@RestController
public class MenuController extends SystemBaseController {

    @Autowired
    private MenuService menuService;

    @ApiOperation(value = "添加菜单 @author zhuoda")
    @PostMapping("/menu/add")
    public ResponseDTO<String> addMenu(@RequestBody @Valid MenuAddForm menuAddForm) {
        menuAddForm.setCreateUserId(SmartRequestUtil.getRequestEmployeeId());
        return menuService.addMenu(menuAddForm);
    }

    @ApiOperation(value = "更新菜单 @author zhuoda")
    @PostMapping("/menu/update")
    public ResponseDTO<String> updateMenu(@RequestBody @Valid MenuUpdateForm menuUpdateForm) {
        menuUpdateForm.setUpdateUserId(SmartRequestUtil.getRequestEmployeeId());
        return menuService.updateMenu(menuUpdateForm);
    }

    @ApiOperation(value = "批量删除菜单 @author zhuoda")
    @GetMapping("/menu/batchDelete")
    public ResponseDTO<String> batchDeleteMenu(@RequestParam("menuIdList") List<Long> menuIdList) {
        return menuService.batchDeleteMenu(menuIdList, SmartRequestUtil.getRequestEmployeeId());
    }

    @ApiOperation(value = "查询菜单列表 @author zhuoda")
    @GetMapping("/menu/query")
    public ResponseDTO<List<MenuVO>> queryMenuList() {
        return ResponseDTO.ok(menuService.queryMenuList(null));
    }

    @ApiOperation(value = "查询菜单详情 @author zhuoda")
    @GetMapping("/menu/detail/{menuId}")
    public ResponseDTO<MenuVO> getMenuDetail(@PathVariable Long menuId) {
        return menuService.getMenuDetail(menuId);
    }

    @ApiOperation(value = "查询菜单树 @author zhuoda")
    @GetMapping("/menu/tree")
    public ResponseDTO<List<MenuTreeVO>> queryMenuTree(@RequestParam("onlyMenu") Boolean onlyMenu) {
        return menuService.queryMenuTree(onlyMenu);
    }

    @ApiOperation(value = "获取所有请求路径 @author zhuoda")
    @GetMapping("/menu/getAllUrl")
    public ResponseDTO<List<MenuUrlVO>> getAllUrl() {
        return menuService.getAllUrlList();
    }
}
