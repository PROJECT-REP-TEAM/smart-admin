package net.lab1024.smartadmin.module.system.login.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.smartadmin.common.domain.RequestUser;
import net.lab1024.smartadmin.common.enumeration.GenderEnum;
import net.lab1024.smartadmin.common.swagger.ApiModelPropertyEnum;

import java.util.Set;

/**
 * 员工请求信息
 *
 * @author 卓大
 * @date 2021年07月21日 晚上23:06:31
 */
@Data
public class RequestEmployee implements RequestUser {

    @ApiModelProperty("token")
    private String token;

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

    @ApiModelProperty("是否为超管")
    private Boolean administratorFlag;

    @ApiModelProperty("权限集合")
    private Set<String> permissionList;

    @Override
    public Long requestUserId() {
        return employeeId;
    }

    @Override
    public String requestUserName() {
        return actualName;
    }
}
