package net.lab1024.smartadmin.service.module.support.file.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 文件简单VO
 *
 * @author 善逸
 * @date 2019/10/23 11:15
 */
@Data
public class FileSimpleVO {

    @ApiModelProperty("文件Key")
    private String fileKey;

    @ApiModelProperty("文件Url")
    private String fileUrl;

}
