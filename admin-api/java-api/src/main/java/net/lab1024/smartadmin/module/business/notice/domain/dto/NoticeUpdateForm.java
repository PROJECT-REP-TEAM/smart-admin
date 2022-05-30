package net.lab1024.smartadmin.module.business.notice.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * [  ]
 *
 * @author 罗伊
 */
@Data
public class NoticeUpdateForm extends NoticeAddForm {

    @ApiModelProperty("id")
    private Long id;
}
