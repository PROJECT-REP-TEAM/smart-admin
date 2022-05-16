/*
 * @Description:
 * @Author: zhuoda
 * @Date: 2021-08-12 16:46:21
 * @LastEditTime: 2021-08-12 16:47:04
 * @LastEditors: zhuoda
 */
import { ResponseModel } from '../../base-model/response-model';
import { CategoryAddForm } from './model/category-add-form';
import { CategoryTreeQueryForm } from './model/category-tree-query-form';
import { CategoryTreeVo } from './model/category-tree-vo';
import { CategoryUpdateForm } from './model/category-update-form';
import { getRequest, postRequest } from '/@/lib/axios';

export const categoryApi = {
  // 添加类目 by zhuoda
  addCategory: (param: CategoryAddForm) => {
    return postRequest<ResponseModel<string>>('/admin/category/add', param);
  },
  // GET
  // 删除类目 by zhuoda
  deleteCategoryById: (categoryId: number) => {
    return getRequest<ResponseModel<string>>(`/admin/category/del/${categoryId}`);
  },
  // 查询类目层级树 by zhuoda
  queryCategoryTree: (param: CategoryTreeQueryForm) => {
    return postRequest<ResponseModel<CategoryTreeVo[]>>('/admin/category/tree', param);
  },
  // 更新类目 by zhuoda
  updateCategory: (param: CategoryUpdateForm) => {
    return postRequest<ResponseModel<string>>('/admin/category/update', param);
  },
  // 查询类目详情 by zhuoda
  getCategory: (categoryId: number) => {
    // POST /admin/clue/user/track/add
    return getRequest<ResponseModel<string>>(`/admin/category/${categoryId}`);
  },
};
