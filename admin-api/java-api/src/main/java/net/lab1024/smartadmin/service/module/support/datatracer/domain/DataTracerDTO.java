package net.lab1024.smartadmin.service.module.support.datatracer.domain;

import lombok.Data;
import net.lab1024.smartadmin.service.common.enumeration.BaseEnum;
import net.lab1024.smartadmin.service.module.support.datatracer.constant.DataTracerBusinessTypeEnum;

import java.time.LocalDateTime;

/**
 * [  ]
 *
 * @author 罗伊
 * @date 2020/8/25 16:03
 */
@Data
public class DataTracerDTO {

    /**
     * 业务id
     */
    private Long businessId;

    /**
     * 业务类型
     */
    private DataTracerBusinessTypeEnum businessType;

    /**
     * 操作类型
     */
    private BaseEnum operateType;

    /**
     * 操作内容
     */
    private String operateContent;

    /**
     * 操作时间
     */
    private LocalDateTime operateTime;

    /**
     * 扩展信息
     */
    private DataTracerExtraDataDTO extraData;

}
