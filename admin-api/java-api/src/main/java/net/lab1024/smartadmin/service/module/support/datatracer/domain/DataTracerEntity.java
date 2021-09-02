package net.lab1024.smartadmin.service.module.support.datatracer.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import net.lab1024.smartadmin.service.module.support.datatracer.constant.DataTracerBusinessTypeEnum;
import net.lab1024.smartadmin.service.module.support.datatracer.constant.DataTracerOperateTypeEnum;

import java.time.LocalDateTime;

/**
 * [  ]
 *
 * @author 罗伊
 * @date 2020/8/25 11:01
 */
@Data
@TableName("t_data_tracer")
public class DataTracerEntity {

    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 业务id
     */
    private Long businessId;
    /**
     * 业务类型
     * {@link DataTracerBusinessTypeEnum}
     */
    private Integer businessType;

    /**
     * 业务类型描述
     * 因是死数据，只做展示使用故直接存类型描述信息
     */
    private String businessTypeDesc;

    /**
     * 操作类型
     * {@link DataTracerOperateTypeEnum}
     */
    private Integer operateType;

    private String operateTypeDesc;

    /**
     * 操作内容
     */
    private String operateContent;

    /**
     * 操作人
     */
    private Long operatorId;

    private String operatorName;

    /**
     * 扩展数据
     */
    private String extraData;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
