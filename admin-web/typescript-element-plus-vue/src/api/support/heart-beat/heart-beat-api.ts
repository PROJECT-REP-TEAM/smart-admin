/*
 * @Description:
 * @version:
 * @Author: zhuoda
 * @Date: 2021-08-17 23:32:36
 * @LastEditors: zhuoda
 * @LastEditTime: 2021-08-18 14:35:52
 */
import { ResponseModel } from '../../base-model/response-model';
import { postRequest } from '/@/lib/axios';
import {PageResultModel} from "/@/api/base-model/page-result-model";
import {HeartBeatVo} from "/@/api/support/heart-beat/model/heart-beat-vo";
import {HeartBeatQueryForm} from "/@/api/support/heart-beat/model/heart-beat-query-form";

export const heartBeatApi = {
  // 分页查询 by zhuoda
  queryList: (param: HeartBeatQueryForm) => {
    return postRequest<ResponseModel<PageResultModel<HeartBeatVo>>>('/support/heartBeat/query', param);
  },
};
