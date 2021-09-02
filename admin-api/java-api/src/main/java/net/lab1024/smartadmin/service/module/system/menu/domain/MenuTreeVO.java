package net.lab1024.smartadmin.service.module.system.menu.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 菜单功能点操作Form
 *
 * @author 卓大
 * @date 2021/7/30 10:56
 */
@Data
public class MenuTreeVO extends MenuVO{

    @ApiModelProperty("菜单子集")
    private List<MenuTreeVO> children;
}
