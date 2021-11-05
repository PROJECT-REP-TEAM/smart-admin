package net.lab1024.smartadmin.service.module.support.repeatsubmit;

import lombok.extern.slf4j.Slf4j;
import net.lab1024.smartadmin.service.common.code.UserErrorCode;
import net.lab1024.smartadmin.service.common.domain.ResponseDTO;
import net.lab1024.smartadmin.service.module.support.repeatsubmit.annoation.RepeatSubmit;
import net.lab1024.smartadmin.service.module.support.repeatsubmit.ticket.AbstractRepeatSubmitTicket;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Method;

/**
 * [  ]
 *
 * @author 罗伊
 * @date
 */
@Aspect
@Slf4j
public class RepeatSubmitAspect {

    private AbstractRepeatSubmitTicket repeatSubmitTicket;

    /**
     * 获取凭证信息
     * rep
     *
     * @param repeatSubmitTicket
     */
    public RepeatSubmitAspect(AbstractRepeatSubmitTicket repeatSubmitTicket) {
        this.repeatSubmitTicket = repeatSubmitTicket;
    }

    /**
     * 定义切入点
     *
     * @param point
     * @return
     * @throws Throwable
     */
    @Around("@annotation(net.lab1024.smartadmin.service.module.support.repeatsubmit.annoation.RepeatSubmit)")
    public Object around(ProceedingJoinPoint point) throws Throwable {

        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        String servletPath = attributes.getRequest().getServletPath();
        String ticket = this.repeatSubmitTicket.getTicket(servletPath);
        if (StringUtils.isEmpty(ticket)) {
            return point.proceed();
        }
        Long timeStamp = this.repeatSubmitTicket.getTicketTimestamp(ticket);
        if (timeStamp != null) {
            Method method = ((MethodSignature) point.getSignature()).getMethod();
            RepeatSubmit annotation = method.getAnnotation(RepeatSubmit.class);
            int interval = Math.min(annotation.value(), RepeatSubmit.MAX_INTERVAL);
            if (System.currentTimeMillis() < (long) timeStamp + interval) {
                // 提交频繁
                return ResponseDTO.error(UserErrorCode.REPEAT_SUBMIT);
            }
        }
        Object obj = null;
        try {
            obj = point.proceed();
            this.repeatSubmitTicket.putTicket(ticket);
        } catch (Throwable throwable) {
            log.error("", throwable);
        } finally {
            this.repeatSubmitTicket.removeTicket(ticket);
        }
        return obj;
    }

}
