/*
 * @Author: LiHaiFan
 * @Date: 2021-09-16 15:38:28
 * @LastEditTime: 2021-09-16 15:51:06
 * @LastEditors: LiHaiFan
 * @Description:
 * @FilePath: /xiaomifeng-crm-manage-web/src/plugins/privilege-plugin.ts
 */
import { privilege } from '../lib/privilege';
export default {
  install: (app) => {
    app.config.globalProperties.$privilege = privilege;
  },
};
