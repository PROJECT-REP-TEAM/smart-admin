/**
 * 
 * @export
 * @interface CategoryUpdateDto
 */
export interface CategoryUpdateForm {
    /**
     * 类目id
     * @type {number}
     * @memberof CategoryUpdateDto
     */
    categoryId?: number;
    /**
     * 类目名称
     * @type {string}
     * @memberof CategoryUpdateDto
     */
    categoryName: string;
    /**
     * 分类类型:  <br>  export const <br> CATEGORY_TYPE_ENUM <br>
     * @type {number}
     * @memberof CategoryUpdateDto
     */
    categoryType: number;
    /**
     * 禁用状态
     * @type {boolean}
     * @memberof CategoryUpdateDto
     */
    disabledFlag?: boolean;
    /**
     * 父级类目id|可选
     * @type {number}
     * @memberof CategoryUpdateDto
     */
    parentId?: number;
    /**
     * 备注|可选
     * @type {string}
     * @memberof CategoryUpdateDto
     */
    remark?: string;
    /**
     * 排序|可选
     * @type {number}
     * @memberof CategoryUpdateDto
     */
    sort?: number;
}
