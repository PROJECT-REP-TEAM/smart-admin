/*
 * @Author: zhuoda
 * @Date: 2021-08-13 17:55:09
 * @LastEditTime: 2022-06-05 23:03:18
 * @LastEditors: LiHaiFan
 * @Description: eventBus
 * @FilePath: \typescript-element-plus-vue\src\views\system\employee\department\department-mitt.js
 */
import mitt from "mitt"
type Events = {
    selectTree: number;
  };
export default mitt<Events>();
