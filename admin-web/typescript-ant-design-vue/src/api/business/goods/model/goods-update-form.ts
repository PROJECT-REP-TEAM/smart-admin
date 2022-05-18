/**
 *
 * @export
 * @interface GoodsUpdateDto
 */
import {GoodsAddForm} from "/@/api/business/goods/model/goods-add-form";

export interface GoodsUpdateForm extends GoodsAddForm{
    /**
     * 商品id
     * @type {number}
     * @memberof GoodsUpdateDto
     */
    goodsId?: number;

}
