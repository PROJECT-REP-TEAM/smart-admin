package net.lab1024.smartadmin.module.system.employee.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.lab1024.smartadmin.common.controller.SystemBaseController;
import net.lab1024.smartadmin.common.domain.PageResult;
import net.lab1024.smartadmin.common.util.SmartRequestUtil;
import net.lab1024.smartadmin.module.system.employee.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import net.lab1024.smartadmin.common.annoation.NoValidPermission;
import net.lab1024.smartadmin.common.domain.ResponseDTO;
import net.lab1024.smartadmin.constant.SwaggerTagConst;
import net.lab1024.smartadmin.module.system.employee.domain.form.*;
import net.lab1024.smartadmin.module.system.employee.domain.vo.EmployeeVO;

import javax.validation.Valid;
import java.util.List;

/**
 * 员工管理
 *
 * @author zhuoda
 */
@RestController
@Api(tags = {SwaggerTagConst.System.SYSTEM_EMPLOYEE})
public class EmployeeController extends SystemBaseController {

    @Autowired
    private EmployeeService employeeService;

    @PostMapping("/employee/query")
    @ApiOperation(value = "员工管理查询 @author zhuoda")
    public ResponseDTO<PageResult<EmployeeVO>> query(@Valid @RequestBody EmployeeQueryForm query) {
        return employeeService.queryEmployee(query);
    }

    @ApiOperation(value = "添加员工 @author zhuoda")
    @PostMapping("/employee/add")
    public ResponseDTO<String> addEmployee(@Valid @RequestBody EmployeeAddForm employeeAddForm) {
        employeeAddForm.setUpdateId(SmartRequestUtil.getRequestEmployeeId());
        return employeeService.addEmployee(employeeAddForm);
    }

    @ApiOperation(value = "更新员工 @author zhuoda")
    @PostMapping("/employee/update")
    public ResponseDTO<String> updateEmployee(@Valid @RequestBody EmployeeUpdateForm employeeUpdateForm) {
        employeeUpdateForm.setUpdateId(SmartRequestUtil.getRequestEmployeeId());
        return employeeService.updateEmployee(employeeUpdateForm);
    }

    @ApiOperation(value = "更新员工禁用/启用状态 @author zhuoda")
    @GetMapping("/employee/update/disabled/{employeeId}")
    public ResponseDTO<String> updateDisableFlag(@PathVariable Long employeeId) {
        return employeeService.updateDisableFlag(employeeId);
    }


    @ApiOperation(value = "批量调整员工部门 @author zhuoda")
    @PostMapping("/employee/update/batch/department")
    public ResponseDTO<String> batchUpdateDepartment(@Valid @RequestBody EmployeeBatchUpdateDepartmentForm batchUpdateDepartmentForm) {
        return employeeService.batchUpdateDepartment(batchUpdateDepartmentForm);
    }

    @ApiOperation(value = "修改密码 @author zhuoda")
    @PostMapping("/employee/update/password")
    @NoValidPermission
    public ResponseDTO<String> updatePassword(@Valid @RequestBody EmployeeUpdatePasswordForm updatePasswordForm) {
        updatePasswordForm.setEmployeeId(SmartRequestUtil.getRequestEmployeeId());
        return employeeService.updatePassword(updatePasswordForm);
    }

    @ApiOperation(value = "重置员工密码 @author zhuoda")
    @GetMapping("/employee/update/password/reset/{employeeId}")
    public ResponseDTO<String> resetPassword(@PathVariable Integer employeeId) {
        return employeeService.resetPassword(employeeId);
    }

    @ApiOperation(value = "查询员工-根据部门id @author zhuoda")
    @GetMapping("/employee/getAllEmployeeByDepartmentId/{departmentId}")
    public ResponseDTO<List<EmployeeVO>> getAllEmployeeByDepartmentId(@PathVariable Long departmentId) {
        return employeeService.getAllEmployeeByDepartmentId(departmentId, Boolean.FALSE);
    }

    @ApiOperation("查询所有员工 @author zhuoda")
    @GetMapping("/employee/queryAll")
    public ResponseDTO<List<EmployeeVO>> queryAllEmployee(@RequestParam(value = "disabledFlag", required = false) Boolean disabledFlag) {
        return employeeService.queryAllEmployee(disabledFlag, SmartRequestUtil.getRequestEmployee());
    }

}
