package net.lab1024.smartadmin.service.module.support.responseencrypt;

import lombok.Data;

/**
* @Description:    用户信息
* @Author:         sbq
* @CreateDate:     2019/8/2 10:41
* @Version:        1.0
*/
@Data
public class ResponseEncryptDecryptUserDTO {

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 扩展信息
     */
    private String extData;

}
