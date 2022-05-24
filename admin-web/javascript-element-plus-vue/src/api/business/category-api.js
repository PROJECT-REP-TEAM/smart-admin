/*
 * @Description:
 * @Author: zhuoda
 * @Date: 2021-08-12 16:46:21
 * @LastEditTime: 2021-08-12 16:47:04
 * @LastEditors: zhuoda
 */
import { getRequest, postRequest } from '/@/lib/axios';

export const categoryApi = {
  // 添加类目 by zhuoda
  addCategory: (param) => {
    return postRequest('/admin/category/add', param);
  },
  // GET
  // 删除类目 by zhuoda
  deleteCategoryById: (categoryId) => {
    return getRequest(`/admin/category/del/${categoryId}`);
  },
  // 查询类目层级树 by zhuoda
  queryCategoryTree: (param) => {
    return postRequest('/admin/category/tree', param);
  },
  // 更新类目 by zhuoda
  updateCategory: (param) => {
    return postRequest('/admin/category/update', param);
  },
  // 查询类目详情 by zhuoda
  getCategory: (categoryId) => {
    // POST /admin/clue/user/track/add
    return getRequest(`/admin/category/${categoryId}`);
  },
};
