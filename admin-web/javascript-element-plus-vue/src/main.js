import { createApp } from 'vue';
import App from './App.vue';
import { router } from '/@/router/index';
import { store } from "/@/store/index";
import './theme/index.scss';
import ElementPlus from 'element-plus';
import 'element-plus/dist/index.css'
import * as icons from "@element-plus/icons";
import lodash from 'lodash';

let vueApp = createApp(App);
let app = vueApp.use(router).use(store).use(ElementPlus);
// 注册图标组件
Object.keys(icons).forEach((key) => {
  app.component(key, icons[key]);
});
app.config.globalProperties.$icons = icons;
app.config.globalProperties.$lodash = lodash;
app.mount('#app');
