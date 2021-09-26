package net.lab1024.smartadmin.service.module.system.employee;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.lab1024.smartadmin.service.common.swagger.SwaggerTagConst;
import net.lab1024.smartadmin.service.common.controller.AdminBaseController;
import net.lab1024.smartadmin.service.common.domain.PageResultDTO;
import net.lab1024.smartadmin.service.common.domain.ResponseDTO;
import net.lab1024.smartadmin.service.module.system.employee.domain.dto.*;
import net.lab1024.smartadmin.service.module.system.employee.domain.vo.EmployeeVO;
import net.lab1024.smartadmin.service.util.SmartEmployeeTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 员工管理
 *
 * @author 开云
 * @date 2017年12月19日上午11:34:52
 */
@RestController
@Api(tags = {SwaggerTagConst.Admin.MANAGER_EMPLOYEE})
public class EmployeeController extends AdminBaseController {

    @Autowired
    private EmployeeService employeeService;

    @PostMapping("/employee/query")
    @ApiOperation(value = "员工管理查询", notes = "员工管理查询 @author 开云")
    public ResponseDTO<PageResultDTO<EmployeeVO>> query(@Valid @RequestBody EmployeeQueryDTO query) {
        return employeeService.queryEmployeeList(query);
    }

    @ApiOperation(value = "添加员工", notes = "@author 罗伊")
    @PostMapping("/employee/add")
    public ResponseDTO<String> addEmployee(@Valid @RequestBody EmployeeAddDTO addDTO) {
        addDTO.setUpdateId(SmartEmployeeTokenUtil.getRequestEmployeeId());
        return employeeService.addEmployee(addDTO);
    }

    @ApiOperation(value = "更新员工禁用状态", notes = "@author 罗伊")
    @GetMapping("/employee/update/disabled/{employeeId}")
    public ResponseDTO<String> updateDisableFlag(@PathVariable Long employeeId) {
        return employeeService.updateDisableFlag(employeeId);
    }

    @ApiOperation(value = "批量更新员工禁用状态", notes = "@author 罗伊")
    @PostMapping("/employee/update/batch/disabled")
    public ResponseDTO<String> batchUpdateDisableFlag(@Valid @RequestBody EmployeeDisabledUpdateDTO updateDTO) {
        return employeeService.batchUpdateDisableFlag(updateDTO);
    }

    @ApiOperation(value = "更新员工信息", notes = "@author 罗伊")
    @PostMapping("/employee/update")
    public ResponseDTO<String> updateEmployee(@Valid @RequestBody EmployeeUpdateDTO updateDTO) {
        updateDTO.setUpdateId(SmartEmployeeTokenUtil.getRequestEmployeeId());
        return employeeService.updateEmployee(updateDTO);
    }

    @ApiOperation(value = "删除员工", notes = "@author 罗伊")
    @GetMapping("/employee/delete/{employeeId}")
    public ResponseDTO<String> deleteEmployeeById(@PathVariable Long employeeId) {
        return employeeService.deleteEmployeeById(employeeId);
    }

    @ApiOperation(value = "批量删除员工", notes = "@author 善逸")
    @GetMapping("/employee/update/batch/delete")
    public ResponseDTO<String> batchUpdateDeleteFlag(@RequestParam("employeeIdList")List<Long> employeeIdList) {
        return employeeService.batchUpdateDeleteFlag(employeeIdList);
    }

    @ApiOperation(value = "批量调整员工部门", notes = "@author 善逸")
    @PostMapping("/employee/update/batch/department")
    public ResponseDTO<String> batchUpdateDepartment(@Valid @RequestBody EmployeeDepartmentUpdateDTO updateDto) {
        return employeeService.batchUpdateDepartment(updateDto);
    }

    @ApiOperation(value = "单个员工角色授权", notes = "@author 罗伊")
    @PostMapping("/employee/update/roles")
    public ResponseDTO<String> updateRoles(@Valid @RequestBody EmployeeRoleUpdateDTO updateRolesDTO) {
        return employeeService.updateRole(updateRolesDTO);
    }

    @ApiOperation(value = "修改密码", notes = "@author 罗伊")
    @PostMapping("/employee/update/pwd")
    public ResponseDTO<String> updatePwd(@Valid @RequestBody EmployeeUpdatePwdDTO updatePwdDTO) {
        updatePwdDTO.setEmployeeId(SmartEmployeeTokenUtil.getRequestEmployeeId());
        return employeeService.updatePwd(updatePwdDTO);
    }

    @ApiOperation(value = "查询员工-根据部门id", notes = "@author 罗伊")
    @GetMapping("/employee/query/dept/{deptId}")
    public ResponseDTO<List<EmployeeVO>> listByDepartmentId(@PathVariable Long deptId) {
        return employeeService.getEmployeeByDeptId(deptId);
    }

    @ApiOperation("查询所有员工 by 善逸")
    @GetMapping("/employee/queryAll")
    public ResponseDTO<List<EmployeeVO>> queryAllEmploy(@RequestParam(value = "disabledFlag", required = false) Boolean disabledFlag) {
        return employeeService.queryAllEmploy(disabledFlag);
    }

    @ApiOperation(value = "重置员工密码", notes = "@author 罗伊")
    @GetMapping("/employee/update/pwd/reset/{employeeId}")
    public ResponseDTO<String> resetPassword(@PathVariable Integer employeeId) {
        return employeeService.resetPassword(employeeId);
    }

    @ApiOperation(value = "查询员工-根据校区id", notes = "@author 善逸")
    @GetMapping("/employee/query/school/{deptId}")
    public ResponseDTO<List<EmployeeVO>> listBySchoolId(@PathVariable Long deptId) {
        return employeeService.getEmployeeBySchoolId(deptId);
    }

}
