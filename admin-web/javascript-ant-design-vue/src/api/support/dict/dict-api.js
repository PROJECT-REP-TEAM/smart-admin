/*
 * @Description:
 * @version:
 * @Author: zhuoda
 * @Date: 2021-08-17 23:32:36
 * @LastEditors: zhuoda
 * @LastEditTime: 2022-06-24
 */
import { postRequest, getRequest } from '/@/lib/axios';

export const dictApi = {
  // 分页查询数据字典KEY - @author yandy
  keyQuery: (param) => {
    return postRequest('/support/dict/key/query', param);
  },
  // 分页查询数据字典value - @author yandy
  valueQuery: (param) => {
    return postRequest('/support/dict/value/query', param);
  },
  // 数据字典KEY-添加- @author yandy
  keyAdd: (param) => {
    return postRequest('/support/dict/key/add', param);
  },
  // 分页查询数据字典value - @author yandy
  valueAdd: (param) => {
    return postRequest('/support/dict/value/add', param);
  },
  // 数据字典key-更新- @author yandy
  keyEdit: (param) => {
    return postRequest('/support/dict/key/edit', param);
  },
  // 数据字典Value-更新- @author yandy
  valueEdit: (param) => {
    return postRequest('/support/dict/value/edit', param);
  },
  // 数据字典key-删除- @author yandy
  keyDelete: (keyIdList) => {
    return postRequest('/support/dict/key/delete', keyIdList);
  },
  // 数据字典Value-删除- @author yandy
  valueDelete: (valueIdList) => {
    return postRequest('/support/dict/value/delete', valueIdList);
  },
  // 缓存刷新- @author yandy
  cacheRefresh: () => {
    return getRequest('/support/dict/cache/refresh');
  },
  // 数据字典-值列表- @author yandy
  valueList: (keyCode) => {
    return getRequest(`/dict/value/list/${keyCode}`);
  },
};
