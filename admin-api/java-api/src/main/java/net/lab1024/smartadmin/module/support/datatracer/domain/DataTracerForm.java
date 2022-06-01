package net.lab1024.smartadmin.module.support.datatracer.domain;

import lombok.Data;
import net.lab1024.smartadmin.common.enumeration.BaseEnum;
import net.lab1024.smartadmin.module.support.datatracer.constant.DataTracerBusinessTypeEnum;

import java.time.LocalDateTime;

/**
 * [  ]
 *
 * @author 罗伊
 */
@Data
public class DataTracerForm {

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
    private DataTracerExtraData extraData;

}
