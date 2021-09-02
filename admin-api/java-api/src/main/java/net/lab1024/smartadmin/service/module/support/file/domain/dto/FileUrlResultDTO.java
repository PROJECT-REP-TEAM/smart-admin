package net.lab1024.smartadmin.service.module.support.file.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 文件 url 查询DTO
 *
 * @author 胡克
 * @date 2020/5/12 14:51
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class FileUrlResultDTO {

    private String fileKey;

    private String fileUrl;
}