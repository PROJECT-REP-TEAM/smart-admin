package net.lab1024.smartadmin.service.common.codeconst;

import lombok.extern.slf4j.Slf4j;

/**
 * 1 表示成功
 * 10 - 100 表示  系统异常，即java报错了
 * 100 以上， 业务
 * <p>
 * 根据实际业务，设置范围值
 * 正常100就够了
 *
 * @author Administrator
 */
@Slf4j
public class ResponseCodeConst {

    public static final ResponseCodeConst SUCCESS = new ResponseCodeConst(1, "操作成功!", true);

    public static final ResponseCodeConst SYSTEM_ERROR = new ResponseCodeConst(11, "系统繁忙，请稍后重试");

    public static final ResponseCodeConst ERROR_PARAM = new ResponseCodeConst(101, "参数异常");

    public static final ResponseCodeConst ERROR_PARAM_ANY = new ResponseCodeConst(102, "%s参数异常！");

    public static final ResponseCodeConst DEVELOPMENT = new ResponseCodeConst(112, "此功能正在开发中");

    public static final ResponseCodeConst NOT_EXISTS = new ResponseCodeConst(113, "数据不存在");

    public static final ResponseCodeConst REQUEST_METHOD_ERROR = new ResponseCodeConst(114, "请求方式错误");

    public static final ResponseCodeConst JSON_FORMAT_ERROR = new ResponseCodeConst(115, "前端请求参数格式错误");

    public static final ResponseCodeConst PERMISSION_DENIED = new ResponseCodeConst(116, "您没有权限修改数据");

    public static final ResponseCodeConst ALREADY_EXIST = new ResponseCodeConst(117, "数据已存在");

    public static final ResponseCodeConst STATUS_ERROR = new ResponseCodeConst(118, "数据状态异常");

    public static final ResponseCodeConst AREA_ERROR = new ResponseCodeConst(119, "地区数据错误");

    public static final ResponseCodeConst REQUEST_ERROR = new ResponseCodeConst(120, "请求异常");

    public static final ResponseCodeConst TOKEN_ERROR = new ResponseCodeConst(121, "登录失效，请重新登录");

    public static final ResponseCodeConst BUSINESS_HANDING = new ResponseCodeConst(122, "业务正在繁忙处理中，请稍后再试");

    public static final ResponseCodeConst NOT_SUPPORT = new ResponseCodeConst(123, "暂不支持");

    public static final ResponseCodeConst REPEAT_SUBMIT = new ResponseCodeConst(125, "太...太快了，请您稍后重试~");

    protected int code;

    protected String msg;

    protected boolean success;

    public ResponseCodeConst() {
    }

    protected ResponseCodeConst(int code, String msg) {
        super();
        this.code = code;
        this.msg = msg;
        ResponseCodeContainer.put(this);
    }

    protected ResponseCodeConst(int code, String msg, boolean success) {
        super();
        this.code = code;
        this.msg = msg;
        this.success = success;
        ResponseCodeContainer.put(this);
    }

    protected ResponseCodeConst(int code) {
        super();
        this.code = code;
        ResponseCodeContainer.put(this);
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean issucc() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public static void init() {
        log.info("-------------- ResponseCodeConst init -------------");
    }


}
