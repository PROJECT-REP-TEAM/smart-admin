package net.lab1024.smartadmin.service.module.support.operatelog.domain.dto;

import net.lab1024.smartadmin.service.common.domain.PageParamForm;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * [  ]
 *
 * @author 罗伊
 */
@Data
public class OperateLogQueryForm extends PageParamForm {


    @ApiModelProperty("开始日期")
    private String startDate;

    @ApiModelProperty("结束日期")
    private String endDate;


    @ApiModelProperty("用户名称")
    private String userName;

    @ApiModelProperty("请求结果 0失败 1成功")
    private Integer resultFlag;

}
