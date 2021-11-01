package net.lab1024.smartadmin.service.common.domain;

import lombok.Data;

/**
 * 验证码
 *
 */
@Data
public class CaptchaVO {

    /**
     * 验证码UUID
     */
    private String uuid;

    /**
     * base64 验证码
     */
    private String code;

}
