package net.lab1024.smartadmin.service.module.system.login.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.smartadmin.service.module.system.menu.domain.vo.MenuTreeVO;
import net.lab1024.smartadmin.service.module.system.menu.domain.vo.MenuVO;

import java.util.List;

/**
 * 员工登录结果 VO
 *
 * @author listen
 * @date 2021年07月21日 上午07:06:31
 */
@Data
public class LoginResultVO extends RequestEmployee {

    @ApiModelProperty("token")
    private String token;

    @ApiModelProperty("菜单树 用于渲染前端菜单")
    private List<MenuTreeVO> menuTree;

    @ApiModelProperty("菜单列表 用于构建前端路由")
    private List<MenuVO> menuList;

    @ApiModelProperty("菜单列表 用于前端判断路由404")
    private List<MenuVO> allMenuList;

    @ApiModelProperty("功能点权限列表")
    private List<String> pointsList;

}
