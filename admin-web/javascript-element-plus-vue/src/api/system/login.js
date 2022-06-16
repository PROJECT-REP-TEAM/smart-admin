/*
 * @Author: zhuoda
 * @Date: 2021-08-03 10:27:11
 * @LastEditTime: 2022-01-07
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
    return postRequest('/login', param);
  },

  /**
   * 获取验证码
   * @param param
   */
  getCaptcha: () => {
    return getRequest('/login/getCaptcha');
  },

  /**
   * 获取登录信息
   * @param param
   */
  getLoginInfo: () => {
    return getRequest('/login/getLoginInfo');
  },
};
