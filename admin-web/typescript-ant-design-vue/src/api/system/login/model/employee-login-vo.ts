/*
 * @Author: zhuoda
 * @Date: 2021-08-18 20:02:35
 * @LastEditTime: 2021-12-29
 * @LastEditors: zhuoda
 * @Description:
 * @FilePath: /smart-admin/src/api/system/login/model/employee-login-vo.ts
 */

import { MenuTreeVo } from '../../menu/model/menu-tree-vo';

export interface EmployeeLoginVo {
  /**
   * 员工名称
   */
  actualName: string;
  /**
   * 部门id
   */
  departmentId?: number;
  /**
   * 部门名称
   */
  departmentName?: string;
  /**
   * 是否被禁用
   */
  disabledFlag?: boolean;
  /**
   * 员工id
   */
  employeeId?: number;
  /**
   * :  <br>  export const <br> GenderEnum = <BR> {<br>&nbsp;&nbsp;UNKNOWN:{<br>&nbsp;&nbsp;&nbsp;&nbsp;value:0,<br>&nbsp;&nbsp;&nbsp;&nbsp;desc:\'未知\'<br>&nbsp;&nbsp;},<br>&nbsp;&nbsp;MAN:{<br>&nbsp;&nbsp;&nbsp;&nbsp;value:1,<br>&nbsp;&nbsp;&nbsp;&nbsp;desc:\'男\'<br>&nbsp;&nbsp;},<br>&nbsp;&nbsp;WOMAN:{<br>&nbsp;&nbsp;&nbsp;&nbsp;value:2,<br>&nbsp;&nbsp;&nbsp;&nbsp;desc:\'女\'<br>&nbsp;&nbsp;}<br>} <br>
   */
  gender?: number;
  /**
   * 是否为超管
   */
  isSuperMan?: boolean;
  /**
   * 登录账号
   */
  loginName?: string;
  /**
   * 菜单树
   */
  menuTree?: Array<MenuTreeVo>;
  /**
   * 手机号码
   */
  phone?: string;
  /**
   * 功能点权限列表
   */
  pointsList?: Array<string>;
  /**
   * 角色列表
   */
  roleList?: Array<number>;
  /**
   * token
   */
  token?: string;
  /**
   * 所属校区
   */
  schoolId?: number;
  /**
   * 所属校区名称
   */
  schoolName?: string;
}
