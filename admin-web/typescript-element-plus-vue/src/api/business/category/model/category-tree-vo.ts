/**
 * 
 * @export
 * @interface CategoryTreeVo
 */
export interface CategoryTreeVo {
    /**
     * 类目层级全称
     * @type {string}
     * @memberof CategoryTreeVo
     */
    categoryFullName?: string;
    /**
     * 类目id
     * @type {number}
     * @memberof CategoryTreeVo
     */
    categoryId?: number;
    /**
     * 类目名称
     * @type {string}
     * @memberof CategoryTreeVo
     */
    categoryName?: string;
    /**
     * 子类
     * @type {Array<CategoryTreeVo>}
     * @memberof CategoryTreeVo
     */
    children?: Array<CategoryTreeVo>;
    /**
     * 类目名称
     * @type {string}
     * @memberof CategoryTreeVo
     */
    label?: string;
    /**
     * 父级id
     * @type {number}
     * @memberof CategoryTreeVo
     */
    parentId?: number;
    /**
     * 类目id
     * @type {number}
     * @memberof CategoryTreeVo
     */
    value?: number;
}
