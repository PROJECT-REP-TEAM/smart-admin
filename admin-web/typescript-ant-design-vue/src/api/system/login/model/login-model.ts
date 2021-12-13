/*
 * @Author: zhuoda
 * @Date: 2021-08-03 22:27:11
 * @LastEditTime: 2021-12-13
 * @LastEditors: zhuoda
 * @Description:
 */

/**
 *
 * @export
 * @interface LoginForm
 */
export interface LoginForm {
  /**
   * 验证码
   * @type {string}
   * @memberof LoginForm
   */
  captchaCode?: string;
  /**
   * 验证码uuid
   * @type {string}
   * @memberof LoginForm
   */
  captchaUuid?: string;
  /**
   * 登录名
   * @type {string}
   * @memberof LoginForm
   */
  loginName: string;
  /**
   * 密码
   * @type {string}
   * @memberof LoginForm
   */
  password: string;

  /**
   * 登录终端
   * @type {string}
   * @memberof LoginForm
   */
  loginTerminal?: string;
}
