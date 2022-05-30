package net.lab1024.smartadmin.module.business.goods.domain.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.smartadmin.module.business.goods.domain.GoodsBaseDTO;

/**
 * 商品 添加 DTO
 *
 * @author 胡克
 * @date 2021/8/5 14:42
 */
@Data
public class GoodsAddForm extends GoodsBaseDTO {

    @ApiModelProperty(hidden = true)
    private Long updateId;

    @ApiModelProperty(hidden = true)
    private String updateName;
}
