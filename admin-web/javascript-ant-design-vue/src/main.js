/*
 * @Description: 主方法
 * @Author: zhuoda
 * @Date: 2021-08-03
 * @LastEditTime: 2022-06-02
 * @LastEditors: zhuoda
 */
import { createApp } from 'vue';
import { router } from '/@/router/index';
import { store } from '/@/store/index';
import Antd from 'ant-design-vue';
import '/@/theme/index.less';
import constantsInfo from '/@/constants/index';
import * as antIcons from '@ant-design/icons-vue';
import smartEnumPlugin from '/@/plugins/smart-enums-plugin';
import lodash from 'lodash';
import moment from 'moment';
import 'moment/dist/locale/zh-cn';
import App from './App.vue';

moment.locale('zh-cn');

let vueApp = createApp(App);
let app = vueApp.use(router).use(store).use(Antd).use(smartEnumPlugin, constantsInfo);

// 注册图标组件
Object.keys(antIcons).forEach((key) => {
  app.component(key, antIcons[key]);
});
//全局
app.config.globalProperties.$antIcons = antIcons;
app.config.globalProperties.$lodash = lodash;
//挂载
app.mount('#app');
