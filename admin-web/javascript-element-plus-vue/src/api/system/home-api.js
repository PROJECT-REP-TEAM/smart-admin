/*
 * @Author: zhuoda
 * @Date: 2021-08-24 17:21:35
 * @LastEditTime: 2021-08-24 17:24:31
 * @LastEditors: zhuoda
 * @Description:
 * @FilePath: /smart-admin/src/api/system/home/home-api.ts
 */
import { getRequest } from '/@/lib/axios';

export const homeApi = {
  /**
   * @description: 首页-金额统计（业绩、收款、订单数等） by zhuoda
   * @param {*}
   * @return {*}
   */
  homeAmountStatistics: () => {
    return getRequest('/admin/home/amount/statistics');
  },
  /**
   * @description: 首页-待办信息 by zhuoda
   * @param {*}
   * @return {*}
   */
  homeWaitHandle: () => {
    return getRequest('/admin/home/wait/handle');
  },
};
