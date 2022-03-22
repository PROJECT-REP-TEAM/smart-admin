package net.lab1024.smartadmin.service.module.system.employee.service;

import net.lab1024.smartadmin.service.common.security.SecurityMetadataSource;
import net.lab1024.smartadmin.service.common.util.SmartRequestUtil;
import net.lab1024.smartadmin.service.module.system.login.domain.RequestEmployee;
import net.lab1024.smartadmin.service.module.system.menu.domain.vo.MenuVO;
import net.lab1024.smartadmin.service.module.system.role.service.RoleEmployeeService;
import net.lab1024.smartadmin.service.module.system.role.service.RoleMenuService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 员工权限校验
 *
 * @author 卓大
 */
@Service(SecurityMetadataSource.PRIVILEGE_CHECK_NAME)
public class EmployeePermissionService {

    @Autowired
    private RoleEmployeeService roleEmployeeService;

    @Autowired
    private RoleMenuService roleMenuService;

    /**
     * 校验是否有权限
     *
     * @param permission
     * @return
     */
    public boolean checkPermission(String permission) {
        RequestEmployee requestEmployee = SmartRequestUtil.getRequestEmployee();
        if (requestEmployee == null) {
            return false;
        }
        if (requestEmployee.getAdministratorFlag()) {
            return true;
        }

        return requestEmployee.getPermissionList().contains(permission);
    }

    /**
     * 构建权限集合
     *
     * @param menuAndPointsList
     */
    public void buildPermissionList(RequestEmployee requestEmployee, List<MenuVO> menuAndPointsList) {
        HashSet<String> permissionList = new HashSet<>();
        for (MenuVO menu : menuAndPointsList) {
            if (StringUtils.isEmpty(menu.getPerms())) {
                continue;
            }

            String[] split = menu.getPerms().split(",");
            for (String perm : split) {
                permissionList.add(perm);
            }
        }
        requestEmployee.setPermissionList(permissionList);
    }

    /**
     * 查询用户拥有的前端菜单项 用于登陆返回 前端动态路由配置
     *
     * @param employeeId
     * @return
     */
    public List<MenuVO> getEmployeeMenuAndPointsList(Long employeeId) {
        List<Long> roleIdList = roleEmployeeService.getRoleIdList(employeeId);
        if (CollectionUtils.isEmpty(roleIdList)) {
            return new ArrayList<>();
        }

        return roleMenuService.getMenuList(roleIdList);
    }

}
