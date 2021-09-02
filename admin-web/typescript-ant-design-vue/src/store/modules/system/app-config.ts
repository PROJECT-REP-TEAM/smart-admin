/*
 * @Description:
 * @Author: zhuoda
 * @Date: 2021-08-03
 * @LastEditTime: 2021-08-27
 * @LastEditors: zhuoda
 */
import { defineStore } from 'pinia';
import { AppConfig } from '/@/types/config';
import { appDefaultConfig } from '/@/config/app-config';

/**
 * app 所有状态
 */
interface AppConfigState extends AppConfig {
  currentScreenWidth: number; // 当前屏幕宽度
  isMobile: boolean; // 是否为手机端模式; 是：true, 不是 false
}

export const useAppConfigStore = defineStore({
  id: 'appConfig',
  state: (): AppConfigState => ({
    //当前屏幕宽度
    currentScreenWidth: document.body.clientWidth,
    // 是否为手机端模式，默认不是
    isMobile: false,
    // 读取config下的默认配置
    ...appDefaultConfig,
  }),
  getters: {
    /**
     * 只有在头部header 固定的时候，才会把标签固定
     * @param state
     */
    getPageTagFixedFlag(state): Boolean {
      return state.headerFixedFlag;
    },
  },

  actions: {
    // 更新当前屏幕宽度
    setCurrentScreenWidth(clientWidth: number): void {
      this.currentScreenWidth = clientWidth;
      // 如果当前宽度小于设置的手机最大宽度，则为手机css适配模式
      this.isMobile = this.currentScreenWidth < this.mobileMaxWidth;
    },
    // 更新语言
    setLanguage(language: string): void {
      console.log(language);
    },
  },
});
