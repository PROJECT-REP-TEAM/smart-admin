package net.lab1024.smartadmin.service.module.business.goods.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 商品 添加 DTO
 *
 * @author 胡克
 * @date 2021/8/5 14:42
 */
@Data
public class GoodsAddDTO extends GoodsBaseDTO {

    @ApiModelProperty(hidden = true)
    private Long updateId;

    @ApiModelProperty(hidden = true)
    private String updateName;
}
