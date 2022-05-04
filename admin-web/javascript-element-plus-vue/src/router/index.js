import nProgress from 'nprogress';
import 'nprogress/nprogress.css';
import { createRouter, createWebHashHistory } from 'vue-router';
import {
  PAGE_NAME_404,
  PAGE_NAME_HOME,
  PAGE_NAME_REGISTER,
  PAGE_NAME_LOGIN,
} from '../constants/common';
import { routerArray } from './routers';
import { useUserStore } from '/@/store/modules/user';
import { getTokenFromCookie } from '/@/lib/cookie-util';

// 顶部页面加载进度条不显示spinner
nProgress.configure({ showSpinner: false });

export const router = createRouter({
  history: createWebHashHistory(),
  routes: routerArray,
  strict: true,
  scrollBehavior: () => ({ left: 0, top: 0 }),
});

// ----------------------- 路由加载前 -----------------------
router.beforeEach((to, from, next) => {
  // 进度条开启
  nProgress.start();
  next();
  // 公共页面，任何时候都可以跳转
  if (
    to.name === PAGE_NAME_REGISTER ||
    to.name === PAGE_NAME_LOGIN ||
    to.name === PAGE_NAME_404
  ) {
    next();
    return;
  }

  // 验证登录
  const token = getTokenFromCookie();
  if (!token) {
    useUserStore().logout();
    next({ name: PAGE_NAME_LOGIN });
    return;
  }

  // 首页（ 需要登录 ，但不需要验证权限）
  if (to.name == PAGE_NAME_HOME) {
    next();
    return;
  }

  // 判断访问路径是否不存在
  if (!router.hasRoute(to.name)) {
    next({
      name: PAGE_NAME_404,
    });
    return;
  }

  // 设置tagNav
  useUserStore().setTagNav(to, from);
  next();
});

// ----------------------- 路由加载后 -----------------------
router.afterEach(() => {
  nProgress.done();
});
