package net.lab1024.smartadmin.service.module.system.employee.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.smartadmin.service.module.support.captcha.domain.CaptchaForm;
import net.lab1024.smartadmin.service.common.util.SmartVerificationUtil;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * 登录
 *
 * @author 开云
 * @date 2017年12月19日上午11:49:46
 */
@Data
public class EmployeeLoginDTO {

    @ApiModelProperty(example = "sa")
    @NotBlank(message = "登录名不能为空")
    @Length(max = 30, message = "登录账号最多30字符")
    private String loginName;

    @ApiModelProperty(example = "123456")
    @NotBlank(message = "密码不能为空")
    @Pattern(regexp = SmartVerificationUtil.PWD_REGEXP, message = "请输入6-15位密码(数字|大小写字母|小数点)")
    private String loginPwd;

    @ApiModelProperty("图形验证码|可选")
    // @NotNull(message = "图形验证码不能为空")
    @Valid
    private CaptchaForm captcha;
}
