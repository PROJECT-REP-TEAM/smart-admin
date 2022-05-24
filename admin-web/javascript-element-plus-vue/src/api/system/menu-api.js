/*
 * @Author: zhuoda
 * @Date: 2021-08-11 22:15:04
 * @LastEditTime: 2021-09-01 20:21:29
 * @LastEditors: zhuoda
 * @Description:
 * @FilePath: /smart-admin/src/api/system/menu/menu-api.ts
 */
import { getRequest, postRequest } from '/@/lib/axios';

export const menuApi = {
  /**
   * 添加菜单
   */
  addMenu: (param) => {
    return postRequest('/admin/menu/add', param);
  },

  /**
   * 更新菜单
   */
  updateMenu: (param) => {
    return postRequest('/admin/menu/update', param);
  },

  /**
   * 批量删除菜单
   */
  batchDeleteMenu: (menuIdList) => {
    return getRequest(`/admin/menu/batchDelete?menuIdList=${menuIdList}`);
  },

  /**
   * 查询所有菜单列表
   */
  queryMenu: () => {
    return getRequest('/admin/menu/query');
  },

  /**
   * 查询菜单树
   */
  queryMenuTree: (onlyMenu) => {
    return getRequest(`/admin/menu/tree?onlyMenu=${onlyMenu}`);
  },

  /**
   * 获取所有请求路径
   */
  getAuthUrl: () => {
    return getRequest('/admin/menu/auth/url');
  },
};
