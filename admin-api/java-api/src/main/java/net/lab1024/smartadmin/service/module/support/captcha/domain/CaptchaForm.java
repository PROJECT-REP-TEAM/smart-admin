package net.lab1024.smartadmin.service.module.support.captcha.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * 图形验证码 表单
 *
 * @author zhuoda
 */
@Data
public class CaptchaForm {

    @ApiModelProperty(value = "验证码")
    @NotBlank(message = "验证码不能为空")
    private String captchaCode;

    @ApiModelProperty(value = "验证码uuid标识")
    @NotBlank(message = "验证码uuid标识不能为空")
    private String captchaUuid;
}
