package net.lab1024.smartadmin.common.exception;

import net.lab1024.smartadmin.common.code.ErrorCode;

/**
 * [ 业务逻辑异常,全局异常拦截后统一返回ResponseCodeConst.SYSTEM_ERROR ]
 *
 * @author 罗伊
 * @date 2020/8/25 21:57
 */
public class BusinessException extends RuntimeException {

    public BusinessException() {
    }

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMsg());
    }

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }

    public BusinessException(Throwable cause) {
        super(cause);
    }

    public BusinessException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
