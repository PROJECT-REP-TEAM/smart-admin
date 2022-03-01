package net.lab1024.smartadmin.service.module.support.serialnumber.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import net.lab1024.smartadmin.service.module.support.serialnumber.constant.SerialNumberIdEnum;
import net.lab1024.smartadmin.service.module.support.serialnumber.constant.SerialNumberRuleTypeEnum;

import java.time.LocalDateTime;

/**
 *
 * @author 卓大
 */
@Data
@TableName("t_serial_number")
public class SerialNumberEntity {

    /**
     * 主键id
     *
     * @see SerialNumberIdEnum
     */
    @TableId(type = IdType.INPUT)
    private Integer serialNumberId;

    /**
     * 业务
     */
    private String businessName;

    /**
     * 格式
     */
    private String format;

    /**
     * 生成规则
     *
     * @see SerialNumberRuleTypeEnum
     */
    private String ruleType;


    /**
     * 初始值
     */
    private Long initNumber;

    /**
     * 步长随机数范围
     */
    private Integer stepRandomRange;

    /**
     * 备注
     */
    private String remark;

    private LocalDateTime updateTime;

    private LocalDateTime createTime;
}
