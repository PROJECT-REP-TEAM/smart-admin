/*
 * @Author: zhuoda
 * @Date: 2021-08-12 17:56:25
 * @LastEditTime: 2021-08-16 10:45:05
 * @LastEditors: zhuoda
 * @Description:
 * @FilePath: /smart-admin/src/api/system/department/department-api.ts
 */
import { getRequest, postRequest } from '/@/lib/axios';

export const departmentApi = {
  /**
   * @description: 查询部门列表
   * @param {*}
   * @return {*}
   */
  queryAllDepartment: () => {
    return getRequest('/admin/department/listAll');
  },

  /**
   * @description: 查询部门树形列表
   * @param {*}
   * @return {*}
   */
  departmentTree: () => {
    return getRequest('/admin/department/treeList');
  },

  /**
   * @description: 获取校区列表 by zhuoda
   * @param {*}
   * @return {*}
   */
  querySchoolDepartmentList: () => {
    return getRequest('/admin/department/querySchoolList');
  },

  /**
   * @description: 添加部门 by zhuoda
   * @param {*}
   * @return {*}
   */
  addDepartment: (param) => {
    return postRequest('/admin/department/add', param);
  },
  /**
   * @description: 更新部门信息 by zhuoda
   * @param {*}
   * @return {*}
   */
  updateDepartment: (param) => {
    return postRequest('/admin/department/update', param);
  },
  /**
   * @description: 获取校区列表 by zhuoda
   * @param {*}
   * @return {*}
   */
  deleteDepartment: (deptId) => {
    return getRequest(`/admin/department/delete/${deptId}`);
  },
};
