/**
 *
 * @export
 * @interface GoodsAddDto
 */
export interface GoodsAddForm {
    /**
     * 商品分类
     * @type {number}
     * @memberof GoodsAddDto
     */
    categoryId?: number;
    /**
     * 商品封面
     * @type {string}
     * @memberof GoodsAddDto
     */
    coverPic?: string;
    /**
     * 商品简介
     * @type {string}
     * @memberof GoodsAddDto
     */
    goodsIntro?: string;
    /**
     * 商品名称
     * @type {string}
     * @memberof GoodsAddDto
     */
    goodsName?: string;
    /**
     * 商品类型|可选:  <br>  export const <br> GOODS_TYPE_ENUM<br>
     * @type {number}
     * @memberof GoodsAddDto
     */
    goodsType?: number;
    /**
     * 商品价格
     * @type {number}
     * @memberof GoodsAddDto
     */
    price?: number;
    /**
     * 上架状态
     * @type {boolean}
     * @memberof GoodsAddDto
     */
    shelvesFlag?: boolean;

    /**
     * 备注|可选
     * @type {string}
     * @memberof GoodsUpdateDto
     */
     remark?: string;
}
