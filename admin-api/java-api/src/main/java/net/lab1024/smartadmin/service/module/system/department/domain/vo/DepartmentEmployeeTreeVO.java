package net.lab1024.smartadmin.service.module.system.department.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.smartadmin.service.module.system.employee.domain.dto.EmployeeDTO;

import java.util.List;

/**
 * @author 罗伊
 * @date 2021-01-30 23:57
 */
@Data
public class DepartmentEmployeeTreeVO extends DepartmentVO {

    @ApiModelProperty("部门员工列表")
    private List<EmployeeDTO> employees;

    @ApiModelProperty("子部门")
    private List<DepartmentEmployeeTreeVO> children;

}
