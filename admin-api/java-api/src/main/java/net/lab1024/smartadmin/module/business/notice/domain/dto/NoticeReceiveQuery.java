package net.lab1024.smartadmin.module.business.notice.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * [  ]
 *
 * @author 罗伊
 */
@Data
public class NoticeReceiveQuery extends NoticeQuery {

    @ApiModelProperty(hidden = true)
    private Long employeeId;

    @ApiModelProperty(hidden = true)
    private Boolean sendStatus;

}
