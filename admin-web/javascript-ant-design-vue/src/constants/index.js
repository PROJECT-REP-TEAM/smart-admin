/*
 * @Description:
 * @version:
 * @Author: zhuoda
 * @Date: 2021-08-11 18:14:13
 * @LastEditors: zhuoda
 * @LastEditTime: 2022-06-02
 */
import menu from './system/menu/menu-enum';
import goods from './business/goods';
import category from './business/category';
import { LOGIN_DEVICE_ENUM } from './system/login-device';
import { FLAG_NUMBER_ENUM, GenderEnum } from './common';
import file from './business/file';

export default {
  FLAG_NUMBER_ENUM,
  LOGIN_DEVICE_ENUM,
  GenderEnum,
  ...menu,
  ...goods,
  ...category,
  ...file
};
