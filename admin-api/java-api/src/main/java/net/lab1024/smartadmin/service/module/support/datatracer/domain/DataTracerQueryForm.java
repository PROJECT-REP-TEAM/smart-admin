package net.lab1024.smartadmin.service.module.support.datatracer.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.smartadmin.service.common.domain.PageBaseDTO;
import net.lab1024.smartadmin.service.common.swagger.ApiModelPropertyEnum;
import net.lab1024.smartadmin.service.common.validator.CheckEnum;
import net.lab1024.smartadmin.service.module.support.datatracer.constant.DataTracerBusinessTypeEnum;

import javax.validation.constraints.NotNull;

/**
 * [  ]
 *
 * @author 罗伊
 * @date 2021/8/17 8:47
 */
@Data
public class DataTracerQueryForm extends PageBaseDTO {

    @ApiModelPropertyEnum(DataTracerBusinessTypeEnum.class)
    @CheckEnum(enumClazz = DataTracerBusinessTypeEnum.class, required = true, message = "业务类型错误")
    private Integer businessType;

    @ApiModelProperty("业务id")
    @NotNull(message = "业务id不能为空")
    private Long businessId;
}
