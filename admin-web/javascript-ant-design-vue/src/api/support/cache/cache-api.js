/*
 * @Description:
 * @version:
 * @Author: zhuoda
 * @Date: 2021-08-17 23:32:36
 * @LastEditors: zhuoda
 * @LastEditTime: 2022-06-24
 */
import { postRequest, getRequest } from '/@/lib/axios';

export const cacheApi = {
  // 获取某个缓存的所有key @author 罗伊
  getKeys: (cacheName) => {
    return getRequest(`/support//cache/keys/${cacheName}`);
  },
  // 移除某个缓存 @author 罗伊
  remove: (cacheName) => {
    return getRequest(`/support/cache/remove/${cacheName}`);
  },
  // 获取所有缓存 @author 罗伊
  getAllCacheNames: () => {
    return getRequest('/support/cache/names');
  },
};
