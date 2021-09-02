package net.lab1024.smartadmin.service.module.support.captcha.domain;

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

    @ApiModelProperty("验证码标识" )
    private String captchaId;

    @ApiModelProperty("验证码图片" )
    private String captchaImg;
}
