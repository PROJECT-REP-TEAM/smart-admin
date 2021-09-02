package net.lab1024.smartadmin.service.module.business.notice.domain.dto;

import lombok.Data;

/**
 * [  ]
 *
 * @author 罗伊
 */
@Data
public class NoticeReadCountDTO {
    /**
     * 员工id
     */
    private Long employeeId;
    /**
     * 已读消息数
     */
    private Integer readCount;

}
