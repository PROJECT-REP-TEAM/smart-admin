package net.lab1024.smartadmin.service.module.support.systemconfig.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 系统配置参数 实体类
 *
 * @author 1024lab
 * @date 2017-12-23 13:41
 */
@Data
@TableName("t_system_config")
public class SystemConfigEntity {

    @TableId(type = IdType.AUTO)
    private Long configId;

    /**
     * 参数key
     */
    private String configKey;

    /**
     * 参数的值
     */
    private String configValue;

    /**
     * 参数名称
     */
    private String configName;

    /**
     * 备注
     */
    private String remark;

    private LocalDateTime updateTime;

    private LocalDateTime createTime;
}
