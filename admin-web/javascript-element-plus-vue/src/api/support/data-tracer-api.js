/*
 * @Description:
 * @version:
 * @Author: lidoudou
 * @Date: 2021-08-27 17:52:09
 * @LastEditors: lidoudou
 * @LastEditTime: 2021-08-27 17:53:44
 */

import { postRequest } from '/@/lib/axios';

export const dataTracerApi = {
  //分页查询业务操作日志 - by listen
  queryDataTracerLogList: (param) => {
    return postRequest('/support/data/tracer/log/query', param);
  },
};
