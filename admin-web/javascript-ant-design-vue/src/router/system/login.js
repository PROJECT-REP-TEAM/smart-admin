/*
 * @Description:
 * @Author: zhuoda
 * @Date: 2021-08-03
 * @LastEditTime: 2022-06-24
 * @LastEditors: zhuoda
 */

export const loginRouters = [
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
