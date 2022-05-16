package net.lab1024.smartadmin.service.module.support.file.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * [  ]
 *
 * @author 罗伊
 * @date 2020/8/25 11:57
 */
@Data
@TableName(value = "t_file")
public class FileEntity {

    /**
     * 主键id
     */
    @TableId(type = IdType.AUTO)
    private Long fileId;

    /**
     * 文件夹类型
     */
    private Integer folderType;

    /**
     * 文件名称
     */
    private String fileName;

    /**
     * 文件大小
     */
    private Long fileSize;

    /**
     * 文件key，用于文件下载
     */
    private String fileKey;

    /**
     * 文件类型
     */
    private String fileType;

    /**
     * 创建人，即上传人
     */
    private Long creatorId;

    /**
     * 创建人 姓名
     */
    private String creatorName;

    private LocalDateTime updateTime;

    private LocalDateTime createTime;
}
