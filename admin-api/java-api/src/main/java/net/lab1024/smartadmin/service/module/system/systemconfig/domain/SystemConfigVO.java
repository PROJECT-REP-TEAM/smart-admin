package net.lab1024.smartadmin.service.module.system.systemconfig.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 系统配置 vo
 *
 * @author huke
 */
@Data
public class SystemConfigVO {
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
