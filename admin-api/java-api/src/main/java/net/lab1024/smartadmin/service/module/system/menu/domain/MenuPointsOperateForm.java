package net.lab1024.smartadmin.service.module.system.menu.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 菜单功能点操作Form
 *
 * @author 卓大
 * @date 2021/7/30 10:56
 */
@Data
public class MenuPointsOperateForm {

    @ApiModelProperty("菜单ID")
    private Long menuId;

    @ApiModelProperty("功能点名称")
    @NotBlank(message = "功能点不能为空")
    @Length(max = 30, message = "功能点最多30个字符")
    private String menuName;

    @ApiModelProperty("禁用状态")
    @NotNull(message = "禁用状态不能为空")
    private Boolean disabledFlag;

    @ApiModelProperty("接口权限")
    private List<String> permsList;

    @ApiModelProperty("功能点关联菜单ID")
    private Long contextMenuId;
}
