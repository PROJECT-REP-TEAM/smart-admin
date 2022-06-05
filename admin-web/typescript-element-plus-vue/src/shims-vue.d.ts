/*
 * @Description:
 * @Author: zhuoda
 * @Date: 2021-08-03
 * @LastEditTime: 2022-06-05 16:56:13
 * @LastEditors: LiHaiFan
 */
import { SmartEnumPlugin } from '/@/types/smart-enum';
import * as lodash from 'lodash';

declare module '*.vue' {
  import { Component } from 'vue';
  const component: Component;
  export default component;
}

// 对vue进行类型补充说明
declare module '@vue/runtime-core' {
  interface ComponentCustomProperties {
    // 常量插件
    $smartEnumPlugin: SmartEnumPlugin;
    // 常量图标
    $icons: Object;
    // lodash工具类
    $lodash: lodash;
  }
}
