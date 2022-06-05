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
import {DictKeyQueryForm} from "/@/api/support/dict/model/dict-key-query-form";
import {DictValueQueryForm} from "/@/api/support/dict/model/dict-value-query-form";
import {DictKeyVo} from "/@/api/support/dict/model/dict-key-vo";
import {DictValueVo} from "/@/api/support/dict/model/dict-value-vo";
import {DictKeyAddForm} from "/@/api/support/dict/model/dict-key-add-form";
import {DictValueAddForm} from "/@/api/support/dict/model/dict-value-add-form";
import {DictKeyUpdateForm} from "/@/api/support/dict/model/dict-key-update-form";
import {DictValueUpdateForm} from "/@/api/support/dict/model/dict-value-update-form";

export const dictApi = {
    // 分页查询数据字典KEY - @author yandy
    keyQuery: (param: DictKeyQueryForm) => {
        return postRequest<ResponseModel<PageResultModel<DictKeyVo>>>('/support/dict/key/query', param);
    },
    // 分页查询数据字典value - @author yandy
    valueQuery: (param: DictValueQueryForm) => {
        return postRequest<ResponseModel<PageResultModel<DictValueVo>>>('/support/dict/value/query', param);
    },
    // 数据字典KEY-添加- @author yandy
    keyAdd: (param: DictKeyAddForm) => {
        return postRequest<ResponseModel<String>>('/support/dict/key/add', param);
    },
    // 分页查询数据字典value - @author yandy
    valueAdd: (param: DictValueAddForm) => {
        return postRequest<ResponseModel<String>>('/support/dict/value/add', param);
    },
    // 数据字典key-更新- @author yandy
    keyEdit: (param: DictKeyUpdateForm) => {
        return postRequest<ResponseModel<String>>('/support/dict/key/edit', param);
    },
    // 数据字典Value-更新- @author yandy
    valueEdit: (param: DictValueUpdateForm) => {
        return postRequest<ResponseModel<String>>('/support/dict/value/edit', param);
    },
    // 数据字典key-删除- @author yandy
    keyDelete: (keyIdList: Array<number>) => {
        return postRequest<ResponseModel<String>>('/support/dict/key/delete', keyIdList);
    },
    // 数据字典Value-删除- @author yandy
    valueDelete: (valueIdList: Array<number>) => {
        return postRequest<ResponseModel<String>>('/support/dict/value/delete', valueIdList);
    },
    // 缓存刷新- @author yandy
    cacheRefresh: () => {
        return getRequest<ResponseModel<String>>('/support/dict/cache/refresh');
    },
    // 数据字典-值列表- @author yandy
    valueList: (keyCode: string) => {
        return getRequest<ResponseModel<Array<DictValueVo>>>(`/dict/value/list/${keyCode}`);
    },

};
