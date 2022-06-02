/*
 * @Description:
 * @version:
 * @Author: zhuoda
 * @Date: 2021-08-20 15:06:57
 * @LastEditors: zhuoda
 * @LastEditTime: 2022-06-02
 */
import SmartLayout from '/@/layout/smart-layout.vue';
import { MENU_TYPE_ENUM } from '/@/constants/system/menu/menu-enum';

export const homeRouters = [
  {
    path: '/',
    name: '_home',
    redirect: '/home',
    component: SmartLayout,
    meta: {
      title: '首页',
      menuType: MENU_TYPE_ENUM.CATALOG.value,
      icon: 'HomeOutlined',
    },
    children: [
      {
        path: '/home',
        name: 'Home',
        meta: {
          title: '首页',
          menuType: MENU_TYPE_ENUM.MENU.value,
          icon: 'HomeOutlined',
          parentMenuList: [{ name: '_home', title: '首页' }],
        },
        component: () => import('/@/views/system/home/index.vue'),
      },
    ],
  },
];
