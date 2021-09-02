/*
 * @Author: zhuoda
 * @Date: 2021-08-16 15:53:46
 * @LastEditTime: 2021-08-30 15:18:18
 * @LastEditors: zhuoda
 * @Description:
 * @FilePath: /smart-admin/src/api/system/role/role-api.ts
 */
import { PageResultModel } from '../../base-model/page-result-model';
import { ResponseModel } from '../../base-model/response-model';
import { EmployeeVo } from '../employee/model/employee-vo';
import { DataScopeAndViewTypeVo } from './model/data-scope-and-view-type-vo';
import { DataScopeBatchSetRoleDto } from './model/data-scope-batch-set-role-dto';
import { DataScopeSelectVo } from './model/data-scope-select-vo';
import { RoleAddDto } from './model/role-add-dto';
import { RoleEmployeeBatchDto } from './model/role-employee-batch-dto';
import { RoleQueryDto } from './model/role-query-dto';
import { RoleUpdateDto } from './model/role-update-dto';
import { RoleVo } from './model/role-vo';
import { getRequest, postRequest } from '/@/lib/axios';
export const roleApi = {
  /**
   * @description: 获取所有角色
   * @param {*}
   * @return {*}
   */
  queryAll: (): Promise<ResponseModel<RoleVo[]>> => {
    return getRequest<ResponseModel<RoleVo[]>>('role/getAll');
  },
  /**
   * @description:添加角色
   * @param {*}
   * @return {*}
   */
  addRole: (data: RoleAddDto) => {
    return postRequest<ResponseModel<string>>('role/add', data);
  },
  /**
   * @description:更新角色
   * @param {*}
   * @return {*}
   */
  updateRole: (data: RoleUpdateDto) => {
    return postRequest<ResponseModel<string>>('role/update', data);
  },
  /**
   * @description: 删除角色
   * @param {number} roleId
   * @return {*}
   */
  deleteRole: (roleId: number) => {
    return getRequest<ResponseModel<string>>(`role/delete/${roleId}`);
  },
  /**
   * @description: 批量设置某角色数据范围
   * @param {DataScopeBatchSetRoleDto} data
   * @return {*}
   */
  updateDataScope: (data: DataScopeBatchSetRoleDto) => {
    return postRequest<ResponseModel<string>>('/dataScope/batchSet', data);
  },
  /**
   * @description: 获取当前系统所配置的所有数据范围
   * @param {*}
   * @return {*}
   */
  getDataScopeList: () => {
    return getRequest<ResponseModel<DataScopeAndViewTypeVo[]>>('/dataScope/list');
  },
  /**
   * @description: 获取某角色所设置的数据范围
   * @param {number} roleId
   * @return {*}
   */
  getDataScopeByRoleId: (roleId: number) => {
    return getRequest<ResponseModel<DataScopeSelectVo[]>>(`/dataScope/listByRole/${roleId}`);
  },
  /**
   * @description: 获取角色成员-员工列表
   * @param {*}
   * @return {*}
   */
  queryRoleEmployee: (params: RoleQueryDto) => {
    return postRequest<ResponseModel<PageResultModel<EmployeeVo>>>('/role/listEmployee', params);
  },
  /**
   * @description: 从角色成员列表中移除员工
   * @param {number} employeeId
   * @param {number} roleId
   * @return {*}
   */
  deleteEmployeeRole: (employeeId: number, roleId: number) => {
    return getRequest<ResponseModel<string>>('/role/removeEmployee?employeeId=' + employeeId + '&roleId=' + roleId);
  },
  /**
   * @description: 从角色成员列表中批量移除员工
   * @param {RoleEmployeeBatchDto} data
   * @return {*}
   */
  deleteEmployeeList: (data: RoleEmployeeBatchDto) => {
    return postRequest<ResponseModel<string>>('/role/removeEmployeeList', data);
  },
  /**
   * @description: 根据角色id获取角色员工列表(无分页)
   * @param {*}
   * @return {*}
   */
  getRoleAllEmployee: (roleId: number) => {
    return getRequest<ResponseModel<EmployeeVo[]>>(`role/listAllEmployee/${roleId}`);
  },
  /**
   * @description: 角色成员列表中批量添加员工
   * @param {RoleEmployeeBatchDto} data
   * @return {*}
   */
  addRoleEmployeeList: (data: RoleEmployeeBatchDto) => {
    return postRequest<ResponseModel<string>>('/role/addEmployeeList', data);
  },
};
