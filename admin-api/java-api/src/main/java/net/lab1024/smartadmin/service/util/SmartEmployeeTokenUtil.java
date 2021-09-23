package net.lab1024.smartadmin.service.util;

import lombok.extern.slf4j.Slf4j;
import net.lab1024.smartadmin.service.module.system.login.domain.EmployeeLoginInfoDTO;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @author 罗伊
 */
@Slf4j
public class SmartEmployeeTokenUtil {

    /**
     * 获取用户信息
     *
     * @return
     */
    public static EmployeeLoginInfoDTO getRequestEmployee() {
        try {
            return (EmployeeLoginInfoDTO) getAuthentication().getPrincipal();
        } catch (Exception e) {
            log.error("获取用户信息异常：{}", e);
        }
        return null;
    }

    /**
     * 获取用户认证信息
     *
     * @return
     */
    public static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    /**
     * 获取用户id
     *
     * @return
     */
    public static Long getRequestEmployeeId() {
        EmployeeLoginInfoDTO requestUser = getRequestEmployee();
        if (null == requestUser) {
            return null;
        }
        return requestUser.getEmployeeId();
    }
}
