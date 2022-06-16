/*
 * @Description: 员工api
 * @Author: zhuoda
 * @Date: 2021-08-12 18:00:56
 * @LastEditTime: 2022-06-16
 * @LastEditors: zhuoda
 */

import { getRequest, postRequest } from '/@/lib/axios';

export const employeeApi = {
  /**
   * @description: 查询所有员工 by zhuoda
   * @param {*}
   * @return {*}
   */
  queryAll: () => {
    return getRequest('/employee/queryAll');
  },
  /**
   * @description: 员工管理查询
   * @param {*}
   * @return {*}
   */
  queryEmployee: (params) => {
    return postRequest('/employee/query', params);
  },
  /**
   * @description: 添加员工
   * @param {EmployeeAddDto} params
   * @return {*}
   */
  addEmployee: (params) => {
    return postRequest('/employee/add', params);
  },
  /**
   * @description: 更新员工信息
   * @param {EmployeeUpdateDto} params
   * @return {*}
   */
  updateEmployee: (params) => {
    return postRequest('/employee/update', params);
  },
  /**
   * @description: 删除员工
   * @param {number} employeeId
   * @return {*}
   */
  deleteEmployee: (employeeId) => {
    return getRequest(`/employee/delete/${employeeId}`);
  },
  /**
   * @description: 批量删除员工
   * @param {number} employeeIdList
   * @return {*}
   */
  batchDeleteEmployee: (employeeIdList) => {
    return postRequest('/employee/update/batch/delete', employeeIdList);
  },
  /**
   * @description: 批量调整员工部门
   * @return {*}
   */
  batchUpdateDepartmentEmployee: (updateParam) => {
    return postRequest('/employee/update/batch/department', updateParam);
  },
  /**
   * @description: 重置员工密码
   * @param {number} employeeId
   * @return {*}
   */
  resetPassword: (employeeId) => {
    return getRequest(`employee/update/password/reset/${employeeId}`);
  },
  /**
   * @description: 更新员工禁用状态
   * @param {number} employeeId
   * @return {*}
   */
  updateDisabled: (employeeId) => {
    return getRequest(`employee/update/disabled/${employeeId}`);
  },
  /**
   * @description: 查询员工-根据校区id
   * @param {number} deptId
   * @return {*}
   */
  querySchoolEmployee: (deptId) => {
    return getRequest(`/employee/query/school/${deptId}`);
  },
  // 查询员工-根据部门id
  queryEmployeeByDeptId: (deptId) => {
    return getRequest(`/employee/query/dept/${deptId}`);
  },
};
