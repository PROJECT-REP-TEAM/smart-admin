package net.lab1024.smartadmin.service.module.system.menu.service;

import net.lab1024.smartadmin.service.common.util.SmartRequestUtil;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import net.lab1024.smartadmin.service.module.system.login.domain.RequestEmployee;

import javax.servlet.http.HttpServletRequest;

/**
 * 菜单权限控制
 *
 * @author lihaifan
 * @date 2021/8/5 17:14
 */
@Service
public class MenuPermissionService {

    @Autowired
    private MenuEmployeeService menuEmployeeService;

    /**
     * header中存在no_check_permission的值为true 则 权限通过
     */
    public static String NO_CHECK_PERMISSION_HEADER = "noCheckPermission";

    /**
     * 校验是否有权限
     * @param perms
     * @return
     */
    public boolean checkPermission(String perms){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String checkPermission = request.getHeader(NO_CHECK_PERMISSION_HEADER);
        if(StringUtils.isNotBlank(checkPermission) && BooleanUtils.toBoolean(checkPermission)){
            return true;
        }
        RequestEmployee requestEmployee = SmartRequestUtil.getRequestEmployee();
        if(requestEmployee == null){
            return false;
        }
        return menuEmployeeService.checkEmployeeHavePrivilege(requestEmployee,perms);
    }
}
