/*
 * @Author: zhuoda
 * @Date: 2021-08-03 10:27:11
 * @LastEditTime: 2021-08-18 20:04:05
 * @LastEditors: zhuoda
 * @Description:
 * @FilePath: /smart-admin/src/api/system/login/login.ts
 */
import { getRequest, postRequest } from '/@/lib/axios';

export const loginApi = {
  /**
   * 登录
   * @param param
   */
  login: (param) => {
    return postRequest('/system/login', param);
  },

  /**
   * 获取登录信息
   * @param param
   */
  getLogin: () => {
    return getRequest('/system/login/get');
  },
};
