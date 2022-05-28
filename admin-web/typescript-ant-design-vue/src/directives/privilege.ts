/*
 * @Description:
 * @version:
 * @Author: lidoudou
 * @Date: 2021-09-08 18:48:40
 * @LastEditors: LiHaiFan
 * @LastEditTime: 2021-10-11 15:25:43
 */
// 页面内按钮过滤
import { useUserStore } from '/@/store/modules/system/user';
import { DirectiveBinding } from 'vue';

export function inserted(el: { parentNode: { removeChild: (arg0: any) => void; }; }, binding: DirectiveBinding<any>) {
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
  // 有权限
  if (!(userPointsList && userPointsList.includes(binding.value))) {
    el.parentNode.removeChild(el);
  }
}
