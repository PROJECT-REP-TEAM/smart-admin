package net.lab1024.smartadmin.service.module.support.systemconfig.domain;

import net.lab1024.smartadmin.service.common.domain.PageBaseDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 分页查询 系统配置
 *
 * @author 罗伊
 * @version 1.0
 *
 * @date
 * @since JDK1.8
 */
@Data
public class SystemConfigQueryDTO extends PageBaseDTO {

    @ApiModelProperty("参数KEY")
    private String configKey;

    @ApiModelProperty("参数类别")
    private String configGroup;
}
