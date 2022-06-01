package net.lab1024.smartadmin.module.business.notice.domain.vo;
import lombok.Data;

import java.time.LocalDateTime;

import io.swagger.annotations.ApiModelProperty;

/**
 * [  ]
 *
 * @author 罗伊
 */
@Data
public class NoticeVO {

    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("消息标题")
    private String title;


    @ApiModelProperty("消息创建人")
    private Long createUser;

    private Integer sendStatus;

    @ApiModelProperty("消息创建人名称")
    private String createUserName;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

}
