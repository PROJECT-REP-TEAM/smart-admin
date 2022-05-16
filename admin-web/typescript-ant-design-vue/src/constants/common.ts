/*
 * @Description:
 * @Author: zhuoda
 * @Date: 2021-08-03
 * @LastEditTime: 2022-05-16 20:36:48
 * @LastEditors: LiHaiFan
 */
import { SmartEnum } from '/@/types/smart-enum';

export const PAGE_SIZE: number = 15;

export const PAGE_SIZE_OPTIONS: string[] = ['10', '15', '25', '35', '45', '55', '100', '150', '200', '300', '500'];

//登录页面名字
export const PAGE_NAME_LOGIN = 'Login';

//首页页面名字
export const PAGE_NAME_HOME = 'Home';

//404页面名字
export const PAGE_NAME_404 = '404';

export const FLAG_NUMBER_ENUM: SmartEnum<number> = {
  TRUE: {
    value: 1,
    desc: '是',
  },
  FALSE: {
    value: 0,
    desc: '否',
  },
};

export const GenderEnum: SmartEnum<number> = {
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
