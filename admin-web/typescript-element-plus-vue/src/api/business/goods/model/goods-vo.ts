/*
 * @Author: zhuoda
 * @Date: 2021-08-12 18:10:22
 * @LastEditTime: 2021-09-01 21:25:35
 * @LastEditors: zhuoda
 * @Description:
 * @FilePath: /smart-admin/src/api/business/goods/model/goods-vo.ts
 */
/**
 *
 * @export
 * @interface GoodsVo
 */
export interface GoodsVo {
    /**
     * 商品分类
     * @type {number}
     * @memberof GoodsVo
     */
    categoryId?: number;
    /**
     * 商品封面
     * @type {string}
     * @memberof GoodsVo
     */
    coverPic?: string;
    /**
     *
     * @type {Date}
     * @memberof GoodsVo
     */
    createTime?: Date;
    /**
     * 商品id
     * @type {number}
     * @memberof GoodsVo
     */
    goodsId: number;
    /**
     * 商品简介
     * @type {string}
     * @memberof GoodsVo
     */
    goodsIntro?: string;
    /**
     * 商品名称
     * @type {string}
     * @memberof GoodsVo
     */
    goodsName?: string;
    /**
     * 商品类型|可选:  <br>  export const <br> GOODS_TYPE_ENUM <br>
     * @type {number}
     * @memberof GoodsVo
     */
    goodsType: number;
    /**
     * 商品价格
     * @type {number}
     * @memberof GoodsVo
     */
    price?: number;
    /**
     *
     * @type {string}
     * @memberof GoodsVo
     */
    remark?: string;
    /**
     * 上架状态
     * @type {boolean}
     * @memberof GoodsVo
     */
    shelvesFlag?: boolean;
    /**
     *
     * @type {Date}
     * @memberof GoodsVo
     */
    updateTime?: Date;
}
