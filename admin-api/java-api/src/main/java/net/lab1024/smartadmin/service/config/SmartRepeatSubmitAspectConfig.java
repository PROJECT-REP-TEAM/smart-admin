package net.lab1024.smartadmin.service.config;

import com.alibaba.fastjson.JSON;
import net.lab1024.smartadmin.service.module.support.repeatsubmit.SmartRepeatSubmitAspect;
import net.lab1024.smartadmin.service.module.support.repeatsubmit.SmartRepeatSubmitUserDTO;
import net.lab1024.smartadmin.service.module.system.login.domain.EmployeeLoginInfoDTO;
import net.lab1024.smartadmin.service.util.SmartEmployeeTokenUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.http.HttpServletRequest;

/**
 * [ 接口重复提交配置 ]
 *
 * @author 罗伊
 * @date 2021/1/27 11:22
 */
@Configuration
public class SmartRepeatSubmitAspectConfig {

    @Bean
    public SmartRepeatSubmitAspect repeatSubmitAspect() {
        return new SmartRepeatSubmitAspect(this::userFunction);
    }

    /**
     * 请求用户信息
     *
     * @return
     */
    private SmartRepeatSubmitUserDTO userFunction(HttpServletRequest request) {
        EmployeeLoginInfoDTO requestEmployee = SmartEmployeeTokenUtil.getRequestEmployee();
        if(requestEmployee == null){
            return null;
        }
        SmartRepeatSubmitUserDTO repeatSubmitUserDTO = new SmartRepeatSubmitUserDTO();
        repeatSubmitUserDTO.setUserId(requestEmployee.getEmployeeId());
        repeatSubmitUserDTO.setUserName(requestEmployee.getActualName());
        repeatSubmitUserDTO.setExtData(JSON.toJSONString(requestEmployee));
        return repeatSubmitUserDTO;
    }
}
