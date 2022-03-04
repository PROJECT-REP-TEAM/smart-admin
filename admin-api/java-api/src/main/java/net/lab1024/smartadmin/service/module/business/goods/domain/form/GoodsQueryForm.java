package net.lab1024.smartadmin.service.module.business.goods.domain.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.smartadmin.service.common.domain.PageParam;
import net.lab1024.smartadmin.service.common.swagger.ApiModelPropertyEnum;
import net.lab1024.smartadmin.service.module.business.goods.constant.GoodsTypeEnum;
import org.hibernate.validator.constraints.Length;

/**
 * 商品 分页查询 DTO
 *
 * @author 胡克
 * @date 2021/8/5 14:42
 */
@Data
public class GoodsQueryForm extends PageParam {

    @ApiModelPropertyEnum(desc = "商品类型|可选", value = GoodsTypeEnum.class)
    private Integer goodsType;

    @ApiModelProperty("商品分类")
    private Integer categoryId;

    @ApiModelProperty("搜索词")
    @Length(max = 30, message = "搜索词最多30字符")
    private String searchWord;

    @ApiModelProperty("上架状态")
    private Boolean shelvesFlag;

    @ApiModelProperty(hidden = true)
    private Boolean deletedFlag;
}
