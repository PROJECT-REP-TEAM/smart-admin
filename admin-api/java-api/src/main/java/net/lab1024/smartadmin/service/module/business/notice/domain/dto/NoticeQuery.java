package net.lab1024.smartadmin.service.module.business.notice.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.smartadmin.service.common.domain.PageParam;

/**
 * [  ]
 *
 * @author 罗伊
 */
@Data
public class NoticeQuery extends PageParam {


    @ApiModelProperty("开始日期")
    private String startDate;

    @ApiModelProperty("结束日期")
    private String endDate;


    @ApiModelProperty("消息标题")
    private String title;

    @ApiModelProperty(value = "是否删除", hidden = true)
    private Boolean deletedFlag;

}
