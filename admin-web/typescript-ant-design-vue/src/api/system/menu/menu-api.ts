/*
 * @Author: zhuoda
 * @Date: 2021-08-11 22:15:04
 * @LastEditTime: 2021-09-01 20:21:29
 * @LastEditors: zhuoda
 * @Description:
 * @FilePath: /smart-admin/src/api/system/menu/menu-api.ts
 */
import { getRequest, postRequest } from '/@/lib/axios';
import { MenuAddForm } from '/@/api/system/menu/model/menu-add-form';
import { ResponseModel } from '/@/api/base-model/response-model';
import { MenuUpdateForm } from '/@/api/system/menu/model/menu-update-form';
import { MenuVo } from '/@/api/system/menu/model/menu-vo';
import { MenuTreeVo } from '/@/api/system/menu/model/menu-tree-vo';
import { RequestUrlVo } from './model/request-url-vo';

export const menuApi = {
  /**
   * 添加菜单
   */
  addMenu: (param: MenuAddForm) => {
    return postRequest<ResponseModel<String>>('/menu/add', param);
  },

  /**
   * 更新菜单
   */
  updateMenu: (param: MenuUpdateForm) => {
    return postRequest<ResponseModel<String>>('/menu/update', param);
  },

  /**
   * 批量删除菜单
   */
  batchDeleteMenu: (menuIdList: Array<number | undefined>) => {
    return getRequest<ResponseModel<String>>(`/menu/batchDelete?menuIdList=${menuIdList}`);
  },

  /**
   * 查询所有菜单列表
   */
  queryMenu: (): Promise<ResponseModel<MenuVo[]>> => {
    return getRequest<ResponseModel<MenuVo[]>>('/menu/query');
  },

  /**
   * 查询菜单树
   */
  queryMenuTree: (onlyMenu?: boolean): Promise<ResponseModel<MenuTreeVo[]>> => {
    return getRequest<ResponseModel<MenuTreeVo[]>>(`/menu/tree?onlyMenu=${onlyMenu}`);
  },

  /**
   * 获取所有请求路径
   */
  getAllUrl: () => {
    return getRequest<ResponseModel<RequestUrlVo[]>>('/menu/getAllUrl');
  },
};
