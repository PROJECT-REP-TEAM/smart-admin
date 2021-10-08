package net.lab1024.smartadmin.service.module.business.category.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 类目 更新 DTO 类
 *
 * @author 胡克
 * @date 2021/1/20 16:24
 */
@Data
public class CategoryUpdateForm extends CategoryBaseDTO {

    @ApiModelProperty("类目id")
    @NotNull(message = "类目id不能为空")
    private Long categoryId;
}
