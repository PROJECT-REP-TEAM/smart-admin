package net.lab1024.smartadmin.module.support.operatelog.domain;

import net.lab1024.smartadmin.common.domain.PageParam;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * [  ]
 *
 * @author 罗伊
 */
@Data
public class OperateLogQueryForm extends PageParam {


    @ApiModelProperty("开始日期")
    private String startDate;

    @ApiModelProperty("结束日期")
    private String endDate;


    @ApiModelProperty("用户名称")
    private String userName;

    @ApiModelProperty("请求结果 0失败 1成功")
    private Integer resultFlag;

}
