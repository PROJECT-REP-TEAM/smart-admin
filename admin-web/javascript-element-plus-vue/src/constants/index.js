/*
 * @Description:
 * @version:
 * @Author: lidoudou
 * @Date: 2021-08-11 18:14:13
 * @LastEditors: LiHaiFan
 * @LastEditTime: 2022-05-16 21:13:09
 */
import menu from './system/menu-enum';
import device from './system/device';
import category from './business/category';
import goods from './business/goods';
import dataTracer from './business/goods';
import file from './business/file';
import { FLAG_NUMBER_ENUM, GENDER_ENUM } from './common';

export default {
  FLAG_NUMBER_ENUM,
  GENDER_ENUM,
  ...device,
  ...menu,
  ...category,
  ...goods,
  ...dataTracer,
  ...file
};
