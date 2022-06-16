import { createApp } from 'vue';
import App from './App.vue';
import { buildRoutes, router } from '/@/router/index';
import { store } from '/@/store/index';
import './theme/index.scss';
import ElementPlus from 'element-plus';
import 'element-plus/dist/index.css';
import * as icons from '@element-plus/icons';
import lodash from 'lodash';
import smartEnumPlugin from '/@/plugins/smart-enums-plugin';
import constantsInfo from '/@/constants/index';
import privilegePlugin from '/@/plugins/privilege-plugin';
import locale from 'element-plus/lib/locale/lang/zh-cn';
import JsonViewer from 'vue-json-viewer';
import { inserted } from '/@/directives/privilege';
import { loginApi } from '/@/api/system/login';
import { useUserStore } from '/@/store/modules/user';
import { getTokenFromCookie } from '/@/lib/cookie-util';

/**
 * 获取用户信息和用户权限对应的路由，构建动态路由
 */
async function getLoginInfo() {
  //获取登录用户信息
  const res = await loginApi.getLoginInfo();
  //构建系统的路由
  let menuRouterList = res.data.menuList.filter(e => e.path);
  buildRoutes(menuRouterList);
  initVue();
  //更新用户信息到pinia
  useUserStore().setUserSession(res.data);
}

function initVue() {
  let vueApp = createApp(App);
  let app = vueApp.use(router).use(store).use(JsonViewer).use(ElementPlus, { locale }).use(smartEnumPlugin, constantsInfo).use(privilegePlugin);

  app.directive('privilege', {
    mounted(el, binding) {
      inserted(el, binding);
    },
  });
  // 注册图标组件
  Object.keys(icons).forEach((key) => {
    app.component(key, icons[key]);
  });
  //全局
  app.config.globalProperties.$icons = icons;
  app.config.globalProperties.$lodash = lodash;
  //挂载
  app.mount('#app');
}

//不需要获取用户信息、用户菜单、用户菜单动态路由，直接初始化vue即可
let token = getTokenFromCookie();
if (!token) {
  initVue();
} else {
  getLoginInfo();
}
