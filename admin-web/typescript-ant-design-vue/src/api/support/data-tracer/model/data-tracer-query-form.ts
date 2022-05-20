/*
 * @Description:
 * @version:
 * @Author: lidoudou
 * @Date: 2021-08-27 17:52:02
 * @LastEditors: lidoudou
 * @LastEditTime: 2021-08-27 18:01:22
 */

import { SortItemModel } from '/@/api/base-model/sort-item-model';

export interface DataTracerQueryForm {
  /**
   * 业务id
   */
  businessId?: number;
  /**
   * :  <br>  export const <br> DATA_TRACER_BUSINESS_TYPE_ENUM = <BR> {<br>&nbsp;&nbsp;CLUE_USER:{<br>&nbsp;&nbsp;&nbsp;&nbsp;value:1,<br>&nbsp;&nbsp;&nbsp;&nbsp;desc:\'线索\'<br>&nbsp;&nbsp;}<br>} <br>
   */
  businessType?: number;
  /**
   * 页码(不能为空)
   */
  pageNum: number;
  /**
   * 每页数量(不能为空)
   */
  pageSize: number;
  /**
   * 是否查询总条数
   */
  searchCount?: boolean;
  /**
   * 排序字段集合
   */
  sortItemList?: Array<SortItemModel>;
}
