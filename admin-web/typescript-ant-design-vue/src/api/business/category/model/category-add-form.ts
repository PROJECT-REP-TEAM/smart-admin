/**
 *
 * @export
 * @interface CategoryAddDto
 */
export interface CategoryAddForm {
  /**
   * 类目名称
   * @type {string}
   * @memberof CategoryAddDto
   */
  categoryName: string;
  /**
   * 分类类型:  <br>  export const <br> CATEGORY_TYPE_ENUM = <BR>  <br>
   * @type {number}
   * @memberof CategoryAddDto
   */
  categoryType: number;
  /**
   * 禁用状态
   * @type {boolean}
   * @memberof CategoryAddDto
   */
  disabledFlag?: boolean;
  /**
   * 父级类目id|可选
   * @type {number}
   * @memberof CategoryAddDto
   */
  parentId?: number;
  /**
   * 备注|可选
   * @type {string}
   * @memberof CategoryAddDto
   */
  remark?: string;
  /**
   * 排序|可选
   * @type {number}
   * @memberof CategoryAddDto
   */
  sort?: number;
}
