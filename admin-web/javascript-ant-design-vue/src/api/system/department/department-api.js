/*
 * author：zhuoda: zhuoda
 * @Date: 2021-08-12 17:56:25
 * @LastEditTime: 2022-06-02
 * @LastEditors: zhuoda
 * @Description:
 */
import { getRequest, postRequest } from '/@/lib/axios';

export const departmentApi = {
  /**
   * @description: 查询部门列表 author：zhuoda
   * @param {*}
   * @return {*}
   */
  queryAllDepartment: () => {
    return getRequest('/department/listAll');
  },

  /**
   * @description: 查询部门树形列表 author：zhuoda
   * @param {*}
   * @return {*}
   */
  departmentTree: () => {
    return getRequest('/department/treeList');
  },

  /**
   * @description: 获取校区列表 author：zhuoda
   * @param {*}
   * @return {*}
   */
  querySchoolDepartmentList: () => {
    return getRequest('/department/querySchoolList');
  },

  /**
   * @description: 添加部门 author：zhuoda
   * @param {*}
   * @return {*}
   */
  addDepartment: (param) => {
    return postRequest('/department/add', param);
  },
  /**
   * @description: 更新部门信息 author：zhuoda
   * @param {*}
   * @return {*}
   */
  updateDepartment: (param) => {
    return postRequest('/department/update', param);
  },
  /**
   * @description: 获取校区列表 author：zhuoda
   * @param {*}
   * @return {*}
   */
  deleteDepartment: (deptId) => {
    return getRequest(`/department/delete/${deptId}`);
  },
};
