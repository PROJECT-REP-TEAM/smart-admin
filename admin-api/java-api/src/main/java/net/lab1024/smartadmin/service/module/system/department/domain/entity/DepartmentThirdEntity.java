package net.lab1024.smartadmin.service.module.system.department.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 部门实体类
 * t_department 数据表
 *
 * @author listen
 * @date 2017/12/19 10:45
 */
@Data
@TableName(value = "t_department_third")
public class DepartmentThirdEntity {

    private Long departmentId;
    /**
     * {@link net.lab1024.smartadmin.mq.constant.XmfCrmPlatformEnum}
     */
    private Integer platformType;
    /**
     * 第三方平台部门id
     */
    private Long thirdDepartmentId;

    private LocalDateTime updateTime;

    private LocalDateTime createTime;

}
