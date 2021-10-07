package net.lab1024.smartadmin.service.module.support.captcha.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 图形验证码 VO
 *
 * @author 胡克
 * @date 2021/8/31 20:52
 */
@Data
public class CaptchaForm {

    @ApiModelProperty
    @NotBlank(message = "验证码标识不能为空" )
    private String captchaId;

    @ApiModelProperty
    @NotBlank(message = "验证码不能为空" )
    private String captcha;
}
