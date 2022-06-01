package net.lab1024.smartadmin.module.support.datatracer.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.smartadmin.common.domain.PageParam;
import net.lab1024.smartadmin.common.swagger.ApiModelPropertyEnum;
import net.lab1024.smartadmin.module.support.datatracer.constant.DataTracerBusinessTypeEnum;

import javax.validation.constraints.NotNull;

/**
 * [  ]
 *
 * @author 罗伊
 */
@Data
public class DataTracerQueryForm extends PageParam {

    @ApiModelPropertyEnum(DataTracerBusinessTypeEnum.class)
    private Integer businessType;

    @ApiModelProperty("业务id")
    @NotNull(message = "业务id不能为空")
    private Long businessId;
}
