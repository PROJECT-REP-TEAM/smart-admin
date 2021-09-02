/**
 * 
 * @export
 * @interface CategoryVo
 */
export interface CategoryVo {
    /**
     * 类目id
     * @type {number}
     * @memberof CategoryVo
     */
    categoryId?: number;
    /**
     * 类目名称
     * @type {string}
     * @memberof CategoryVo
     */
    categoryName: string;
    /**
     * 分类类型:  <br>  export const <br> CATEGORY_TYPE_ENUM<br>
     * @type {number}
     * @memberof CategoryVo
     */
    categoryType: number;
    /**
     * 
     * @type {Date}
     * @memberof CategoryVo
     */
    createTime?: Date;
    /**
     * 禁用状态
     * @type {boolean}
     * @memberof CategoryVo
     */
    disabledFlag?: boolean;
    /**
     * 父级类目id|可选
     * @type {number}
     * @memberof CategoryVo
     */
    parentId?: number;
    /**
     * 备注|可选
     * @type {string}
     * @memberof CategoryVo
     */
    remark?: string;
    /**
     * 排序|可选
     * @type {number}
     * @memberof CategoryVo
     */
    sort?: number;
    /**
     * 
     * @type {Date}
     * @memberof CategoryVo
     */
    updateTime?: Date;
}
