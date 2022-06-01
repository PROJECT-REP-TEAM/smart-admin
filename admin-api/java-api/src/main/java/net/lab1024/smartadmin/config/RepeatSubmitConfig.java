package net.lab1024.smartadmin.config;

import net.lab1024.smartadmin.common.util.SmartRequestUtil;
import net.lab1024.smartadmin.module.support.repeatsubmit.RepeatSubmitAspect;
import net.lab1024.smartadmin.module.support.repeatsubmit.ticket.RepeatSubmitCaffeineTicket;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * [  ]
 *
 * @author 罗伊
 * @date 2021/10/9 18:47
 */
@Configuration
public class RepeatSubmitConfig {


    @Bean
    public RepeatSubmitAspect repeatSubmitAspect() {
        RepeatSubmitCaffeineTicket caffeineTicket = new RepeatSubmitCaffeineTicket(this::ticket);
        return new RepeatSubmitAspect(caffeineTicket);
    }


    /**
     * 获取指明某个用户的凭证
     *
     * @return
     */
    private String ticket(String servletPath) {
        Long employeeId = SmartRequestUtil.getRequestEmployeeId();
        if (employeeId == null) {
            return "";
        }
        return servletPath + "_" + employeeId.toString();
    }
}
