/*
 * @Author: zhuoda
 * @Date: 2021-08-03 10:27:11
 * @LastEditTime: 2022-01-07
 * @LastEditors: zhuoda
 * @Description:
 * @FilePath: /smart-admin/src/api/system/login/login.ts
 */
import { LoginResultVo } from './model/login-result-vo';
import { ResponseModel } from '/@/api/base-model/response-model';
import { LoginForm } from './model/login-model';
import { CaptchaVO } from './model/captcha-vo';
import { getRequest, postRequest } from '/@/lib/axios';

export const loginApi = {
  /**
   * 登录
   * @param param
   */
  login: (param: LoginForm) => {
    return postRequest<ResponseModel<LoginResultVo>>('/admin/login', param);
  },

  /**
   * 获取验证码
   * @param param
   */
  getCaptcha: () => {
    return getRequest<ResponseModel<CaptchaVO>>('/admin/login/getCaptcha');
  },

  /**
   * 获取登录信息
   * @param param
   */
  getLoginInfo: () => {
    return getRequest<ResponseModel<LoginResultVo>>('/admin/login/getLoginResult');
  },
};
