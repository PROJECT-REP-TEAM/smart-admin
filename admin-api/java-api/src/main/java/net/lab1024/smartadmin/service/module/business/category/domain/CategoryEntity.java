package net.lab1024.smartadmin.service.module.business.category.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 类目 实体类
 *
 * @author 胡克
 * @date 2021/8/6 9:45
 */
@Data
@TableName("t_category")
public class CategoryEntity {

    @TableId(type = IdType.AUTO)
    private Long categoryId;

    /**
     * 类目名称
     */
    private String categoryName;

    /**
     * 类目 类型
     *
     * @see CategoryTypeEnum
     */
    private Integer categoryType;

    /**
     * 父级类目id
     */
    private Long parentId;

    /**
     * 是否禁用
     */
    private Boolean disabledFlag;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 删除状态
     */
    private Boolean deletedFlag;

    /**
     * 备注
     */
    private String remark;

    private LocalDateTime updateTime;

    private LocalDateTime createTime;
}
