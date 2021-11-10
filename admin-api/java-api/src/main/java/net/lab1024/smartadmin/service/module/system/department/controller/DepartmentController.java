package net.lab1024.smartadmin.service.module.system.department.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.lab1024.smartadmin.service.common.controller.SystemBaseController;
import net.lab1024.smartadmin.service.common.domain.ResponseDTO;
import net.lab1024.smartadmin.service.constant.SwaggerTagConst;
import net.lab1024.smartadmin.service.module.system.department.domain.form.DepartmentAddForm;
import net.lab1024.smartadmin.service.module.system.department.domain.form.DepartmentUpdateForm;
import net.lab1024.smartadmin.service.module.system.department.domain.vo.DepartmentTreeVO;
import net.lab1024.smartadmin.service.module.system.department.domain.vo.DepartmentVO;
import net.lab1024.smartadmin.service.module.system.department.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 部门管理
 *
 * @author zhuoda
 */
@Api(tags = {SwaggerTagConst.System.SYSTEM_DEPARTMENT})
@RestController
public class DepartmentController extends SystemBaseController {

    @Autowired
    private DepartmentService departmentService;

    @ApiOperation(value = "查询部门树形列表 @author zhuoda")
    @GetMapping("/department/treeList")
    public ResponseDTO<List<DepartmentTreeVO>> departmentTree() {
        return departmentService.departmentTree();
    }


    @ApiOperation(value = "添加部门 @author zhuoda")
    @PostMapping("/department/add")
    public ResponseDTO<String> addDepartment(@Valid @RequestBody DepartmentAddForm createDTO) {
        return departmentService.addDepartment(createDTO);
    }

    @ApiOperation(value = "更新部门 @author zhuoda")
    @PostMapping("/department/update")
    public ResponseDTO<String> updateDepartment(@Valid @RequestBody DepartmentUpdateForm updateDTO) {
        return departmentService.updateDepartment(updateDTO);
    }

    @ApiOperation(value = "删除部门 @author zhuoda")
    @GetMapping("/department/delete/{departmentId}")
    public ResponseDTO<String> deleteDepartment(@PathVariable Long departmentId) {
        return departmentService.deleteDepartment(departmentId);
    }

    @ApiOperation(value = "查询部门列表 @author zhuoda")
    @GetMapping("/department/listAll")
    public ResponseDTO<List<DepartmentVO>> listAll() {
        return departmentService.listAll();
    }

}
