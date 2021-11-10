package net.lab1024.smartadmin.service.common.satoken;

import cn.dev33.satoken.stp.StpInterface;
import com.google.common.collect.Lists;
import net.lab1024.smartadmin.service.module.system.employee.service.EmployeeService;
import net.lab1024.smartadmin.service.module.system.login.domain.RequestEmployee;
import net.lab1024.smartadmin.service.module.system.menu.domain.vo.MenuVO;
import net.lab1024.smartadmin.service.module.system.menu.service.MenuEmployeeService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * [ 获取某一用户对于的权限列表以及角色信息 ]
 *
 * @author yandanyang
 * @date 2021/10/13 16:56
 */
@Component
public class SaTokenAuthStp implements StpInterface {

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private MenuEmployeeService menuEmployeeService;


    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        List<String> permissionList = Lists.newArrayList();

        RequestEmployee requestEmployee = employeeService.getById(NumberUtils.toLong(loginId.toString()));
        List<MenuVO> menuList = menuEmployeeService.getMenuByRoleIdList(requestEmployee.getRoleList(), requestEmployee.getIsSuperMan());
        menuList.forEach(e -> {
            if(CollectionUtils.isNotEmpty(e.getPermsList())){
                permissionList.addAll(e.getPermsList());
            }
        });
        return permissionList;
    }

    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        RequestEmployee loginInfoDTO = employeeService.getById(NumberUtils.toLong(loginId.toString()));
        List<Long> roleIdList = loginInfoDTO.getRoleList();
        List<String> roleList = roleIdList.stream().map(e -> e.toString()).collect(Collectors.toList());
        return roleList;
    }
}
