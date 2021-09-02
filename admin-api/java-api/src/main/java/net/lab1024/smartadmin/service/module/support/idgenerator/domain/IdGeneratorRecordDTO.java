package net.lab1024.smartadmin.service.module.support.idgenerator.domain;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Auther: 罗伊
 * @Date: 2018/8/7 0007 13:33
 * @Description:
 */
@Data
public class IdGeneratorRecordDTO {

    private Integer generatorId;

    private Integer year;

    private Integer month;

    private Integer day;

    private Long lastNumber;

    private Long count;

    private LocalDateTime updateTime;

    private LocalDateTime createTime;

}
