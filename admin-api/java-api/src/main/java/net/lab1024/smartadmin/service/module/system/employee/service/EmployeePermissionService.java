package net.lab1024.smartadmin.service.module.system.employee.service;

import io.jsonwebtoken.lang.Collections;
import net.lab1024.smartadmin.service.common.security.SecurityMetadataSource;
import net.lab1024.smartadmin.service.common.util.SmartRequestUtil;
import net.lab1024.smartadmin.service.module.system.login.domain.RequestEmployee;
import net.lab1024.smartadmin.service.module.system.menu.domain.bo.MenuLoginBO;
import net.lab1024.smartadmin.service.module.system.menu.domain.vo.MenuVO;
import net.lab1024.smartadmin.service.module.system.role.service.RoleEmployeeService;
import net.lab1024.smartadmin.service.module.system.role.service.RoleMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 员工权限校验
 *
 * @author 李善逸
 * @date 2021/8/5 17:14
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
     * 查询用户拥有的前端菜单项 用于登陆返回 前端动态路由配置
     *
     * @param employeeId
     * @return
     */
    public List<MenuVO> getEmployeeMenuAndPointsList(Long employeeId) {
        List<Long> roleIdList = roleEmployeeService.getRoleIdList(employeeId);
        if (Collections.isEmpty(roleIdList)) {
            return new ArrayList<>();
        }

        return roleMenuService.getMenuList(roleIdList);
    }

}
