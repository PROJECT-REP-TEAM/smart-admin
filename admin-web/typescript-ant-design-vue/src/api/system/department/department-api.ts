/*
 * @Author: zhuoda
 * @Date: 2021-08-12 17:56:25
 * @LastEditTime: 2021-08-16 10:45:05
 * @LastEditors: zhuoda
 * @Description:
 * @FilePath: /smart-admin/src/api/system/department/department-api.ts
 */
import { ResponseModel } from '../../base-model/response-model';
import { DepartmentCreateDto } from './model/department-create-dto';
import { DepartmentTreeVo } from './model/department-tree-vo';
import { DepartmentVo } from './model/department-vo';
import { getRequest, postRequest } from '/@/lib/axios';
export const departmentApi = {
  /**
   * @description: 查询部门列表
   * @param {*}
   * @return {*}
   */
  queryAllDepartment: (): Promise<ResponseModel<DepartmentVo[]>> => {
    return getRequest<ResponseModel<Array<DepartmentVo>>>('/admin/department/listAll');
  },

  /**
   * @description: 查询部门树形列表
   * @param {*}
   * @return {*}
   */
  departmentTree: (): Promise<ResponseModel<DepartmentTreeVo[]>> => {
    return getRequest<ResponseModel<Array<DepartmentTreeVo>>>('/admin/department/treeList');
  },

  /**
   * @description: 获取校区列表 by zhuoda
   * @param {*}
   * @return {*}
   */
  querySchoolDepartmentList: (): Promise<ResponseModel<DepartmentVo[]>> => {
    return getRequest<ResponseModel<DepartmentVo[]>>('/admin/department/querySchoolList');
  },

  /**
   * @description: 添加部门 by zhuoda
   * @param {*}
   * @return {*}
   */
  addDepartment: (param: DepartmentCreateDto) => {
    return postRequest<ResponseModel<String>>('/admin/department/add', param);
  },
  /**
   * @description: 更新部门信息 by zhuoda
   * @param {*}
   * @return {*}
   */
  updateDepartment: (param: DepartmentCreateDto) => {
    return postRequest<ResponseModel<String>>('/admin/department/update', param);
  },
  /**
   * @description: 获取校区列表 by zhuoda
   * @param {*}
   * @return {*}
   */
  deleteDepartment: (deptId: number) => {
    return getRequest<ResponseModel<String>>(`/admin/department/delete/${deptId}`);
  },
};
