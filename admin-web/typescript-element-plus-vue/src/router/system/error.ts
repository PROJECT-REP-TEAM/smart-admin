/*
 * @Author: LiHaiFan
 * @Date: 2022-06-05 15:26:04
 * @LastEditTime: 2022-06-05 18:05:16
 * @LastEditors: LiHaiFan
 * @Description: 
 * @FilePath: \typescript-element-plus-vue\src\router\system\error.ts
 */
import { PAGE_NAME_404 } from '/@/constants/common';
import { RouteRecordRaw } from 'vue-router';

export const error404Routers: Array<RouteRecordRaw> = [
  {
    path: '/404',
    name: PAGE_NAME_404,
    component: () => import('/@/views/system/error/404.vue'),
    meta: {
      title: '404',
      hideInMenu: true,
    },
  },
];
