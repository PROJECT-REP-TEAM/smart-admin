/*
 * @Author: LiHaiFan
 * @Date: 2021-09-16 15:50:20
 * @LastEditTime: 2021-10-11 15:26:12
 * @LastEditors: LiHaiFan
 * @Description:
 * @FilePath: /xiaomifeng-crm-manage-web/src/lib/privilege.ts
 */
import { useUserStore } from '/@/store/modules/user';

export const privilege = (value) => {
  let userInfo = useUserStore().getUserInfo;
  // 超级管理员
  if (userInfo.administratorFlag) {
    return true;
  }
  // 获取功能点权限
  let userPointsList = useUserStore().getPointList;
  if (!userPointsList) {
    return false;
  }
  return userPointsList && userPointsList.includes(value);
};
