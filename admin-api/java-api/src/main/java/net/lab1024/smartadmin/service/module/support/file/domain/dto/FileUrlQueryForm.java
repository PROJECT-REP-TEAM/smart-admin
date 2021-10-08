package net.lab1024.smartadmin.service.module.support.file.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * 文件 url 查询DTO
 *
 * @author 胡克
 * @date 2020/5/12 14:51
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class FileUrlQueryForm {

    @ApiModelProperty("file key")
    @NotEmpty(message = "file key not empty")
    private List<String> fileKeyList;
}
