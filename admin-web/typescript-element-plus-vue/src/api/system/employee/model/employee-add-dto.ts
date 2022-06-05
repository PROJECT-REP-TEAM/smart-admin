/*
 * @Author: zhuoda
 * @Date: 2021-08-16 15:10:08
 * @LastEditTime: 2022-06-06 00:17:14
 * @LastEditors: LiHaiFan
 * @Description:
 * @FilePath: \typescript-element-plus-vue\src\api\system\employee\model\employee-add-dto.ts
 */
/**
 * smart-admin-local
 * http://localhost:50110/v2/api-docs?group=Admin
 *
 * The version of the OpenAPI document: 1.0.0
 *
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */

export interface EmployeeAddDto {
  /**
   * 姓名
   */
  actualName?: string;
  /**
   * 部门id
   */
  departmentId?: number;
  /**
   * 是否启用
   */
  disabledFlag?: boolean | number;
  /**
   * :  <br>  export const <br> GenderEnum = <BR> {<br>&nbsp;&nbsp;UNKNOWN:{<br>&nbsp;&nbsp;&nbsp;&nbsp;value:0,<br>&nbsp;&nbsp;&nbsp;&nbsp;desc:\'未知\'<br>&nbsp;&nbsp;},<br>&nbsp;&nbsp;MAN:{<br>&nbsp;&nbsp;&nbsp;&nbsp;value:1,<br>&nbsp;&nbsp;&nbsp;&nbsp;desc:\'男\'<br>&nbsp;&nbsp;},<br>&nbsp;&nbsp;WOMAN:{<br>&nbsp;&nbsp;&nbsp;&nbsp;value:2,<br>&nbsp;&nbsp;&nbsp;&nbsp;desc:\'女\'<br>&nbsp;&nbsp;}<br>} <br>
   */
  gender?: number;
  /**
   * 登录账号
   */
  loginName?: string;
  /**
   * 手机号
   */
  phone?: string;
  /**
   * 角色列表
   */
  roleIdList?: Array<number>;
}
