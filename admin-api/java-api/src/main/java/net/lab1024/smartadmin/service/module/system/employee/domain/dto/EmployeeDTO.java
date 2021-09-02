package net.lab1024.smartadmin.service.module.system.employee.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 员工列表DTO
 *
 * @author 开云
 * @date 2017年12月21日上午09:09:31
 */
@Data
public class EmployeeDTO {

    @ApiModelProperty("主键id")
    private Long id;

    @ApiModelProperty("登录账号")
    private String loginName;

    @ApiModelProperty("员工名称")
    private String actualName;

    @ApiModelProperty("手机号码")
    private String phone;

    @ApiModelProperty("部门id")
    private Long departmentId;

    @ApiModelProperty("是否被禁用")
    private Boolean disabledFlag;

    @ApiModelProperty("是否删除")
    private Boolean deletedFlag;

    @ApiModelProperty("部门名称")
    private String departmentName;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;
}
