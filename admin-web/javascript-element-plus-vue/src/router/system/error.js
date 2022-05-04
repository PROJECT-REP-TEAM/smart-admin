import { PAGE_NAME_404 } from '/@/constants/common';

export const error404Routers = [
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
