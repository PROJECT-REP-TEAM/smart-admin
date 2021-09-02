package net.lab1024.smartadmin.service.module.business.notice.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * [  ]
 *
 * @author 罗伊
 */
@Data
public class NoticeUpdateDTO extends NoticeAddDTO {

    @ApiModelProperty("id")
    private Long id;
}
