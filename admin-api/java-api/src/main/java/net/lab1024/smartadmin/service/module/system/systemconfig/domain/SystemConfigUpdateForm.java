package net.lab1024.smartadmin.service.module.system.systemconfig.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * [  ]
 *
 * @author 罗伊
 * @version 1.0
 *
 * @date
 * @since JDK1.8
 */
@Data
public class SystemConfigUpdateForm extends SystemConfigAddForm {

    @ApiModelProperty("configId")
    @NotNull(message = "configId不能为空")
    private Long configId;
}
