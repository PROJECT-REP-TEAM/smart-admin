package net.lab1024.smartadmin.service.module.system.datascope.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

/**
 * [  ]
 *
 * @author 罗伊
 */
@Data
@Builder
public class DataScopeViewTypeVO {

    @ApiModelProperty("可见范围")
    private Integer viewType;
    @ApiModelProperty("可见范围名称")
    private String viewTypeName;

    @ApiModelProperty("级别,用于表示范围大小")
    private Integer viewTypeLevel;
}
