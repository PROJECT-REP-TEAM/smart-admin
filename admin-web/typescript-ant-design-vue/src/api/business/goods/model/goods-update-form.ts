/**
 *
 * @export
 * @interface GoodsUpdateDto
 */
export interface GoodsUpdateForm {
    /**
     * 商品分类
     * @type {number}
     * @memberof GoodsUpdateDto
     */
    categoryId?: number;
    /**
     * 商品封面
     * @type {string}
     * @memberof GoodsUpdateDto
     */
    coverPic?: string;
    /**
     * 商品id
     * @type {number}
     * @memberof GoodsUpdateDto
     */
    goodsId?: number;
    /**
     * 商品简介
     * @type {string}
     * @memberof GoodsUpdateDto
     */
    goodsIntro?: string;
    /**
     * 商品名称
     * @type {string}
     * @memberof GoodsUpdateDto
     */
    goodsName?: string;
    /**
     * 商品类型|可选:  <br>  export const <br> GOODS_TYPE_ENUM <br>
     * @type {number}
     * @memberof GoodsUpdateDto
     */
    goodsType?: number;
    /**
     * 商品分组:  <br>  export const <br> GOODS_GROUP_TYPE_ENUM  <br>
     * @type {number}
     * @memberof GoodsUpdateDto
     */
    groupType?: number;
    /**
     * 商品价格
     * @type {number}
     * @memberof GoodsUpdateDto
     */
    price?: number;
    /**
     * 上架状态
     * @type {boolean}
     * @memberof GoodsUpdateDto
     */
    shelvesFlag?: boolean;
    /**
     * 第三方商品id|可选
     * @type {number}
     * @memberof GoodsUpdateDto
     */
    thirdGoodsId?: number;
    /**
     * 备注|可选
     * @type {string}
     * @memberof GoodsUpdateDto
     */
     remark?: string;
}
