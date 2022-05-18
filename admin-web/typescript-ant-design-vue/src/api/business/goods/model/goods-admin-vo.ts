/*
 * @Author: zhuoda
 * @Date: 2021-08-12 18:10:22
 * @LastEditTime: 2021-09-01 21:25:35
 * @LastEditors: zhuoda
 * @Description:
 * @FilePath: /smart-admin/src/api/business/goods/model/goods-admin-vo.ts
 */
/**
 *
 * @export
 * @interface GoodsAdminVo
 */
export interface GoodsAdminVo {
    /**
     * 商品分类
     * @type {number}
     * @memberof GoodsAdminVo
     */
    categoryId?: number;
    /**
     * 商品封面
     * @type {string}
     * @memberof GoodsAdminVo
     */
    coverPic?: string;
    /**
     *
     * @type {Date}
     * @memberof GoodsAdminVo
     */
    createTime?: Date;
    /**
     * 商品id
     * @type {number}
     * @memberof GoodsAdminVo
     */
    goodsId: number;
    /**
     * 商品简介
     * @type {string}
     * @memberof GoodsAdminVo
     */
    goodsIntro?: string;
    /**
     * 商品名称
     * @type {string}
     * @memberof GoodsAdminVo
     */
    goodsName?: string;
    /**
     * 商品类型|可选:  <br>  export const <br> GOODS_TYPE_ENUM <br>
     * @type {number}
     * @memberof GoodsAdminVo
     */
    goodsType: number;
    /**
     * 商品价格
     * @type {number}
     * @memberof GoodsAdminVo
     */
    price?: number;
    /**
     *
     * @type {string}
     * @memberof GoodsAdminVo
     */
    remark?: string;
    /**
     * 上架状态
     * @type {boolean}
     * @memberof GoodsAdminVo
     */
    shelvesFlag?: boolean;
    /**
     *
     * @type {Date}
     * @memberof GoodsAdminVo
     */
    updateTime?: Date;
}
