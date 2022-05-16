/*
 * @Description: 员工api
 * @Author: zhuoda
 * @Date: 2021-08-12 18:00:56
 * @LastEditTime: 2021-08-25 11:24:51
 * @LastEditors: zhuoda
 */

import { PageResultModel } from '../../base-model/page-result-model';
import { ResponseModel } from '../../base-model/response-model';
import { EmployeeAddDto } from './model/employee-add-dto';
import { EmployeeDepartmentUpdateDto } from './model/employee-department-update-dto';
import { EmployeeQueryDto } from './model/employee-query-dto';
import { EmployeeUpdateDto } from './model/employee-update-dto';
import { EmployeeVo } from './model/employee-vo';
import { getRequest, postRequest } from '/@/lib/axios';
export const employeeApi = {
  /**
   * @description: 查询所有员工 by zhuoda
   * @param {*}
   * @return {*}
   */
  queryAll: (): Promise<ResponseModel<EmployeeVo[]>> => {
    return getRequest<ResponseModel<EmployeeVo[]>>('/admin/employee/queryAll');
  },
  /**
   * @description: 员工管理查询
   * @param {*}
   * @return {*}
   */
  queryEmployee: (params: EmployeeQueryDto): Promise<ResponseModel<PageResultModel<EmployeeVo>>> => {
    return postRequest<ResponseModel<PageResultModel<EmployeeVo>>>('/admin/employee/query', params);
  },
  /**
   * @description: 添加员工
   * @param {EmployeeAddDto} params
   * @return {*}
   */
  addEmployee: (params: EmployeeAddDto) => {
    return postRequest<ResponseModel<string>>('/admin/employee/add', params);
  },
  /**
   * @description: 更新员工信息
   * @param {EmployeeUpdateDto} params
   * @return {*}
   */
  updateEmployee: (params: EmployeeUpdateDto) => {
    return postRequest<ResponseModel<string>>('/admin/employee/update', params);
  },
  /**
   * @description: 删除员工
   * @param {number} employeeId
   * @return {*}
   */
  deleteEmployee: (employeeId: number) => {
    return getRequest<ResponseModel<string>>(`/admin/employee/delete/${employeeId}`);
  },
  /**
   * @description: 批量删除员工
   * @param {number} employeeIdList
   * @return {*}
   */
  batchDeleteEmployee: (employeeIdList: number[]) => {
    return getRequest<ResponseModel<string>>(`/admin/employee/update/batch/delete?employeeIdList=${employeeIdList}`);
  },
  /**
   * @description: 批量调整员工部门
   * @param {EmployeeDepartmentUpdateDto} updateDto
   * @return {*}
   */
  batchUpdateDepartmentEmployee: (updateDto: EmployeeDepartmentUpdateDto) => {
    return postRequest<ResponseModel<string>>('/admin/employee/update/batch/department', updateDto);
  },
  /**
   * @description: 重置员工密码
   * @param {number} employeeId
   * @return {*}
   */
  resetPassword: (employeeId: number) => {
    return getRequest<ResponseModel<string>>(`/adminemployee/update/pwd/reset/${employeeId}`);
  },
  /**
   * @description: 更新员工禁用状态
   * @param {number} employeeId
   * @return {*}
   */
  updateDisabled: (employeeId: number) => {
    return getRequest<ResponseModel<string>>(`/adminemployee/update/disabled/${employeeId}`);
  },
  /**
   * @description: 查询员工-根据校区id
   * @param {number} deptId
   * @return {*}
   */
  querySchoolEmployee: (deptId: number) => {
    return getRequest<ResponseModel<EmployeeVo[]>>(`/admin/employee/query/school/${deptId}`);
  },
  // 查询员工-根据部门id
  queryEmployeeByDeptId: (deptId: number) => {
    return getRequest<ResponseModel<EmployeeVo[]>>(`/admin/employee/query/dept/${deptId}`);
  },
};
