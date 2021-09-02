/*
 * @Author: zhuoda
 * @Date: 2021-08-03 10:27:11
 * @LastEditTime: 2021-09-01 20:56:03
 * @LastEditors: zhuoda
 * @Description:
 * @FilePath: /smart-admin/src/api/system/menu/model/menu-points-operate-form.ts
 */
/**
 * smart-admin-java-2.0-admin
 * http://localhost:20086/v2/api-docs?group=Admin
 *
 * The version of the OpenAPI document: 1.0.0
 *
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */

export interface MenuPointsOperateForm {
  /**
   * 功能点关联菜单ID
   */
  contextMenuId?: number;
  /**
   * 禁用状态
   */
  disabledFlag?: boolean;
  /**
   * 菜单ID
   */
  menuId?: number;
  /**
   * 功能点名称
   */
  menuName?: string;
  /**
   * 接口权限
   */
  permsList?: string[];
}
