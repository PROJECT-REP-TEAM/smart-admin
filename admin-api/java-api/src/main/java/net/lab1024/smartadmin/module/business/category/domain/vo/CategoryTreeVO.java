package net.lab1024.smartadmin.module.business.category.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.smartadmin.module.business.category.domain.dto.CategorySimpleDTO;

import java.util.List;

/**
 * 类目 层级树 vo
 *
 * @author 胡克
 * @date 2021/01/21 17:03
 */
@Data
public class CategoryTreeVO extends CategorySimpleDTO {

    @ApiModelProperty("类目id")
    private Long value;

    @ApiModelProperty("类目名称")
    private String label;

    @ApiModelProperty("子类")
    private List<CategoryTreeVO> children;
}
