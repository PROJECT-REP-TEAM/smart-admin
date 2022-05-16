import { createRouter, createWebHashHistory, RouteRecordRaw } from 'vue-router';
import { routerArray } from './routers';
import nProgress from 'nprogress';
import 'nprogress/nprogress.css';
import { clearAllCoolies, getTokenFromCookie } from '/@/utils/cookie-util';
import { useUserStore } from '/@/store/modules/system/user';
import { MenuTreeVo } from '/@/api/system/menu/model/menu-tree-vo';
import _ from 'lodash';
import SmartLayout from '/@/layout/smart-layout.vue';
import SmartParentView from '/@/layout/smart-parent-view.vue';
import { MENU_TYPE_ENUM } from '/@/constants/system/menu';

const LOGIN_PAGE_NAME = 'Login';

export const router = createRouter({
  history: createWebHashHistory(),
  routes: routerArray,
  strict: true,
  scrollBehavior: () => ({ left: 0, top: 0 }),
});

// ----------------------- 路由加载前 -----------------------
router.beforeEach(async (to, from, next) => {
  nProgress.configure({ showSpinner: false });
  if (to.meta.title) {
    nProgress.start();
  }

  // 公共页面，任何时候都可以跳转
  if (to.path === '/login' || to.path === '/403' || to.path === '/404') {
    next();
    nProgress.done();
    return;
  }

  // 非公共页面，就需要验证token了
  const token = getTokenFromCookie();
  if (!token) {
    // 跳转到 登录页面
    clearAllCoolies();
    nProgress.done();
    next({
      name: LOGIN_PAGE_NAME,
    });
    return;
  }

  // 设置tagNav
  useUserStore().setTagNav(to, from);

  let serverRoutes = router.getRoutes().filter((e) => e.meta.fromServer);
  if (!_.isEmpty(serverRoutes)) {
    next();
    return;
  }

  // 判断是否获取有用户菜单
  let menuTree = useUserStore().getMenuTree || [];
  if (!_.isEmpty(menuTree)) {
    let routeList = buildRoutes(menuTree, 1, []);
    console.log(routeList);
    routeList.forEach((e) => {
      router.addRoute(e);
    });
  }
  next({ ...to, replace: true });
});

// ----------------------- 路由加载后 -----------------------
router.afterEach(() => {
  nProgress.done();
});

// ----------------------- 构建router对象 -----------------------
function buildRoutes(menuList: Array<MenuTreeVo>, level: number, parentMenuList: Array<Record<string, string>>): Array<RouteRecordRaw> {
  const resList: Array<RouteRecordRaw> = [];
  // 获取所有vue组件
  const modules = import.meta.glob('../views/**/**.vue');
  for (let e of menuList) {
    if (level == 1) {
      parentMenuList = [];
    }
    // @ts-ignore
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
        noKeepAlive: e.cacheFlag,
        // 菜单类型  由于router.getRoutes()会把所有路由全部返回（目录以及菜单） 需要一个标识过滤出目录类型
        menuType: e.menuType,
        // 是否来自服务器 用于在beforeEach中判断router是否已经加载了来自服务器的路由 以此跳过重复addRoute
        fromServer: true,
        // 上级菜单目录唯一标识集合 用于a-menu展开菜单目录
        parentMenuList: parentMenuList,
      },
      component: level == 1 ? SmartLayout : SmartParentView,
    };
    if (e.menuType == MENU_TYPE_ENUM.MENU.value) {
      let componentPath = e.component && e.component.startsWith('/') ? e.component : '/' + e.component;
      let relativePath = `../views${componentPath}`;
      // eslint-disable-next-line no-prototype-builtins
      if (modules.hasOwnProperty(relativePath)) {
        route.component = modules[relativePath];
      }
    }
    if (!_.isEmpty(e.children)) {
      // 递归
      parentMenuList.push({ name: menuIdStr, title: e.menuName });
      route.children = buildRoutes(e.children || [], level + 1, parentMenuList);
    }
    // 如果当前是一级且是菜单 需要在外面包一层虚拟路由承载SmartLayout
    if (level == 1 && e.menuType == MENU_TYPE_ENUM.MENU.value) {
      if (!route.meta) {
        continue;
      }
      route.meta.parentMenuList.push({ name: menuIdStr, title: e.menuName });
      let virtualRoute: RouteRecordRaw = {
        // @ts-ignore
        // path: e.path.startsWith('/') ? e.path : `/${e.path}`,
        path: `/virtual-${menuIdStr}`,
        // 使用menuId作为name唯一标识
        name: `virtual${menuIdStr}`,
        meta: {},
        component: SmartLayout,
        children: [route],
      };
      resList.push(virtualRoute);
      continue;
    }
    resList.push(route);
  }
  return resList;
}
