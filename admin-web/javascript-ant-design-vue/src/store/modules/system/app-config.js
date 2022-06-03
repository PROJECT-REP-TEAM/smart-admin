/*
 * @Description:
 * @Author: zhuoda
 * @Date: 2021-08-03
 * @LastEditTime: 2022-06-02
 * @LastEditors: zhuoda
 */
import { defineStore } from 'pinia';
import { appDefaultConfig } from '/@/config/app-config';

export const useAppConfigStore = defineStore({
  id: 'appConfig',
  state: () => ({
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
    getPageTagFixedFlag (state) {
      return state.headerFixedFlag;
    },
  },

  actions: {
    // 更新当前屏幕宽度
    setCurrentScreenWidth (clientWidth) {
      this.currentScreenWidth = clientWidth;
      // 如果当前宽度小于设置的手机最大宽度，则为手机css适配模式
      this.isMobile = this.currentScreenWidth < this.mobileMaxWidth;
    },
    // 更新语言
    setLanguage (language) {
      console.log(language);
    },
  },
});
