package net.lab1024.smartadmin.module.business.notice.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * [  ]
 *
 * @author 罗伊
 */
@Data
public class NoticeAddForm {

    @ApiModelProperty("消息标题")
    @Length(max = 200)
    private String title;

    @ApiModelProperty("消息内容")
    @Length(max = 5000)
    private String content;

    @ApiModelProperty(hidden = true)
    private Long createId;

}
