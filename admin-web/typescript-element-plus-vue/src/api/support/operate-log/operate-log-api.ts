/*
 * @Description:
 * @version:
 * @Author: zhuoda
 * @Date: 2021-08-17 23:32:36
 * @LastEditors: zhuoda
 * @LastEditTime: 2021-08-18 14:35:52
 */
import { ResponseModel } from '../../base-model/response-model';
import { postRequest,getRequest } from '/@/lib/axios';
import {PageResultModel} from "/@/api/base-model/page-result-model";
import {OperateLogQueryForm} from "/@/api/support/operate-log/model/operate-log-query-form";
import {OperateLogVo} from "/@/api/support/operate-log/model/operate-log-vo";

export const operateLogApi = {
  // 分页查询 by zhuoda
  queryList: (param: OperateLogQueryForm) => {
    return postRequest<ResponseModel<PageResultModel<OperateLogVo>>>('/support/operateLog/page/query', param);
  },
  // 详情 by zhuoda
  detail: (id: Number) => {
    return getRequest<ResponseModel<OperateLogVo>>(`/support/operateLog/detail/${id}`);
  },
};
