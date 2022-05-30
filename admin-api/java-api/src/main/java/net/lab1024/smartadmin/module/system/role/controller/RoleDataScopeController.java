package net.lab1024.smartadmin.module.system.role.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.lab1024.smartadmin.common.controller.SystemBaseController;
import net.lab1024.smartadmin.common.domain.ResponseDTO;
import net.lab1024.smartadmin.constant.SwaggerTagConst;
import net.lab1024.smartadmin.module.system.role.domain.form.RoleDataScopeUpdateForm;
import net.lab1024.smartadmin.module.system.role.domain.vo.RoleDataScopeVO;
import net.lab1024.smartadmin.module.system.role.service.RoleDataScopeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @author zhuoda
 */

@Api(tags = {SwaggerTagConst.System.SYSTEM_ROLE_DATA_SCOPE})
@RestController
public class RoleDataScopeController extends SystemBaseController {

    @Autowired
    private RoleDataScopeService roleDataScopeService;

    @ApiOperation(value = "获取某角色所设置的数据范围 @author zhuoda")
    @GetMapping("/role/dataScope/getRoleDataScopeList/{roleId}")
    public ResponseDTO<List<RoleDataScopeVO>> dataScopeListByRole(@PathVariable Long roleId) {
        return roleDataScopeService.getRoleDataScopeList(roleId);
    }

    @ApiOperation(value = "批量设置某角色数据范围 @author zhuoda")
    @PostMapping("/role/dataScope/updateRoleDataScopeList")
    public ResponseDTO<String> updateRoleDataScopeList(@RequestBody @Valid RoleDataScopeUpdateForm roleDataScopeUpdateForm) {
        return roleDataScopeService.updateRoleDataScopeList(roleDataScopeUpdateForm);
    }


}
