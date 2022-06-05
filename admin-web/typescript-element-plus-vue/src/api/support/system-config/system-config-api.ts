/*
 * @Description:
 * @version:
 * @Author: zhuoda
 * @Date: 2021-08-17 23:32:36
 * @LastEditors: zhuoda
 * @LastEditTime: 2021-08-18 14:35:52
 */
import {ResponseModel} from '../../base-model/response-model';
import {postRequest, getRequest} from '/@/lib/axios';
import {PageResultModel} from "/@/api/base-model/page-result-model";
import {SystemConfigQueryForm} from "/@/api/support/system-config/model/system-config-query-form";
import {SystemConfigVo} from "/@/api/support/system-config/model/system-config-vo";
import {SystemConfigAddForm} from "/@/api/support/system-config/model/system-config-add-form";
import {SystemConfigUpdateForm} from "/@/api/support/system-config/model/system-config-update-form";

export const systemConfigApi = {
    // 分页查询 by zhuoda
    queryList: (param: SystemConfigQueryForm) => {
        return postRequest<ResponseModel<PageResultModel<SystemConfigVo>>>('/support/config/query', param);
    },
    // 添加配置参数 by zhuoda
    addSystemConfig: (param: SystemConfigAddForm) => {
        return postRequest<ResponseModel<String>>('/support/config/add', param);
    },
    // 修改配置参数 by zhuoda
    updateSystemConfig: (param: SystemConfigUpdateForm) => {
        return postRequest<ResponseModel<String>>('/support/config/update', param);
    },
    // 查询配置详情 by zhuoda
    queryByKey: (param: string) => {
        return getRequest<ResponseModel<String>>(`/support/config/queryByKey?configKey=${param}`);
    },

};
