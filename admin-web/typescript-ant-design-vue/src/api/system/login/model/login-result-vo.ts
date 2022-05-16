/*
 * @Author: zhuoda
 * @Date: 2021-08-18 20:02:35
 * @LastEditTime: 2021-12-29
 * @LastEditors: zhuoda
 * @Description:
 * @FilePath: /smart-admin/src/api/system/login/model/login-result-vo.ts
 */

import {MenuVo} from "/@/api/system/menu/model/menu-vo";

export interface LoginResultVo {

  /**
   * token
   */
  token: string;
  /**
   * 员工id
   */
  employeeId?: number;
  /**
   * 登录账号
   */
  loginName?: string;
  /**
   * 员工名称
   */
  actualName?: string;
  /**
   * :  <br>  export const <br> GenderEnum = <BR> {<br>&nbsp;&nbsp;UNKNOWN:{<br>&nbsp;&nbsp;&nbsp;&nbsp;value:0,<br>&nbsp;&nbsp;&nbsp;&nbsp;desc:\'未知\'<br>&nbsp;&nbsp;},<br>&nbsp;&nbsp;MAN:{<br>&nbsp;&nbsp;&nbsp;&nbsp;value:1,<br>&nbsp;&nbsp;&nbsp;&nbsp;desc:\'男\'<br>&nbsp;&nbsp;},<br>&nbsp;&nbsp;WOMAN:{<br>&nbsp;&nbsp;&nbsp;&nbsp;value:2,<br>&nbsp;&nbsp;&nbsp;&nbsp;desc:\'女\'<br>&nbsp;&nbsp;}<br>} <br>
   */
  gender?: number;
  /**
   * 手机号码
   */
  phone?: string;
  /**
   * 部门id
   */
  departmentId?: number;
  /**
   * 部门名称
   */
  departmentName?: string;

  /**
   * 是否管理员
   */
  administratorFlag?: boolean;


  /**
   * 功能点权限列表
   */
  permissionList?: Array<string>;
  /**
   * 菜单树
   */
  menuList?: Array<MenuVo>;
}
