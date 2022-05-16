/*
 * @Description:
 * @version:
 * @Author: lidoudou
 * @Date: 2021-08-11 18:14:13
 * @LastEditors: LiHaiFan
 * @LastEditTime: 2022-05-16 21:13:09
 */
import goods from './business/goods';
import menu from './system/menu';
import category from './business/category';
import { FLAG_NUMBER_ENUM, GenderEnum } from './common';

export default {
  FLAG_NUMBER_ENUM,
  GenderEnum,
  ...goods,
  ...menu,
  ...category
};
