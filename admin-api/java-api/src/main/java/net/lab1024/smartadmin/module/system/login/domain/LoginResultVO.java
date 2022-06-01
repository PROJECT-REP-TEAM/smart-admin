package net.lab1024.smartadmin.module.system.login.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.smartadmin.module.system.menu.domain.vo.MenuVO;

import java.util.List;

/**
 * 员工登录结果 VO
 *
 * @author 胡克
 * @date 2021年07月21日 上午07:06:31
 */
@Data
public class LoginResultVO extends RequestEmployee {

    @ApiModelProperty("菜单列表")
    private List<MenuVO> menuList;

}
