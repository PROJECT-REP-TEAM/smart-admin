package net.lab1024.smartadmin.service.module.system.datascope;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.lab1024.smartadmin.service.common.swagger.SwaggerTagConst;
import net.lab1024.smartadmin.service.common.controller.AdminBaseController;
import net.lab1024.smartadmin.service.common.domain.ResponseDTO;
import net.lab1024.smartadmin.service.module.system.datascope.domain.dto.DataScopeAndViewTypeVO;
import net.lab1024.smartadmin.service.module.system.datascope.domain.dto.DataScopeBatchSetRoleDTO;
import net.lab1024.smartadmin.service.module.system.datascope.domain.dto.DataScopeSelectVO;
import net.lab1024.smartadmin.service.module.system.datascope.service.DataScopeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * [  ]
 *
 * @author 罗伊
 */
@Api(tags = {SwaggerTagConst.Admin.MANAGER_ROLE})
@RestController
public class DataScopeController extends AdminBaseController {

    @Autowired
    private DataScopeService dataScopeService;

    @ApiOperation(value = "获取当前系统所配置的所有数据范围")
    @GetMapping("/dataScope/list")
    public ResponseDTO<List<DataScopeAndViewTypeVO>> dataScopeList() {
        return dataScopeService.dataScopeList();
    }

    @ApiOperation(value = "获取某角色所设置的数据范围")
    @GetMapping("/dataScope/listByRole/{roleId}")
    public ResponseDTO<List<DataScopeSelectVO>> dataScopeListByRole(@PathVariable Long roleId) {
        return dataScopeService.dataScopeListByRole(roleId);
    }

    @ApiOperation(value = "批量设置某角色数据范围")
    @PostMapping("/dataScope/batchSet")
    public ResponseDTO<String> dataScopeBatchSet(@RequestBody @Valid DataScopeBatchSetRoleDTO batchSetRoleDTO) {
        return dataScopeService.dataScopeBatchSet(batchSetRoleDTO);
    }


}
