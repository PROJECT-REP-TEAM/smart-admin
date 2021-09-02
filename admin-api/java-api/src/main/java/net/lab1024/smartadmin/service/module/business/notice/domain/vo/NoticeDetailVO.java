package net.lab1024.smartadmin.service.module.business.notice.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * [  ]
 *
 * @author 罗伊
 */
@Data
public class NoticeDetailVO extends NoticeVO {


    @ApiModelProperty("消息内容")
    private String content;

    @ApiModelProperty("更新时间")
    private LocalDateTime updateTime;

}
