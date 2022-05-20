/*
 * @Description:
 * @version:
 * @Author: lidoudou
 * @Date: 2021-08-27 17:52:09
 * @LastEditors: lidoudou
 * @LastEditTime: 2021-08-27 17:53:44
 */

import { PageResultModel } from '../../base-model/page-result-model';
import { ResponseModel } from '../../base-model/response-model';
import { DataTracerQueryForm } from './model/data-tracer-query-form';
import { DataTracerVo } from './model/data-tracer-vo';
import { postRequest } from '/@/lib/axios';

export const dataTracerApi = {
  //分页查询业务操作日志 - by listen
  queryDataTracerLogList: (param: DataTracerQueryForm) => {
    return postRequest<ResponseModel<PageResultModel<DataTracerVo>>>('/support/data/tracer/log/query', param);
  },
};
