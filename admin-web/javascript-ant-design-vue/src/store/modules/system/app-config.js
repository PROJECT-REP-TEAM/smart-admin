/*
 * @Description:
 * @Author: zhuoda
 * @Date: 2021-08-03
 * @LastEditTime: 2022-06-22
 * @LastEditors: zhuoda
 */
import { defineStore } from 'pinia';
import localStorageKeyConst from '/@/constants/local-storage-key-const';
import { localRead, localSave } from '/@/utils/local-util';
import { appDefaultConfig } from '/@/config/app-config';

let state = { ...appDefaultConfig };

let appConfigStr = localRead(localStorageKeyConst.APP_CONFIG);
if (appConfigStr) {
  try {
    state = JSON.parse(appConfigStr);
  } catch (e) {

  }
}


export const useAppConfigStore = defineStore({
  id: 'appConfig',
  state: () => ({
    // 读取config下的默认配置
    ...state,
  }),
  actions: {
    reset () {
      for (const k in appDefaultConfig) {
        this[k] = appDefaultConfig[k];
      }
    }
  },
});

