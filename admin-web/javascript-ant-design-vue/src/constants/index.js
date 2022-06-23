/*
 * @Description:
 * @version:
 * @Author: zhuoda
 * @Date: 2021-08-11 18:14:13
 * @LastEditors: zhuoda
 * @LastEditTime: 2022-06-23
 */
import menu from './system/menu-const'
import goods from './business/goods-const'
import category from './business/category-const'
import { LOGIN_DEVICE_ENUM } from './system/login-device-const'
import { FLAG_NUMBER_ENUM, GENDER_ENUM } from './common-const'
import { LAYOUT_ENUM } from './layout-const'
import file from './business/file-const'

export default {
  FLAG_NUMBER_ENUM,
  LOGIN_DEVICE_ENUM,
  GENDER_ENUM,
  LAYOUT_ENUM,
  ...menu,
  ...goods,
  ...category,
  ...file,
}
