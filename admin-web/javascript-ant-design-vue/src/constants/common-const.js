/*
 * @Description:
 * @Author: zhuoda
 * @Date: 2021-08-03
 * @LastEditTime: 2022-06-17
 * @LastEditors: zhuoda
 */

export const PAGE_SIZE = 15;

export const PAGE_SIZE_OPTIONS = ['10', '15', '20', '30', '40', '50', '75', '100', '150', '200', '300', '500'];

//登录页面名字
export const PAGE_PATH_LOGIN = '/login';

//首页页面名字
export const PAGE_PATH_HOME = '/home';

//404页面名字
export const PAGE_PATH_404 = '/404';

export const showTableTotal = function (total) {
  return `共${total}条`;
};

export const FLAG_NUMBER_ENUM = {
  TRUE: {
    value: 1,
    desc: '是',
  },
  FALSE: {
    value: 0,
    desc: '否',
  },
};

export const GENDER_ENUM = {
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
