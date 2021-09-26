package net.lab1024.smartadmin.service.module.system.login.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.smartadmin.service.common.enumconst.GenderEnum;
import net.lab1024.smartadmin.service.common.swagger.ApiModelPropertyEnum;

import java.util.List;

/**
 * 员工登录信息 DTO
 *
 * @author listen
 * @date 2021年07月21日 上午07:06:31
 */
@Data
public class EmployeeLoginInfoDTO {

    @ApiModelProperty("员工id")
    private Long employeeId;

    @ApiModelProperty("登录账号")
    private String loginName;

    @ApiModelProperty("员工名称")
    private String actualName;

    @ApiModelPropertyEnum(GenderEnum.class)
    private Integer gender;

    @ApiModelProperty("手机号码")
    private String phone;

    @ApiModelProperty("部门id")
    private Long departmentId;

    @ApiModelProperty("部门名称")
    private String departmentName;

    @ApiModelProperty("是否被禁用")
    private Boolean disabledFlag;

    @ApiModelProperty("是否为超管")
    private Boolean isSuperMan;

    @ApiModelProperty("角色列表")
    private List<Long> roleList;

    @ApiModelProperty("所在校区ID")
    private Long schoolId;

    @ApiModelProperty("所在校区名称")
    private String schoolName;
}
