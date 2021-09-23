package net.lab1024.smartadmin.service.module.support.file.domain.dto;

import net.lab1024.smartadmin.service.common.swagger.ApiModelPropertyEnum;
import net.lab1024.smartadmin.service.module.support.file.domain.FileFolderTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * url上传文件 DTO 类
 *
 * @author listen
 * @date 2020年3月16日 10:14:54
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileUrlUploadDTO {

    @ApiModelPropertyEnum(FileFolderTypeEnum.class)
    private Integer folder;

    @ApiModelProperty("文件url")
    @NotBlank(message = "文件url不能为空")
    private String fileUrl;

    @NotNull(message = "userId不能为空")
    private Long userId;

    @NotBlank(message = "上传姓名不能为空")
    private String userName;

}
