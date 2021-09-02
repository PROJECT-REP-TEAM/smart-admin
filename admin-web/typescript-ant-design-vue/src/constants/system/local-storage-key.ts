/*
 * @Author: zhuoda
 * @Date: 2021-08-09 08:58:11
 * @LastEditTime: 2021-08-18 20:13:28
 * @LastEditors: zhuoda
 * @Description: 
 * @FilePath: /smart-admin/src/constants/system/local-storage-key.ts
 */
/**
 * key前缀
 */
const KEY_PREFIX:string = 'crm_';
/**
 * localStorageKey集合
 */
export default {
    // 用户信息
    USER_INFO: `${KEY_PREFIX}user_info`,
    // 用户菜单路由
    USER_MENU: `${KEY_PREFIX}user_menu`,
    // 用户权限点
    USER_POINTS:`${KEY_PREFIX}user_points`,
    // 用户的tag列表
    USER_TAG_NAV:`${KEY_PREFIX}user_tag_nav`,
}
