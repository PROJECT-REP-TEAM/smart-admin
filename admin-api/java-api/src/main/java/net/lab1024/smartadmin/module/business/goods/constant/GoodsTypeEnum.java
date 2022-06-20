package net.lab1024.smartadmin.module.business.goods.constant;


import lombok.AllArgsConstructor;
import lombok.Getter;
import net.lab1024.smartadmin.common.enumeration.BaseEnum;

/**
 * 商品类型 枚举
 *
 * @author 胡克
 * @date 2021/08/05 15:26
 */
@AllArgsConstructor
@Getter
public enum GoodsTypeEnum implements BaseEnum {

    /**
     * 1 图书
     */
    BOOK(1, "图书"),

    /**
     * 2 课程
     */
    COURSE(2, "课程"),

    ;

    private final Integer value;

    private final String desc;
}