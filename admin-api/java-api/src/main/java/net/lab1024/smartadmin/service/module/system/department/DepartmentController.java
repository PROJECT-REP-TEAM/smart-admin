package net.lab1024.smartadmin.service.module.system.department;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.lab1024.smartadmin.service.common.constant.SwaggerTagConst;
import net.lab1024.smartadmin.service.common.controller.AdminBaseController;
import net.lab1024.smartadmin.service.common.domain.ResponseDTO;
import net.lab1024.smartadmin.service.module.system.department.domain.dto.DepartmentCreateDTO;
import net.lab1024.smartadmin.service.module.system.department.domain.dto.DepartmentUpdateDTO;
import net.lab1024.smartadmin.service.module.system.department.domain.vo.DepartmentEmployeeTreeVO;
import net.lab1024.smartadmin.service.module.system.department.domain.vo.DepartmentTreeVO;
import net.lab1024.smartadmin.service.module.system.department.domain.vo.DepartmentVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 部门管理路由器
 *
 * @author listen
 * @date 2017/12/19 14:29
 */
@Api(tags = {SwaggerTagConst.Admin.MANAGER_DEPARTMENT})
@RestController
public class DepartmentController extends AdminBaseController {

    @Autowired
    private DepartmentService departmentService;

    @ApiOperation(value = "查询部门树形列表")
    @GetMapping("/department/treeList")
    public ResponseDTO<List<DepartmentTreeVO>> departmentTree() {
        return departmentService.departmentTree();
    }

    @ApiOperation(value = "查询部门及员工树列表")
    @GetMapping("/department/departmentEmployeeTree")
    public ResponseDTO<List<DepartmentEmployeeTreeVO>> departmentEmployeeTree() {
        return departmentService.departmentEmployeeTree();
    }

    @ApiOperation(value = "根据部门名称查询部门树列表")
    @GetMapping("/department/departmentTreeByName")
    public ResponseDTO<List<DepartmentTreeVO>> departmentTreeByName(String departmentName) {
        return departmentService.departmentTreeByName(departmentName);
    }

    @ApiOperation(value = "添加部门")
    @PostMapping("/department/add")
    public ResponseDTO<String> addDepartment(@Valid @RequestBody DepartmentCreateDTO createDTO) {
        return departmentService.addDepartment(createDTO);
    }

    @ApiOperation(value = "更新部门信息")
    @PostMapping("/department/update")
    public ResponseDTO<String> updateDepartment(@Valid @RequestBody DepartmentUpdateDTO updateDTO) {
        return departmentService.updateDepartment(updateDTO);
    }

    @ApiOperation(value = "删除部门")
    @GetMapping("/department/delete/{deptId}")
    public ResponseDTO<String> delDepartment(@PathVariable Long deptId) {
        return departmentService.delDepartment(deptId);
    }

    @ApiOperation(value = "获取部门信息")
    @GetMapping("/department/query/{deptId}")
    public ResponseDTO<DepartmentVO> getDepartment(@PathVariable Long deptId) {
        return departmentService.getDepartmentById(deptId);
    }

    @ApiOperation(value = "查询部门列表")
    @GetMapping("/department/listAll")
    public ResponseDTO<List<DepartmentVO>> listAll() {
        return departmentService.listAll();
    }


    @ApiOperation(value = "上下移动")
    @GetMapping("/department/upOrDown/{deptId}/{swapId}")
    public ResponseDTO<String> upOrDown(@PathVariable Long deptId, @PathVariable Long swapId) {
        return departmentService.upOrDown(deptId, swapId);
    }

    @ApiOperation(value = "升级")
    @GetMapping("/department/upgrade/{deptId}")
    public ResponseDTO<String> upgrade(@PathVariable Long deptId) {
        return departmentService.upgrade(deptId);
    }

    @ApiOperation(value = "降级")
    @GetMapping("/department/downgrade/{deptId}/{preId}")
    public ResponseDTO<String> downgrade(@PathVariable Long deptId, @PathVariable Long preId) {
        return departmentService.downgrade(deptId, preId);
    }

    @ApiOperation("获取校区列表 by 善逸")
    @GetMapping("/department/querySchoolList")
    public ResponseDTO<List<DepartmentVO>> querySchoolList() {
        return departmentService.querySchoolList();
    }

}
