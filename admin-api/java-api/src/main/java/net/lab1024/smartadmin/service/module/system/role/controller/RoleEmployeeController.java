package net.lab1024.smartadmin.service.module.system.role.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import net.lab1024.smartadmin.service.common.controller.SystemBaseController;
import net.lab1024.smartadmin.service.common.domain.PageResult;
import net.lab1024.smartadmin.service.common.domain.ResponseDTO;
import net.lab1024.smartadmin.service.constant.SwaggerTagConst;
import net.lab1024.smartadmin.service.module.system.employee.domain.vo.EmployeeVO;
import net.lab1024.smartadmin.service.module.system.role.domain.form.RoleEmployeeQueryForm;
import net.lab1024.smartadmin.service.module.system.role.domain.form.RoleEmployeeUpdateForm;
import net.lab1024.smartadmin.service.module.system.role.domain.vo.RoleSelectedVO;
import net.lab1024.smartadmin.service.module.system.role.service.RoleEmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 用户角色管理路由
 *
 * @author 胡克
 */
@Api(tags = {SwaggerTagConst.System.SYSTEM_ROLE_EMPLOYEE})
@RestController
public class RoleEmployeeController extends SystemBaseController {

    @Autowired
    private RoleEmployeeService roleEmployeeService;

    @ApiOperation(value = "查询某个角色下的员工列表  @author zhuoda")
    @PostMapping("/role/employee/queryEmployee")
    public ResponseDTO<PageResult<EmployeeVO>> queryEmployee(@Valid @RequestBody RoleEmployeeQueryForm roleEmployeeQueryForm) {
        return roleEmployeeService.queryEmployee(roleEmployeeQueryForm);
    }

    @ApiOperation(value = "获取某个角色下的所有员工列表(无分页)  @author zhuoda")
    @GetMapping("/role/employee/getAllEmployeeByRoleId/{roleId}")
    public ResponseDTO<List<EmployeeVO>> listAllEmployeeRoleId(@PathVariable Long roleId) {
        return ResponseDTO.ok(roleEmployeeService.getAllEmployeeByRoleId(roleId));
    }

    @ApiOperation(value = "从角色成员列表中移除员工 @author zhuoda")
    @ApiImplicitParams({@ApiImplicitParam(name = "employeeId", value = "员工id", paramType = "query", required = true),
            @ApiImplicitParam(name = "roleId", value = "角色id", paramType = "query", required = true)})
    @GetMapping("/role/employee/removeEmployee")
    public ResponseDTO<String> removeEmployee(Long employeeId, Long roleId) {
        return roleEmployeeService.removeRoleEmployee(employeeId, roleId);
    }

    @ApiOperation(value = "从角色成员列表中批量移除员工 @author zhuoda")
    @PostMapping("/role/employee/batchRemoveRoleEmployee")
    public ResponseDTO<String> batchRemoveEmployee(@Valid @RequestBody RoleEmployeeUpdateForm updateForm) {
        return roleEmployeeService.batchRemoveRoleEmployee(updateForm);
    }

    @ApiOperation(value = "角色成员列表中批量添加员工 @author zhuoda")
    @PostMapping("/role/employee/batchAddRoleEmployee")
    public ResponseDTO<String> addEmployeeList(@Valid @RequestBody RoleEmployeeUpdateForm addForm) {
        return roleEmployeeService.batchAddRoleEmployee(addForm);
    }

    @ApiOperation(value = "获取员工所有选中的角色和所有角色 @author zhuoda")
    @GetMapping("/role/employee/getRoles/{employeeId}")
    public ResponseDTO<List<RoleSelectedVO>> getRoleByEmployeeId(@PathVariable Long employeeId) {
        return ResponseDTO.ok(roleEmployeeService.getRoleInfoListByEmployeeId(employeeId));
    }
}
