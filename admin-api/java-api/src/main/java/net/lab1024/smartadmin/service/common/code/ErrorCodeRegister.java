package net.lab1024.smartadmin.service.common.code;


import static net.lab1024.smartadmin.service.common.code.ErrorCodeRangeContainer.register;

/**
 * 注册code状态码
 * 之所以需要在此处手动注册
 * 主要是为了 有一个地方能统一、清楚的看到所有状态码目前的范围，方便新增定义
 *
 * @author zhuoda
 * @date 2021/09/27 23:09
 */
public class ErrorCodeRegister {

    static {

        // 系统 错误码
        register(SystemErrorCode.class, 10001, 20000);

        // 意外 错误码
        register(UnexpectedErrorCode.class, 20001, 30000);

        // 用户 通用错误码
        register(UserErrorCode.class, 30001, 40000);

    }


    public static void init() {
        ErrorCodeRangeContainer.finish();
    }

    public static void main(String[] args) {
        ErrorCodeRegister.init();
    }

}
