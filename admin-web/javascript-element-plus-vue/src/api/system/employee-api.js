/*
 * @Description: 员工api
 * @Author: zhuoda
 * @Date: 2021-08-12 18:00:56
 * @LastEditTime: 2021-08-25 11:24:51
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
    return getRequest('/admin/employee/queryAll');
  },
  /**
   * @description: 员工管理查询
   * @param {*}
   * @return {*}
   */
  queryEmployee: (params) => {
    return postRequest('/admin/employee/query', params);
  },
  /**
   * @description: 添加员工
   * @param {EmployeeAddDto} params
   * @return {*}
   */
  addEmployee: (params) => {
    return postRequest('/admin/employee/add', params);
  },
  /**
   * @description: 更新员工信息
   * @param {EmployeeUpdateDto} params
   * @return {*}
   */
  updateEmployee: (params) => {
    return postRequest('/admin/employee/update', params);
  },
  /**
   * @description: 删除员工
   * @param {number} employeeId
   * @return {*}
   */
  deleteEmployee: (employeeId) => {
    return getRequest(`/admin/employee/delete/${employeeId}`);
  },
  /**
   * @description: 批量删除员工
   * @param {number} employeeIdList
   * @return {*}
   */
  batchDeleteEmployee: (employeeIdList) => {
    return getRequest(`/admin/employee/update/batch/delete?employeeIdList=${employeeIdList}`);
  },
  /**
   * @description: 批量调整员工部门
   * @param {EmployeeDepartmentUpdateDto} updateDto
   * @return {*}
   */
  batchUpdateDepartmentEmployee: (updateDto) => {
    return postRequest('/admin/employee/update/batch/department', updateDto);
  },
  /**
   * @description: 重置员工密码
   * @param {number} employeeId
   * @return {*}
   */
  resetPassword: (employeeId) => {
    return getRequest(`/adminemployee/update/pwd/reset/${employeeId}`);
  },
  /**
   * @description: 更新员工禁用状态
   * @param {number} employeeId
   * @return {*}
   */
  updateDisabled: (employeeId) => {
    return getRequest(`/adminemployee/update/disabled/${employeeId}`);
  },
  /**
   * @description: 查询员工-根据校区id
   * @param {number} deptId
   * @return {*}
   */
  querySchoolEmployee: (deptId) => {
    return getRequest(`/admin/employee/query/school/${deptId}`);
  },
  // 查询员工-根据部门id
  queryEmployeeByDeptId: (deptId) => {
    return getRequest(`/admin/employee/query/dept/${deptId}`);
  },
};
