package net.lab1024.smartadmin.service.module.system.menu.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Description
 *
 * @author lihaifan
 * @date 2021/9/1 20:15
 */
@Data
public class RequestUrlVO {

    @ApiModelProperty("注释说明")
    private String comment;

    @ApiModelProperty("controller.method")
    private String name;

    @ApiModelProperty("url")
    private String url;
}
