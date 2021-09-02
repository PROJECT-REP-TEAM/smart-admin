package net.lab1024.smartadmin.service.module.support.idgenerator.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @Auther: 卓大
 * @Date: 2018/8/7 0007 13:33
 * @Description:
 */
@Data
@TableName("t_id_generator")
public class IdGeneratorEntity implements Serializable {

    private static final long serialVersionUID = 5582354131134766548L;

    /**
     * 主键id
     *
     * @see net.lab1024.smartadmin.service.module.support.idgenerator.constant.IdGeneratorEnum
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 英文key
     */
    private String keyName;

    /**
     * 前缀
     */
    private String prefix;

    /**
     * 最低补位长度
     */
    private Integer minLength;

    /**
     * 类型
     */
    private String ruleType;

    /**
     * 初始值
     */
    private Long initNumber;

    /**
     * 步长随机数范围
     */
    private Integer stepRandomRange;

    /**
     * 备注
     */
    private String remark;

    private LocalDateTime updateTime;

    private LocalDateTime createTime;
}
