export const PAGE_SIZE = 10;

export const PAGE_SIZE_OPTIONS = ['10', '15', '25', '35', '45', '55', '100', '150', '200', '300', '500'];
export const showTableTotal = function (total) {
  return `共${total}条`;
};

//登录页面名字
export const PAGE_NAME_LOGIN = 'Login';
//注册页面名字
export const PAGE_NAME_REGISTER = 'Register';

//首页页面名字
export const PAGE_NAME_HOME = 'Home';

//404页面名字
export const PAGE_NAME_404 = '404';

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

/**
 * 性别
 */
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
