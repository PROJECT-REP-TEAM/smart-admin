/*
 * @Description:报错收集
 * @Author: zhuoda
 * @Date: 2021-08-27
 * @LastEditTime: 2021-08-27
 * @LastEditors: zhuoda
 */

// import * as Sentry from '@sentry/browser';
export const smartSentry = {
  /**
   * sentry 主动上报
   * @param {error} error 错误信息
   */
  captureException: (error) => {
    if (error.config && error.data && error && error.headers && error.request && error.status) {
      return;
    }
    // Sentry.captureException(error);
    console.log(error);
  },
};
