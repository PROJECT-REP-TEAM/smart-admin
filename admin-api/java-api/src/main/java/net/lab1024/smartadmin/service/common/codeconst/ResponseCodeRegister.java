package net.lab1024.smartadmin.service.common.codeconst;

import lombok.extern.slf4j.Slf4j;

/**
 * [  ]
 *
 * @author 罗伊
 * @date 2021/1/8 15:59
 */
@Slf4j
public class ResponseCodeRegister {

    // 范围声明
    static {
        // 系统功能，从0开始，step=1000
        ResponseCodeContainer.register(ResponseCodeConst.class, 0, 1000);
        ResponseCodeContainer.register(LoginResponseCodeConst.class, 1001, 1999);
        ResponseCodeContainer.register(DepartmentResponseCodeConst.class, 2001, 2999);
        ResponseCodeContainer.register(EmployeeResponseCodeConst.class, 3001, 3999);
        ResponseCodeContainer.register(FileResponseCodeConst.class, 4001, 5000);
        ResponseCodeContainer.register(RoleResponseCodeConst.class, 6001, 6999);
        ResponseCodeContainer.register(PrivilegeResponseCodeConst.class, 7001, 7999);
        ResponseCodeContainer.register(PositionResponseCodeConst.class, 13000, 13999);
    }

    public static void init() {
        log.info("-------------- ResponseCodeConst init -------------");
    }
}
