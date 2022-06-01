package net.lab1024.smartadmin.module.support.captcha.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 图形验证码 VO
 *
 * @author 胡克
 * @date 2021/8/31 20:52
 */
@Data
public class CaptchaVO {

    @ApiModelProperty("验证码唯一标识" )
    private String captchaUuid;

    @ApiModelProperty("验证码Base64图片" )
    private String captchaBase64Image;
}
