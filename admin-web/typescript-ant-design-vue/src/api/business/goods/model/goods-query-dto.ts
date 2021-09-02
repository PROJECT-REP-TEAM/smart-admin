import {
  SortItemModel
} from '../../../base-model/sort-item-model';

/**
 *
 * @export
 * @interface GoodsQueryDto
 */
export interface GoodsQueryDto {
    /**
     * 商品分类
     * @type {number}
     * @memberof GoodsQueryDto
     */
    categoryId?: number;
    /**
     * 商品类型|可选:  <br>  export const <br> GOODS_TYPE_ENUM <br>
     * @type {number}
     * @memberof GoodsQueryDto
     */
    goodsType?: number;
    /**
     * 商品分组:  <br>  export const <br> GOODS_GROUP_TYPE_ENUM <br>
     * @type {number}
     * @memberof GoodsQueryDto
     */
    groupType?: number;
    /**
     * 页码(不能为空)
     * @type {number}
     * @memberof GoodsQueryDto
     */
    pageNum: number;
    /**
     * 每页数量(不能为空)
     * @type {number}
     * @memberof GoodsQueryDto
     */
    pageSize: number;
    /**
     * 搜索词
     * @type {string}
     * @memberof GoodsQueryDto
     */
    searchWord?: string;
    /**
     * 上架状态
     * @type {boolean}
     * @memberof GoodsQueryDto
     */
    shelvesFlag?: boolean | string;
    /**
     * 排序字段集合
     * @type {Array<SortItemDto>}
     * @memberof GoodsQueryDto
     */
    sortItemList?: Array<SortItemModel>;
}

