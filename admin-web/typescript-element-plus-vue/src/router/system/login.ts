/*
 * @Description:
 * @Author: zhuoda
 * @Date: 2021-08-03
 * @LastEditTime: 2021-08-30 17:51:22
 * @LastEditors: zhuoda
 */
import { RouteRecordRaw } from 'vue-router';

export const loginRouters: Array<RouteRecordRaw> = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('/@/views/system/login/login.vue'),
    meta: {
      title: '登录',
      hideInMenu: true,
    },
  },
];
