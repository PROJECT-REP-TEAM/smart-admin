package net.lab1024.smartadmin.service.common.exception;

import net.lab1024.smartadmin.service.common.code.ErrorCode;

/**
 * [ 业务逻辑异常,全局异常拦截后统一返回ResponseCodeConst.SYSTEM_ERROR ]
 *
 * @author 罗伊
 * @date 2020/8/25 11:57
 */
public class SmartBusinessException extends RuntimeException {

    public SmartBusinessException() {
    }

    public SmartBusinessException(ErrorCode errorCode) {
        super(errorCode.getMsg());
    }

    public SmartBusinessException(String message) {
        super(message);
    }

    public SmartBusinessException(String message, Throwable cause) {
        super(message, cause);
    }

    public SmartBusinessException(Throwable cause) {
        super(cause);
    }

    public SmartBusinessException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
