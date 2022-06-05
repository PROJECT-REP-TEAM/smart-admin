/*
 * @Description:
 * @Author: zhuoda
 * @Date: 2021-08-11
 * @LastEditTime: 2021-09-01 22:31:37
 * @LastEditors: zhuoda
 */
import { SmartEnum } from '/@/types/smart-enum';

// 商品分类
export const GOODS_TYPE_ENUM: SmartEnum<number> = {
  BOOK: {
    value: 1,
    desc: '图书',
  },
  COURSE: {
    value: 2,
    desc: '课程',
  },
};
export default {
  GOODS_TYPE_ENUM,
};
