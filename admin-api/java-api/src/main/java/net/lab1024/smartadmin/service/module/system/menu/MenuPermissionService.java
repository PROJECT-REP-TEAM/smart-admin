package net.lab1024.smartadmin.service.module.system.menu;

import net.lab1024.smartadmin.service.common.security.SecurityMetadataSource;
import net.lab1024.smartadmin.service.module.system.login.domain.EmployeeLoginInfoDTO;
import net.lab1024.smartadmin.service.common.util.SmartEmployeeTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 菜单权限控制
 *
 * @author 卓大
 * @date 2021/8/5 17:14
 */
@Service(SecurityMetadataSource.PRIVILEGE_CHECK_NAME)
public class MenuPermissionService {

    @Autowired
    private MenuEmployeeService menuEmployeeService;

    /**
     * 校验是否有权限
     * @param perms
     * @return
     */
    public boolean checkPermission(String perms){
        EmployeeLoginInfoDTO employeeLoginInfoDTO = SmartEmployeeTokenUtil.getRequestEmployee();
        if(employeeLoginInfoDTO == null){
            return false;
        }
        return menuEmployeeService.checkEmployeeHavePrivilege(employeeLoginInfoDTO,perms);
    }
}
