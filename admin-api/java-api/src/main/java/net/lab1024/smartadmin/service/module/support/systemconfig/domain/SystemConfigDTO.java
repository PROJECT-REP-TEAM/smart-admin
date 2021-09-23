package net.lab1024.smartadmin.service.module.support.systemconfig.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author 罗伊
 * @version 1.0
 * @date
 * @since JDK1.8
 */
@Data
public class SystemConfigDTO {

    @ApiModelProperty("主键")
    private Long configId;

    @ApiModelProperty("参数key")
    private String configKey;

    @ApiModelProperty("参数的值")
    private String configValue;

    @ApiModelProperty("参数名称")
    private String configName;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("上次修改时间")
    private LocalDateTime updateTime;

}
