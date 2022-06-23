/*
 * @Description:
 * @Author: zhuoda
 * @Date: 2021-11-05
 * @LastEditTime: 2022-06-23
 * @LastEditors: zhuoda
 */
import { homeRouters } from './system/home';
import { loginRouters } from './system/login';

export const routerArray = [...loginRouters, ...homeRouters];
