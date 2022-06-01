package net.lab1024.smartadmin.module.system.department.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.smartadmin.module.system.employee.domain.vo.EmployeeVO;

import java.util.List;

/**
 * @author 罗伊
 * @date 2021-01-30 23:57
 */
@Data
public class DepartmentEmployeeTreeVO extends DepartmentVO {

    @ApiModelProperty("部门员工列表")
    private List<EmployeeVO> employees;

    @ApiModelProperty("子部门")
    private List<DepartmentEmployeeTreeVO> children;

}
