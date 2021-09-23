package net.lab1024.smartadmin.service.module.support.file.domain.dto;

import net.lab1024.smartadmin.service.common.domain.PageBaseDTO;
import net.lab1024.smartadmin.service.common.swagger.ApiModelPropertyEnum;
import net.lab1024.smartadmin.service.module.support.file.domain.FileFolderTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Description: 文件信息查询dto
 * @Author: sbq
 * @CreateDate: 2019/7/3 17:38
 * @Version: 1.0
 */
@Data
public class FileQueryDTO extends PageBaseDTO {

    @ApiModelProperty(value = "文件名称")
    private String fileName;

    @ApiModelProperty(value = "业务类型")
    @ApiModelPropertyEnum(FileFolderTypeEnum.class)
    private Integer folderType;

}
