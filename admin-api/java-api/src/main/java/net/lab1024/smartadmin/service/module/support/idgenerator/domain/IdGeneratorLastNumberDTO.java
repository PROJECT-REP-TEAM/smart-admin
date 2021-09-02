package net.lab1024.smartadmin.service.module.support.idgenerator.domain;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Auther: 罗伊
 * @Date: 2018/8/7 0007 13:33
 * @Description:
 */
@Data
public class IdGeneratorLastNumberDTO {
    private LocalDateTime updateTime;
    private Long lastNumber;
    private LocalDateTime databaseTime;
}
