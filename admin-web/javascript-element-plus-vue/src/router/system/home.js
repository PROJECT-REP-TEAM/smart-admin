import SmartLayout from '/@/layout/smart-layout.vue';
import { MENU_TYPE_ENUM } from '/@/constants/system/menu-enum';

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
        component: () => import('/@/views/business/home/index.vue'),
      },
    ],
  },
];
