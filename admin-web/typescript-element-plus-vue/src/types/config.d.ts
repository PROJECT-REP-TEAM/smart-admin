/*
 * @Description:
 * @Author: zhuoda
 * @Date: 2021-08-03
 * @LastEditTime: 2021-09-01
 * @LastEditors: zhuoda
 */
/**
 *  语言 i18n
 */
export type LanguageType = 'zh_CN' | 'en' | 'ru' | 'ja' | 'ko';

/**
 * 四种布局: 左侧、左侧展开、顶部、混合
 */
export type LayoutType = 'side' | 'side-expand' | 'top' | 'mix';

/**
 * 主题： 亮色，暗色，夜色
 */
export type ThemeType = 'light' | 'dark';

/**
 * 项目信息配置
 */
export interface ProjectConfig {
  // 项目名称, eg : SmartAdmin
  projectName: String;
  // 版权信息 'Copyright © 2015-2021 1024lab.net  1024创新实验室版权所有'
  copyright: String;
  // 点击版权的跳转 'http://1024lab.net'
  copyrightUrl: String;
  // 版本: 2.0.0
  version: String;
  // build时间戳
  buildTime: String;
}

/**
 * 应用信息配置
 */
export interface AppConfig {
  // i18n 语言选择
  languageType: LanguageType;
  // 布局
  layout: string;
  // 主题
  theme: ThemeType;
  // 手机模式最大宽度 768px，如果少于768px，则手机自适应
  mobileMaxWidth: number;
  // 侧边菜单宽度 ， 默认为256px
  sideMenuWidth: number;
  // 标签页
  multiPageTagFlag: boolean;
  // 标签页缓存, keep-alive， true 开启缓存; false 不开启
  multiPageTagKeepAliveFlag: boolean;
  // 固定头部状态栏，true:固定，false:不固定
  headerFixedFlag: boolean;
  // 固定侧边栏，true:固定，false:不固定
  sideBarFixedFlag: boolean;
  // 隐藏设置，true:隐藏，false:不隐藏
  hideSettingFlag: false;
  // 首页页面Name
  homePageName: string;
}
