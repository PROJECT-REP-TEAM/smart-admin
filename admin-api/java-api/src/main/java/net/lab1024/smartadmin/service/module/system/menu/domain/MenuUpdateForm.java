package net.lab1024.smartadmin.service.module.system.menu.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author 卓大
 * @Date 2021/7/27
 */
@Data
public class MenuUpdateForm extends MenuBasicDTO {

    @ApiModelProperty("菜单ID")
    @NotNull(message = "菜单ID不能为空")
    private Long menuId;

    @ApiModelProperty(hidden = true)
    private Long updateUserId;

    @ApiModelProperty("功能点列表")
    @Valid
    private List<MenuPointsOperateForm> pointList;
}
