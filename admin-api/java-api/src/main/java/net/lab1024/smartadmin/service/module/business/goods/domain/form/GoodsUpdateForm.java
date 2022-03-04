package net.lab1024.smartadmin.service.module.business.goods.domain.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 商品 更新 DTO
 *
 * @author 胡克
 * @date 2021/8/5 14:42
 */
@Data
public class GoodsUpdateForm extends GoodsAddForm {

    @ApiModelProperty("商品id")
    @NotNull(message = "商品id不能为空")
    private Long goodsId;
}
