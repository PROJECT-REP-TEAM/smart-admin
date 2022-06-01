package net.lab1024.smartadmin.common.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author zhuoda
 * @Date 2021-09-27
 */

@Getter
@AllArgsConstructor
public enum UnexpectedErrorCode implements ErrorCode {

    BUSINESS_HANDING(20001, "呃~ 业务繁忙，请稍后重试"),

    ;

    private final int code;

    private final String msg;

    private final String level;

    UnexpectedErrorCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
        this.level = LEVEL_UNEXPECTED;
    }

}
