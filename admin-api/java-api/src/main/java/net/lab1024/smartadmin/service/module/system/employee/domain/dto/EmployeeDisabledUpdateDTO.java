package net.lab1024.smartadmin.service.module.system.employee.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * 员工禁用状态更新 DTO
 *
 * @author listen
 * @date 2021年07月29日 20:00
 */
@Data
public class EmployeeDisabledUpdateDTO {

    @ApiModelProperty("员工id")
    @NotEmpty(message = "员工id不能为空")
    @Size(max = 99, message = "一次最多更新99次")
    private List<Long> employeeIdList;

    @ApiModelProperty("禁用状态")
    @NotNull(message = "禁用状态不能为空")
    private Boolean disabledFlag;
}
