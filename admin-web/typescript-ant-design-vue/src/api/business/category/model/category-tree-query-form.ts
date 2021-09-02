/**
 * 
 * @export
 * @interface CategoryTreeQueryDto
 */
export interface CategoryTreeQueryForm {
    /**
     * 分类类型|可选:  <br>  export const <br> CATEGORY_TYPE_ENUM  <br>
     * @type {number}
     * @memberof CategoryTreeQueryDto
     */
    categoryType: number;
    /**
     * 父级类目id|可选
     * @type {number}
     * @memberof CategoryTreeQueryDto
     */
    parentId?: number;
}