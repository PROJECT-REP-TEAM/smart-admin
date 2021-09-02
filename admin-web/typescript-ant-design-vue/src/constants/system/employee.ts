/*
 * @Author: zhuoda
 * @Date: 2021-08-16 15:12:42
 * @LastEditTime: 2021-08-27
 * @LastEditors: zhuoda
 * @Description:
 * @FilePath: /smart-admin/src/constants/system/employee.ts
 */
import { SmartEnum } from '/@/types/smart-enum';

export const GENDER_ENUM: SmartEnum<number> = {
  UNKNOWN: {
    value: 0,
    desc: '未知',
  },
  MAN: {
    value: 1,
    desc: '男',
  },
  WOMAN: {
    value: 2,
    desc: '女',
  },
};

export default {
  GENDER_ENUM,
};
