/*
 * @Author: zhuoda
 * @Date: 2021-08-11 22:15:04
 * @LastEditTime: 2022-06-16
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
    return postRequest('/menu/add', param);
  },

  /**
   * 更新菜单
   */
  updateMenu: (param) => {
    return postRequest('/menu/update', param);
  },

  /**
   * 批量删除菜单
   */
  batchDeleteMenu: (menuIdList) => {
    return getRequest(`/menu/batchDelete?menuIdList=${menuIdList}`);
  },

  /**
   * 查询所有菜单列表
   */
  queryMenu: () => {
    return getRequest('/menu/query');
  },

  /**
   * 查询菜单树
   */
  queryMenuTree: (onlyMenu) => {
    return getRequest(`/menu/tree?onlyMenu=${onlyMenu}`);
  },


  /**
 * 获取所有请求路径
 */
  getAuthUrl: () => {
    return getRequest('/menu/auth/url');
  },
};
