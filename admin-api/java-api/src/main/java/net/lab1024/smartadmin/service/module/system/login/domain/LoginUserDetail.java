package net.lab1024.smartadmin.service.module.system.login.domain;

import lombok.Data;

/**
 * 员工登陆BO
 *
 * @author lihaifan
 * @date 2021/8/4 11:15
 */
@Data
public class LoginUserDetail extends RequestEmployee {

    /**
     * 登录密码
     */
    private String loginPassword;

}
