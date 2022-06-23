/*
 * @Author: LiHaiFan
 * @Date: 2021-4-16 21:38:28
 * @LastEditTime: 2022-06-23
 * @LastEditors: zhuoda
 * @Description:
 */

const privilege = (value) => {
  // 超级管理员
  if (useUserStore().administratorFlag) {
    return true;
  }
  // 获取功能点权限
  let userPointsList = useUserStore().getPointList;
  if (!userPointsList) {
    return false;
  }
  return userPointsList && userPointsList.includes(value);
};

export default {
  install: (app) => {
    app.config.globalProperties.$privilege = privilege;
  },
};
