/*
 * @Author: zhuoda
 * @Date: 2021-08-03 10:27:11
 * @LastEditTime: 2021-08-25 17:01:33
 * @LastEditors: zhuoda
 * @Description:
 * @FilePath: /smart-admin/src/config/app-config.ts
 */
import { AppConfig } from '/@/types/config';

/**
 * 应用默认配置
 */

export const appDefaultConfig: AppConfig = {
  // i18n 语言选择
  languageType: 'zh_CN',
  // 布局: side 或者 side-expand
  layout: 'side-expand',
  // 主题
  theme: 'dark',
  // 手机模式最大宽度 768px，如果少于768px，则手机自适应
  mobileMaxWidth: 768,
  // 侧边菜单宽度 ， 默认为256px
  sideMenuWidth: 256,
  // 标签页
  multiPageTagFlag: true,
  // 标签页缓存, keep-alive， true 开启缓存; false 不开启
  multiPageTagKeepAliveFlag: true,
  // 固定头部状态栏，true:固定，false:不固定
  headerFixedFlag: true,
  // 固定侧边栏，true:固定，false:不固定
  sideBarFixedFlag: true,
  // 隐藏设置，true:隐藏，false:不隐藏
  hideSettingFlag: false,
  // 首页页面Name
  homePageName: 'Home',
};
