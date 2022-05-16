import { createRouter, createWebHashHistory, RouteRecordRaw } from 'vue-router';
import { routerArray } from './routers';
import nProgress from 'nprogress';
import 'nprogress/nprogress.css';
import { getTokenFromCookie } from '/@/utils/cookie-util';
import { useUserStore } from '/@/store/modules/system/user';
import _ from 'lodash';
import SmartLayout from '/@/layout/smart-layout.vue';
import { PAGE_NAME_404, PAGE_NAME_HOME, PAGE_NAME_LOGIN } from '../constants/common';
import { nextTick } from 'vue';
import { MenuVo } from '../api/system/menu/model/menu-vo';

// 顶部页面加载进度条不显示spinner
nProgress.configure({ showSpinner: false });

// 记录后端返回的路由已经完成初始化
let buildServerRouterFlag = false;

export const router = createRouter({
  history: createWebHashHistory(),
  routes: routerArray,
  strict: true,
  scrollBehavior: () => ({ left: 0, top: 0 }),
});

// ----------------------- 路由加载前 -----------------------
router.beforeEach(async (to, from, next) => {
  // 进度条开启
  nProgress.start();

  // 公共页面，任何时候都可以跳转
  if (to.name === PAGE_NAME_LOGIN || to.name === PAGE_NAME_404) {
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

  // 初始化后端返回的路由
  if (!buildServerRouterFlag) {
    let menuList = useUserStore().getMenuList || [];
    buildRoutes(menuList);
    buildServerRouterFlag = true;
    next({ path: to.path, query: to.query });
    return;
  }

  // 首页（ 需要登录 ，但不需要验证权限）
  if (to.name == PAGE_NAME_HOME) {
    next();
    return;
  }

  // 是否刷新缓存
  // 当前路由是否在tag中 存在tag中且没有传递keepAlive则刷新缓存
  let findTag = (useUserStore().tagNav || []).find((e) => e.menuName == to.name);
  let reloadKeepAlive = findTag && !to.params.keepAlive;

  // 设置tagNav
  useUserStore().setTagNav(to, from);
  // 设置keepAlive 或 删除KeepAlive
  if (to.meta.keepAlive) {
    if (reloadKeepAlive) {
      useUserStore().deleteKeepAliveIncludes(to.name?.toString());
    }
    nextTick(() => {
      useUserStore().pushKeepAliveIncludes(to.name?.toString());
    });
  }
  next();
});

// ----------------------- 路由加载后 -----------------------
router.afterEach(() => {
  nProgress.done();
});

// ----------------------- 构建router对象 -----------------------
function buildRoutes(menuList: Array<MenuVo>) {
  /**
   * 1、构建整个路由信息
   * 2、添加到路由里
   */
  const resList: Array<RouteRecordRaw> = [];
  // 获取所有vue组件引用地址 用于构建路由
  const modules = import.meta.glob('../views/**/**.vue');
  // 获取所有vue组件 用于注入name属性 name属性用于keep-alive
  const modulesEager = import.meta.globEager('../views/**/**.vue');

  //1、构建整个路由信息
  for (const e of menuList) {
    if (!e.menuId) {
      continue;
    }
    if(!e.path){
      continue;
    }
    let menuIdStr = e.menuId.toString();
    let route: RouteRecordRaw = {
      // @ts-ignore
      path: e.path.startsWith('/') ? e.path : `/${e.path}`,
      // 使用menuId作为name唯一标识
      name: menuIdStr,
      meta: {
        // 菜单展示
        title: e.menuName,
        // 菜单图标展示
        icon: e.icon,
        // 是否在菜单隐藏
        hideInMenu: !e.visibleFlag,
        // 页面是否keep-alive缓存
        keepAlive: e.cacheFlag,
        // 是否来自服务器 用于在beforeEach中判断router是否已经加载了来自服务器的路由 以此跳过重复addRoute
        fromServer: true,
      },
    };
    let componentPath = e.component && e.component.startsWith('/') ? e.component : '/' + e.component;
    let relativePath = `../views${componentPath}`;
    // eslint-disable-next-line no-prototype-builtins
    if (modules.hasOwnProperty(relativePath)) {
      route.component = modules[relativePath];
      // 组件注入name
      let eager = modulesEager[relativePath];
      if (eager) {
        eager.default.name = menuIdStr;
      }
    }
    resList.push(route);
  }

  //2、添加到路由里
  router.addRoute({
    path: '',
    meta: {},
    component: SmartLayout,
    children: resList,
  });
}
