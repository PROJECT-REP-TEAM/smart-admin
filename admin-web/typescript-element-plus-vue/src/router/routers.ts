/*
 * @Author: LiHaiFan
 * @Date: 2022-06-05 15:26:04
 * @LastEditTime: 2022-06-05 18:06:22
 * @LastEditors: LiHaiFan
 * @Description: 
 * @FilePath: \typescript-element-plus-vue\src\router\routers.ts
 */
import { error404Routers } from './system/error';
import { homeRouters } from './system/home';
import { loginRouters } from './system/login';
import type {RouteRecordRaw} from "vue-router";

export const routerArray:RouteRecordRaw[] = [
  ...loginRouters,
  ...homeRouters,
  ...error404Routers,
];
