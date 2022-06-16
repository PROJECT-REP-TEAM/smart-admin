/*
 * @Author: zhuoda
 * @Date: 2021-08-09 08:58:11
 * @LastEditTime: 2022-06-15
 * @LastEditors: zhuoda
 * @Description:
 */
/**
 * key前缀
 */
const KEY_PREFIX = 'smart_admin_';
/**
 * localStorageKey集合
 */
export default {
  // 用户信息
  USER_INFO: `${KEY_PREFIX}user_info`,
  // 用户权限点
  USER_POINTS: `${KEY_PREFIX}user_points`,
  // 用户的tag列表
  USER_TAG_NAV: `${KEY_PREFIX}user_tag_nav`,
};
