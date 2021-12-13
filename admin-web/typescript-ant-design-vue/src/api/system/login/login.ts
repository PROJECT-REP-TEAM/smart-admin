/*
 * @Author: zhuoda
 * @Date: 2021-08-03 10:27:11
 * @LastEditTime: 2021-12-13
 * @LastEditors: zhuoda
 * @Description:
 * @FilePath: /smart-admin/src/api/system/login/login.ts
 */
import { EmployeeLoginVo } from './model/employee-login-vo';
import { ResponseModel } from '/@/api/base-model/response-model';
import { LoginForm } from './model/login-model';
import { getRequest, postRequest } from '/@/lib/axios';

export const loginApi = {
  /**
   * 登录
   * @param param
   */
  login: (param: LoginForm) => {
    return postRequest<ResponseModel<EmployeeLoginVo>>('/system/login', param);
  },

  /**
   * 获取登录信息
   * @param param
   */
  getLogin: () => {
    return getRequest<ResponseModel<EmployeeLoginVo>>('/system/login/getLoginInfo');
  },
};
