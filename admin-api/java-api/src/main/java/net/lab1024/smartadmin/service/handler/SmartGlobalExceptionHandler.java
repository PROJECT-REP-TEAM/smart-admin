package net.lab1024.smartadmin.service.handler;

import lombok.extern.slf4j.Slf4j;
import net.lab1024.smartadmin.service.common.codeconst.ResponseCodeConst;
import net.lab1024.smartadmin.service.common.enumconst.SystemEnvironmentEnum;
import net.lab1024.smartadmin.service.common.domain.ResponseDTO;
import net.lab1024.smartadmin.service.common.domain.SystemEnvironmentBO;
import net.lab1024.smartadmin.service.common.exception.SmartBusinessException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.List;
import java.util.stream.Collectors;

/**
 * [ 全局异常拦截 ]
 *
 * @author 罗伊
 * @date 2020/8/25 11:57
 */
@Slf4j
@ControllerAdvice
public class SmartGlobalExceptionHandler {

    @Autowired
    private SystemEnvironmentBO systemEnvironmentBO;

    /**
     * 添加全局异常处理流程
     *
     * @param e
     * @return
     * @throws Exception
     */
    @ResponseBody
    @ExceptionHandler(Exception.class)
    public ResponseDTO exceptionHandler(Exception e) {

        // json 格式错误
        if (e instanceof HttpMessageNotReadableException) {
            return ResponseDTO.wrap(ResponseCodeConst.JSON_FORMAT_ERROR);
        }

        String uri = null;
        RequestAttributes request = RequestContextHolder.getRequestAttributes();
        if (null != request) {
            ServletRequestAttributes servletRequest = (ServletRequestAttributes) request;
            uri = servletRequest.getRequest().getRequestURI();
        }

        // http 请求方式错误
        if (e instanceof HttpRequestMethodNotSupportedException) {
            return ResponseDTO.wrap(ResponseCodeConst.REQUEST_METHOD_ERROR);
        }

        // 参数类型错误
        if (e instanceof TypeMismatchException) {
            return ResponseDTO.wrap(ResponseCodeConst.ERROR_PARAM);
        }

        // 参数校验未通过
        if (e instanceof MethodArgumentNotValidException) {
            List<FieldError> fieldErrors = ((MethodArgumentNotValidException) e).getBindingResult().getFieldErrors();
            List<String> msgList = fieldErrors.stream().map(FieldError::getDefaultMessage).collect(Collectors.toList());
            return ResponseDTO.wrapMsg(ResponseCodeConst.ERROR_PARAM, String.join(",", msgList));
        }

        // 参数绑定错误
        if (e instanceof BindException) {
            List<FieldError> fieldErrors = ((BindException) e).getFieldErrors();
            List<String> error = fieldErrors.stream().map(field -> field.getField() + ":" + field.getRejectedValue()).collect(Collectors.toList());
            String errorMsg = ResponseCodeConst.ERROR_PARAM.getMsg() + ":" + error.toString();
            return ResponseDTO.wrapMsg(ResponseCodeConst.ERROR_PARAM, errorMsg);
        }

        if (e instanceof SmartBusinessException) {
            return ResponseDTO.wrapMsg(ResponseCodeConst.SYSTEM_ERROR, e.getMessage());
        }

        if (e instanceof AccessDeniedException) {
            return ResponseDTO.wrapMsg(ResponseCodeConst.SYSTEM_ERROR, "您暂无权限");
        }

        log.error("捕获全局异常,URL:{}", uri, e);

        // 正式环境 不返回错误信息
        SystemEnvironmentEnum currentEnvironment = systemEnvironmentBO.getCurrentEnvironment();
        if (SystemEnvironmentEnum.PROD == currentEnvironment) {
            return ResponseDTO.wrap(ResponseCodeConst.SYSTEM_ERROR);
        }

        return ResponseDTO.wrapMsg(ResponseCodeConst.SYSTEM_ERROR, e.toString());
    }
}
