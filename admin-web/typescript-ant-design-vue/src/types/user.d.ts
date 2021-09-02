/*
 * @Author: zhuoda
 * @Date: 2021-08-03 10:27:11
 * @LastEditTime: 2021-08-18 20:14:20
 * @LastEditors: zhuoda
 * @Description:
 * @FilePath: /smart-admin/src/types/user.d.ts
 */
import { MenuTreeVo } from '@/api/system/login/login-model.ts';
import { UserTagNav } from '/@/store/modules/model/UserTagNav';
import { EmployeeLoginVo } from '/@/api/system/login/model/employee-login-vo';

export interface UserState {
  /**
   * @description: token
   * @param {*}
   * @return {*}
   */
  token?: string;
  /**
   * @description: 用户信息
   * @param {*}
   * @return {*}
   */
  userInfo: EmployeeLoginVo;
  /**
   * @description: 功能点权限列表
   * @param {*}
   * @return {*}
   */
  pointsList?: Array<string>;
  /**
   * @description: 菜单树
   * @param {*}
   * @return {*}
   */
  menuTree?: Array<MenuTreeVo>;
  /**
   * @description: tag列表
   * @param {*}
   * @return {*}
   */
  tagNav?: Array<UserTagNav>;
}
