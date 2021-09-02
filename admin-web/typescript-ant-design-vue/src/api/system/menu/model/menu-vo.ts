/*
 * @Author: zhuoda
 * @Date: 2021-08-11 11:52:11
 * @LastEditTime: 2021-09-01 20:55:06
 * @LastEditors: zhuoda
 * @Description: 
 * @FilePath: /smart-admin/src/api/system/menu/model/menu-vo.ts
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

export interface MenuVo {
  /**
   * 是否缓存
   */
  cacheFlag?: boolean;
  /**
   * 组件路径
   */
  component?: string;
  /**
   * 功能点关联菜单ID
   */
  contextMenuId?: number;
  /**
   * 创建时间
   */
  createTime?: string;
  /**
   * 创建人
   */
  createUser?: number;
  /**
   * 禁用状态
   */
  disabledFlag?: boolean;
  /**
   * 是否为外链
   */
  frameFlag?: boolean;
  /**
   * 菜单图标
   */
  icon?: string;
  /**
   * 菜单ID
   */
  menuId?: number;
  /**
   * 菜单名称
   */
  menuName?: string;
  /**
   * 类型:  <br>  export const <br> MENU_TYPE_ENUM = <BR> {<br>&nbsp;&nbsp;CATALOG:{<br>&nbsp;&nbsp;&nbsp;&nbsp;value:1,<br>&nbsp;&nbsp;&nbsp;&nbsp;desc:\'目录\'<br>&nbsp;&nbsp;},<br>&nbsp;&nbsp;MENU:{<br>&nbsp;&nbsp;&nbsp;&nbsp;value:2,<br>&nbsp;&nbsp;&nbsp;&nbsp;desc:\'菜单\'<br>&nbsp;&nbsp;},<br>&nbsp;&nbsp;POINTS:{<br>&nbsp;&nbsp;&nbsp;&nbsp;value:3,<br>&nbsp;&nbsp;&nbsp;&nbsp;desc:\'功能点\'<br>&nbsp;&nbsp;}<br>} <br>
   */
  menuType: number;
  /**
   * 父菜单ID
   */
  parentId?: number;
  /**
   * 路由地址
   */
  path?: string;
  /**
   * 接口权限
   */
  perms?: string;
  /**
   * 接口权限（拆分）
   */
  permsList?: string[];
  /**
   * 显示顺序
   */
  sort?: number;
  /**
   * 更新时间
   */
  updateTime?: string;
  /**
   * 更新人
   */
  updateUser?: number;
  /**
   * 显示状态
   */
  visibleFlag?: boolean;

  /**
   * 孩子
   */
  children?: MenuVo[];
}
