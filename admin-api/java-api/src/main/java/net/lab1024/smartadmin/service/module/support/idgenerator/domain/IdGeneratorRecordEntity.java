package net.lab1024.smartadmin.service.module.support.idgenerator.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Auther: 罗伊
 * @Date: 2018/8/7 0007 13:33
 * @Description:
 */
@Data
@TableName("t_id_generator_record")
public class IdGeneratorRecordEntity {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Integer generatorId;

    private String time;

    private Long lastNumber;

    private Long count;

    private LocalDateTime updateTime;

    private LocalDateTime createTime;

}
