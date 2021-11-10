package net.lab1024.smartadmin.service.common.util;

import lombok.extern.slf4j.Slf4j;
import net.lab1024.smartadmin.service.module.system.login.domain.RequestEmployee;

/**
 * @author 罗伊
 */
@Slf4j
public class SmartRequestUtil {

    private static final ThreadLocal<RequestEmployee> LOCAL_USER = new ThreadLocal<>();

    public static void setRequestEmployee(RequestEmployee requestEmployee) {
        LOCAL_USER.set(requestEmployee);
    }

    public static RequestEmployee getRequestEmployee() {
        return LOCAL_USER.get();
    }

    public static Long getRequestEmployeeId() {
        RequestEmployee requestEmployee = getRequestEmployee();
        if (null == requestEmployee) {
            return null;
        }
        return requestEmployee.getEmployeeId();
    }

    public static void remove() {
        LOCAL_USER.remove();
    }


}
