/*
 * @Author: zhuoda
 * @Date: 2021-08-24 17:21:35
 * @LastEditTime: 2021-08-24 17:24:31
 * @LastEditors: zhuoda
 * @Description:
 * @FilePath: /smart-admin/src/api/system/home/home-api.ts
 */
import { ResponseModel } from '../../base-model/response-model';
import { HomeAmountStatisticsVo } from './model/home-amount-statistics-vo';
import { HomeWaitHandleVo } from './model/home-wait-handle-vo';
import { getRequest, postRequest } from '/@/lib/axios';
export const homeApi = {
  /**
   * @description: 首页-金额统计（业绩、收款、订单数等） by zhuoda
   * @param {*}
   * @return {*}
   */
  homeAmountStatistics: () => {
    return getRequest<ResponseModel<HomeAmountStatisticsVo>>('/admin/home/amount/statistics');
  },
  /**
   * @description: 首页-待办信息 by zhuoda
   * @param {*}
   * @return {*}
   */
  homeWaitHandle: () => {
    return getRequest<ResponseModel<HomeWaitHandleVo>>('/admin/home/wait/handle');
  },
};
