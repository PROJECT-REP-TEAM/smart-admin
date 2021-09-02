package net.lab1024.smartadmin.service.config;

import com.alibaba.fastjson.JSON;
import net.lab1024.smartadmin.service.module.support.responseencrypt.ResponseEncryptDecryptAspect;
import net.lab1024.smartadmin.service.module.support.responseencrypt.ResponseEncryptDecryptUserDTO;
import net.lab1024.smartadmin.service.module.system.login.domain.EmployeeLoginInfoDTO;
import net.lab1024.smartadmin.service.util.SmartEmployeeTokenUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.http.HttpServletRequest;

/**
 * [ 接口加解密配置 ]
 *
 * @author 罗伊
 * @date 2021/1/27 11:22
 */
@Configuration
public class SmartResponseEncryptAspectConfig {


    @Bean
    public ResponseEncryptDecryptAspect responseEncryptAspect() {
        return new ResponseEncryptDecryptAspect(this::employeeFunction);
    }

    /**
     * 请求用户信息
     * @param request
     * @return
     */
    private ResponseEncryptDecryptUserDTO employeeFunction(HttpServletRequest request){
        EmployeeLoginInfoDTO employeeLoginInfoDTO = SmartEmployeeTokenUtil.getRequestEmployee();
        if(employeeLoginInfoDTO == null){
            return null;
        }
        ResponseEncryptDecryptUserDTO responseEncryptDecryptUserDTO = new ResponseEncryptDecryptUserDTO();
        responseEncryptDecryptUserDTO.setUserId(employeeLoginInfoDTO.getEmployeeId());
        responseEncryptDecryptUserDTO.setUserName(employeeLoginInfoDTO.getActualName());
        responseEncryptDecryptUserDTO.setExtData(JSON.toJSONString(employeeLoginInfoDTO));
        return responseEncryptDecryptUserDTO;
    }

}
