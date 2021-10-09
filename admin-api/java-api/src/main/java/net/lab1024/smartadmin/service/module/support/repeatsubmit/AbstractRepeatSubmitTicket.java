package net.lab1024.smartadmin.service.module.support.repeatsubmit;

import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * [  ]
 *
 * @author yandanyang
 * @date 2021/10/9 19:10
 */
public abstract class AbstractRepeatSubmitTicket {

    private Function<String,String> ticketFunction;


    public AbstractRepeatSubmitTicket(Function<String, String> ticketFunction) {
        this.ticketFunction = ticketFunction;
    }


    /**
     * 获取凭证
     * @param servletPath
     * @return
     */
    public String getTicket(String servletPath){
        return this.ticketFunction.apply(servletPath);
    }

    /**
     * 获取凭证 时间戳
     * @param ticket
     * @return
     */
    abstract Long ticketTimeStamp(String ticket);


    /**
     * 设置本次请求时间
     * @param ticket
     */
    abstract void putTicket(String ticket);

    /**
     * 移除凭证
     * @param ticket
     */
    abstract void removeTicket(String ticket);
}
