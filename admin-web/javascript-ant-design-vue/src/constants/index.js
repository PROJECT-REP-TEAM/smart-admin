/*
 * @Description:
 * @version:
 * @Author: zhuoda
 * @Date: 2021-08-11 18:14:13
 * @LastEditors: zhuoda
 * @LastEditTime: 2021-09-01 20:55:07
 */
import menu from './system/menu/menu-enum';
import goods from './business/goods';
import category from './business/category';
import { FLAG_NUMBER_ENUM, GenderEnum } from './common';
import file from './business/file';

export default {
  FLAG_NUMBER_ENUM,
  GenderEnum,
  ...menu,
  ...goods,
  ...category,
  ...file
};
