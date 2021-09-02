package net.lab1024.smartadmin.service.module.system.login.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.smartadmin.service.module.system.menu.domain.MenuTreeVO;

import java.util.List;

/**
 * 员工登录结果 VO
 *
 * @author listen
 * @date 2021年07月21日 上午07:06:31
 */
@Data
public class EmployeeLoginVO extends EmployeeLoginInfoDTO {

    @ApiModelProperty("token")
    private String token;

    @ApiModelProperty("菜单树")
    private List<MenuTreeVO> menuTree;

    @ApiModelProperty("功能点权限列表")
    private List<String> pointsList;

}
