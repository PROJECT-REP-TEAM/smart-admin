package net.lab1024.smartadmin.service.module.system.employee.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author 1024lab
 * 不带分页的查询条件
 */
@Data
public class EmployeeQueryExportDTO {

    @ApiModelProperty(hidden = true)
    private String phone;

    @ApiModelProperty("姓名")
    private String actualName;

    @ApiModelProperty(hidden = true)
    private String keyword;

    @ApiModelProperty(hidden = true)
    private Long departmentId;

    @ApiModelProperty(hidden = true)
    private Boolean disabledFlag;

    @ApiModelProperty(value = "删除状态", hidden = true)
    private Boolean deletedFlag;

    @ApiModelProperty(value = "员工ID集合", hidden = true)
    private List<Long> employeeIds;

}
