package net.lab1024.smartadmin.service.config;

import net.lab1024.smartadmin.service.common.util.SmartEmployeeTokenUtil;
import net.lab1024.smartadmin.service.module.support.repeatsubmit.RepeatSubmitAspect;
import net.lab1024.smartadmin.service.module.support.repeatsubmit.RepeatSubmitCaffeineTicket;
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

//    @Autowired
//    private ValueOperations<String, String> redisValueOperations;
//
//    @Bean
//    public RepeatSubmitAspect repeatSubmitAspect() {
//        RepeatSubmitRedisTicket redisTicket = new RepeatSubmitRedisTicket(redisValueOperations, this::ticket);
//        return new RepeatSubmitAspect(redisTicket);
//    }


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
        Long employeeId = SmartEmployeeTokenUtil.getRequestEmployeeId();
        if (employeeId == null) {
            return "";
        }
        return servletPath + "_" + employeeId.toString();
    }
}
