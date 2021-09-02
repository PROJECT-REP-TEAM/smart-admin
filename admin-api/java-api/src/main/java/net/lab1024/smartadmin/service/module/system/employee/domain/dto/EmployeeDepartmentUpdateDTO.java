package net.lab1024.smartadmin.service.module.system.employee.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * 员工更新部门 DTO
 *
 * @author 善逸
 * @date 2021年07月29日 20:00
 */
@Data
public class EmployeeDepartmentUpdateDTO {

    @ApiModelProperty("员工id")
    @NotEmpty(message = "员工id不能为空")
    @Size(max = 99, message = "一次最多调整99个员工")
    private List<Long> employeeIdList;

    @ApiModelProperty("部门ID")
    @NotNull(message = "部门ID不能为空")
    private Long departmentId;
}
