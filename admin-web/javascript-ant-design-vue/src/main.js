/*
 * @Description: 主方法
 * @Author: zhuoda
 * @Date: 2021-08-03
 * @LastEditTime: 2022-06-23
 * @LastEditors: zhuoda
 */
import { createApp } from 'vue';
import { router } from '/@/router/index';
import { store } from '/@/store/index';
import Antd from 'ant-design-vue';
import '/@/theme/index.less';
import i18n from '/@/i18n/index';
import constantsInfo from '/@/constants/index';
import * as antIcons from '@ant-design/icons-vue';
import { privilegeDirective } from '/@/directives/privilege';
import smartEnumPlugin from '/@/plugins/smart-enums-plugin';
import privilegePlugin from '/@/plugins/privilege-plugin';
import lodash from 'lodash';
import dayjs from 'dayjs';
import App from './App.vue';
import { useUserStore } from "/@/store/modules/system/user";
import { buildRoutes } from "/@/router/index";
import { loginApi } from "/@/api/system/login/login-api";
import { getTokenFromCookie } from '/@/utils/cookie-util';
import JsonViewer from "vue3-json-viewer"
import "vue3-json-viewer/dist/index.css";



dayjs.locale('zh-cn');

/**
 * 获取用户信息和用户权限对应的路由，构建动态路由
 */
async function getLoginInfo () {
  //获取登录用户信息
  const res = await loginApi.getLoginInfo();
  //构建系统的路由
  let menuRouterList = res.data.menuList.filter(e => e.path || e.frameUrl);
  buildRoutes(menuRouterList);
  initVue();
  //更新用户信息到pinia
  useUserStore().setUserLoginInfo(res.data);
}


function initVue () {
  let vueApp = createApp(App);
  let app = vueApp.use(router).use(store).use(i18n).use(Antd).use(smartEnumPlugin, constantsInfo).use(privilegePlugin).use(JsonViewer);
  //注入权限
  app.directive('privilege', {
    mounted (el, binding) {
      privilegeDirective(el, binding);
    },
  });
  // 注册图标组件
  Object.keys(antIcons).forEach((key) => {
    app.component(key, antIcons[key]);
  });
  //全局
  app.config.globalProperties.$antIcons = antIcons;
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











