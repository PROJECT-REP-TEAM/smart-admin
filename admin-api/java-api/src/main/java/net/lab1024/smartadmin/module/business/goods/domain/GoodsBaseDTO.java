package net.lab1024.smartadmin.module.business.goods.domain;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.smartadmin.common.serializer.FileKeySerializer;
import net.lab1024.smartadmin.common.swagger.ApiModelPropertyEnum;
import net.lab1024.smartadmin.module.business.goods.constant.GoodsTypeEnum;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 商品 基础属性 DTO
 *
 * @author 胡克
 * @date 2021/8/5 14:42
 */
@Data
public class GoodsBaseDTO {

    @ApiModelPropertyEnum(desc = "商品类型", value = GoodsTypeEnum.class)
    private Integer goodsType;

    @ApiModelProperty("商品分类")
    @NotNull(message = "商品分类不能为空")
    private Long categoryId;

    @ApiModelProperty("商品名称")
    @NotBlank(message = "商品名称不能为空")
    @Length(max = 200, message = "商品名称最多50字符")
    private String goodsName;

    @ApiModelProperty("商品简介")
    @Length(max = 200, message = "商品简介最多200字符")
    private String goodsIntro;

    @ApiModelProperty("商品价格")
    @NotNull(message = "商品价格不能为空")
    @DecimalMin(value = "0", message = "商品价格最低0")
    private BigDecimal price;

    @ApiModelProperty("商品封面")
    @Length(max = 250, message = "商品封面最多250字符")
    @JsonSerialize(using = FileKeySerializer.class)
    private String coverPic;

    @ApiModelProperty("上架状态")
    @NotNull(message = "上架状态不能为空")
    private Boolean shelvesFlag;

    @ApiModelProperty("备注|可选")
    @Length(max = 200, message = "备注最多200字符")
    private String remark;
}
