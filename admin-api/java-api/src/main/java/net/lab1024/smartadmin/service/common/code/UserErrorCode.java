package net.lab1024.smartadmin.service.common.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 *
 * @author zhuoda
 * @Date 2021-09-27
 */
@Getter
@AllArgsConstructor
public enum UserErrorCode implements ErrorCode {

    PARAM_ERROR(30001, "参数错误"),

    DATA_NOT_EXIST(30002, "左翻右翻，数据竟然找不到了~"),

    ALREADY_EXIST(30003, "数据已存在了呀~"),

    REPEAT_SUBMIT(30004, "亲~您操作的可太快了，请稍等下再操作~"),

    NO_PERMISSION(30005, "对不起，您没有权限哦~"),

    DEVELOPING(30006, "系統正在紧急开发中，敬请期待~"),

    LOGIN_STATE_INVALID(30007, "您还未登录或登录失效，请重新登录！"),

    LOGIN_OTHER_DEVICE(30008, "您的账号已在其他设备登录,请重新登录"),

    USER_STATUS_ERROR(30009, "用户状态异常"),

    LOGIN_FAILED(30010, "用户名或密码错误!"),

    VERIFICATION_CODE_INVALID(30011, "验证码错误或已过期，请输入正确的验证码"),

    ;

    private final int code;

    private final String msg;

    private final String level;

    UserErrorCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
        this.level = LEVEL_USER;
    }
}
