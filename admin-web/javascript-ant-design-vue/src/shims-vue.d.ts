/*
 * @Description:
 * @Author: zhuoda
 * @Date: 2021-08-03
 * @LastEditTime: 2021-08-27
 * @LastEditors: zhuoda
 */
import * as lodash from 'lodash';

declare module '*.vue' {
  import { Component } from 'vue';
  const component: Component;
  export default component;
}

// 对vue进行类型补充说明
declare module '@vue/runtime-core' {
  interface ComponentCustomProperties {
    // 常量图标
    $antIcons: Object;
    // lodash工具类
    $lodash: lodash;
  }
}
