package net.lab1024.smartadmin.service.module.system.employee.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 更新员工
 *
 * @author 开云
 * @date 2017年12月19日下午2:06:31
 */
@Data
public class EmployeeUpdateDTO extends EmployeeAddDTO {

    @ApiModelProperty("员工id")
    @NotNull(message = "员工id不能为空")
    private Long id;
}
