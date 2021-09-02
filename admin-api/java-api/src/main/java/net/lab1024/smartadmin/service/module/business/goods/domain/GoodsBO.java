package net.lab1024.smartadmin.service.module.business.goods.domain;

import lombok.Data;
import net.lab1024.smartadmin.service.module.business.goods.constant.GoodsTypeEnum;

/**
 * 商品
 *
 * @author 胡克
 * @date 2021/8/5 14:42
 */
@Data
public class GoodsBO {

    /**
     * 商品类型
     *
     * @see GoodsTypeEnum
     */
    private Integer goodsType;

    /**
     * 商品分类
     */
    private Long categoryId;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 删除状态
     */
    private Boolean deletedFlag;
}
