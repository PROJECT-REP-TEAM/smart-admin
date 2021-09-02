package net.lab1024.smartadmin.service.util;

import net.lab1024.smartadmin.service.module.system.login.domain.EmployeeLoginInfoDTO;

/**
 * @author 罗伊
 */
public class SmartEmployeeTokenUtil {

    private static final ThreadLocal<EmployeeLoginInfoDTO> LOCAL_USER = new ThreadLocal<>();

    public static void setUser(EmployeeLoginInfoDTO loginInfoDTO) {
        LOCAL_USER.set(loginInfoDTO);
    }

    public static EmployeeLoginInfoDTO getRequestEmployee() {
        return LOCAL_USER.get();
    }

    public static Long getRequestEmployeeId() {
        EmployeeLoginInfoDTO requestUser = getRequestEmployee();
        if (null == requestUser) {
            return null;
        }
        return requestUser.getEmployeeId();
    }

    public static void remove() {
        LOCAL_USER.remove();
    }
}
