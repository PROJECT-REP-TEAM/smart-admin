package net.lab1024.smartadmin.service.module.support.operatelog.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * [  ]
 *
 * @author 罗伊
 */
@Data
public class OperateLogVO {

    @ApiModelProperty("主键")
    private Long operateLogId;

    @ApiModelProperty("用户id")
    private Long operateUserId;

    @ApiModelProperty("用户名称")
    private String operateUserName;

    @ApiModelProperty("操作模块")
    private String module;

    @ApiModelProperty("操作内容")
    private String content;

    @ApiModelProperty("请求路径")
    private String url;

    @ApiModelProperty("请求方法")
    private String method;

    @ApiModelProperty("请求参数")
    private String param;

    @ApiModelProperty("请求结果 0失败 1成功")
    private Integer successFlag;

    @ApiModelProperty("失败原因")
    private String failReason;

    @ApiModelProperty("更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;


}