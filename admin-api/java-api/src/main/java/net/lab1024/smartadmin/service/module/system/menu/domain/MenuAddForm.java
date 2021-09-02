package net.lab1024.smartadmin.service.module.system.menu.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import java.util.List;

/**
 * @author 卓大
 * @Date 2021/7/27
 */
@Data
public class MenuAddForm extends MenuBasicDTO {

    @ApiModelProperty(hidden = true)
    private Long createUserId;

    @ApiModelProperty("功能点列表")
    @Valid
    private List<MenuPointsOperateForm> pointList;
}
