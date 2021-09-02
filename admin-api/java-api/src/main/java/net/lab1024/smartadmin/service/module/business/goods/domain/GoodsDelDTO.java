package net.lab1024.smartadmin.service.module.business.goods.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * 商品 删除 DTO
 *
 * @author 胡克
 * @date 2021/8/5 14:42
 */
@Data
public class GoodsDelDTO {

    @ApiModelProperty("商品id集合")
    @NotEmpty(message = "商品id不能为空")
    @Size(max = 99, message = "一次最多删除99")
    private List<Long> goodsIdList;

    @ApiModelProperty(hidden = true)
    private Long updateId;

    @ApiModelProperty(hidden = true)
    private String updateName;
}
