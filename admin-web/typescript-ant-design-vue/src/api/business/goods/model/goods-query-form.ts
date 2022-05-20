import {
  SortItemModel
} from '../../../base-model/sort-item-model';

/**
 *
 * @export
 * @interface GoodsQueryForm
 */
export interface GoodsQueryForm {
    /**
     * 商品分类
     * @type {number}
     * @memberof GoodsQueryForm
     */
    categoryId?: number;
    /**
     * 商品类型|可选:  <br>  export const <br> GOODS_TYPE_ENUM <br>
     * @type {number}
     * @memberof GoodsQueryForm
     */
    goodsType?: number;
    /**
     * 商品分组:  <br>  export const <br> GOODS_GROUP_TYPE_ENUM <br>
     * @type {number}
     * @memberof GoodsQueryForm
     */
    groupType?: number;
    /**
     * 页码(不能为空)
     * @type {number}
     * @memberof GoodsQueryForm
     */
    pageNum: number;
    /**
     * 每页数量(不能为空)
     * @type {number}
     * @memberof GoodsQueryForm
     */
    pageSize: number;
    /**
     * 搜索词
     * @type {string}
     * @memberof GoodsQueryForm
     */
    searchWord?: string;
    /**
     * 上架状态
     * @type {boolean}
     * @memberof GoodsQueryForm
     */
    shelvesFlag?: boolean | string;
    /**
     * 排序字段集合
     * @type {Array<SortItemDto>}
     * @memberof GoodsQueryForm
     */
    sortItemList?: Array<SortItemModel>;
}

