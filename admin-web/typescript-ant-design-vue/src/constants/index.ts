/*
 * @Description:
 * @version:
 * @Author: lidoudou
 * @Date: 2021-08-11 18:14:13
 * @LastEditors: zhuoda
 * @LastEditTime: 2021-12-13
 */
import goods from './business/goods';
import menu from './system/menu';
import { FLAG_NUMBER_ENUM, GenderEnum } from './common';

export default {
  FLAG_NUMBER_ENUM,
  GenderEnum,
  ...goods,
  ...menu,
};
