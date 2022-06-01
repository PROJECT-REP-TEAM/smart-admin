package net.lab1024.smartadmin.module.business.goods.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import net.lab1024.smartadmin.module.business.category.CategoryDao;
import net.lab1024.smartadmin.module.business.goods.constant.GoodsTypeEnum;
import net.lab1024.smartadmin.module.support.datatracer.annoation.DataTracerFieldBigDecimal;
import net.lab1024.smartadmin.module.support.datatracer.annoation.DataTracerFieldDoc;
import net.lab1024.smartadmin.module.support.datatracer.annoation.DataTracerFieldEnum;
import net.lab1024.smartadmin.module.support.datatracer.annoation.DataTracerFieldSql;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 商品 实体类
 *
 * @author 胡克
 * @date 2021/8/5 14:42
 */
@Data
@TableName("t_goods")
public class GoodsEntity {

    @TableId(type = IdType.AUTO)
    private Long goodsId;

    /**
     * 商品类型
     *
     * @see GoodsTypeEnum
     */
    @DataTracerFieldDoc("商品类型")
    @DataTracerFieldEnum(enumClass = GoodsTypeEnum.class)
    private Integer goodsType;

    /**
     * 第三方商品id
     */
    private Long thirdGoodsId;

    /**
     * 商品分类
     */
    @DataTracerFieldSql(relateColumn = "categoryId",relateDisplayColumn = "categoryName",relateMapper = CategoryDao.class)
    private Long categoryId;

    /**
     * 商品名称
     */
    @DataTracerFieldDoc("商品名称")
    private String goodsName;

    /**
     * 商品简介
     */
    private String goodsIntro;

    /**
     * 商品价格
     */
    @DataTracerFieldDoc("商品价格")
    @DataTracerFieldBigDecimal
    private BigDecimal price;

    /**
     * 商品封面
     */
    private String coverPic;

    /**
     * 上架状态
     */
    @DataTracerFieldDoc("上架状态")
    private Boolean shelvesFlag;

    /**
     * 删除状态
     */
    private Boolean deletedFlag;

    private String remark;

    private LocalDateTime updateTime;

    private LocalDateTime createTime;
}
