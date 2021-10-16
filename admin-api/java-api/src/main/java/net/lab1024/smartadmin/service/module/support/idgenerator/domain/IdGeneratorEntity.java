package net.lab1024.smartadmin.service.module.support.idgenerator.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import net.lab1024.smartadmin.service.module.support.idgenerator.constant.IdGeneratorEnum;
import net.lab1024.smartadmin.service.module.support.idgenerator.constant.IdGeneratorRuleTypeEnum;

import java.time.LocalDateTime;

/**
 * @Auther: 卓大
 * @Date: 2018/8/7 0007 13:33
 * @Description:
 */
@Data
@TableName("t_id_generator")
public class IdGeneratorEntity {

    /**
     * 主键id
     *
     * @see IdGeneratorEnum
     */
    @TableId(type = IdType.INPUT)
    private Integer id;

    /**
     * 业务
     */
    private String businessName;

    /**
     * 前缀
     */
    private String prefix;

    /**
     * 生成规则
     *
     * @see IdGeneratorRuleTypeEnum
     */
    private String ruleType;

    /**
     * 最低生成id长度
     */
    private Integer minLength;

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
