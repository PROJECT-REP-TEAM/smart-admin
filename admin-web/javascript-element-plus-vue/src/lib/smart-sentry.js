/**
 * 报错收集
 */

import * as Sentry from "@sentry/vue";
import lodash from "lodash";
export const smartSentry = {
  /**
   * sentry 主动上报
   * @param {error} error 错误信息
   */
  captureException: (error) => {
    console.log(error);
    if (
      error.config &&
      error.data &&
      error &&
      error.headers &&
      error.request &&
      error.status
    ) {
      return;
    }
    // Sentry.captureException(error);
  },
  setUser: (userInfo) => {
    let user = lodash.cloneDeep(userInfo);
    delete user.menuTree;
    delete user.pointsList;
    Sentry.setUser(user);
  },
  clearUser: () => {
    Sentry.setUser(null);
  },
};
