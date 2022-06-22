/*
 * @Description: 
 * @Author: zhuoda
 * @Date: 2021-11-05
 * @LastEditTime: 2022-06-22
 * @LastEditors: zhuoda
 */

// 语言选择数组
export const i18nList = [
  {
    text: '简体中文',
    value: 'zh_CN',
    dayjs: 'zh-cn'
  },
  {
    text: 'English',
    value: 'en_US',
    dayjs: 'en-au'
  },
]

import { createI18n } from 'vue-i18n';
import messages from './messages'
const i18n = createI18n({
  fallbackLocale: 'zh',//预设语言环境
  globalInjection: true,
  legacy: false, // 
  locale: "zh",//默认显示的语言
  messages,//
});

export default i18n;