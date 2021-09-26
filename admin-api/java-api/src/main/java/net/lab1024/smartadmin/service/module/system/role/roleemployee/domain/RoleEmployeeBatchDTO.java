package net.lab1024.smartadmin.service.module.system.role.roleemployee.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * 角色员工 批量操作DTO
 *
 * @author listen
 * @date 2017/12/29 15:38
 */
@Data
public class RoleEmployeeBatchDTO {

    @ApiModelProperty("角色id")
    @NotNull(message = "角色id不能为空")
    protected Long roleId;

    @ApiModelProperty(value = "员工id集合")
    @NotEmpty(message = "员工id不能为空")
    @Size(max = 99, message = "一次最多99个员工")
    protected List<Long> employeeIdList;

}