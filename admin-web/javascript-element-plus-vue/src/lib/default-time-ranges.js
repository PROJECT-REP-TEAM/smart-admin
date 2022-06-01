/*
 * @Description:
 * @Author: zhuoda
 * @Date: 2022-02-12
 * @LastEditTime: 2022-03-24 15:46:04
 * @LastEditors: qq:23983208
 */

import moment from 'moment';
export const defaultTimeRanges = {
  今日: [moment(), moment()],
  昨日: [moment().subtract(1, 'days'), moment().subtract(1, 'days')],
  本月: [moment().startOf('month'), moment().endOf('month')],
  上个月: [moment().subtract(1, 'months').startOf('month'), moment().subtract(1, 'months').endOf('month')],
  本年度: [moment().startOf('year'), moment().endOf('year')],
  上年度: [moment().subtract(1, 'years').startOf('year'), moment().subtract(1, 'years').endOf('year')],
};

// 不可跨月
export const defaultLimitMonth = {
  今日: [moment(), moment()],
  昨日: [moment().subtract(1, 'days'), moment().subtract(1, 'days')],
  本月: [moment().startOf('month'), moment().endOf('month')],
  上个月: [moment().subtract(1, 'months').startOf('month'), moment().subtract(1, 'months').endOf('month')],
  下个月: [moment().subtract(-1, 'months').startOf('month'), moment().subtract(-1, 'months').endOf('month')],
};

// 线索：今日，昨日，本月，上个月
export const defaultDaysLastMonth = {
  今日: [moment(), moment()],
  昨日: [moment().subtract(1, 'days'), moment().subtract(1, 'days')],
  本月: [moment().startOf('month'), moment().endOf('month')],
  上个月: [moment().subtract(1, 'months').startOf('month'), moment().subtract(1, 'months').endOf('month')],
};
