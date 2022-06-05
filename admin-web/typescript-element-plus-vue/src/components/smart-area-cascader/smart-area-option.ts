/*
 * @Description:地区类型
 * @Author: zhuoda
 * @Date: 2021-08-18
 * @LastEditTime: 2022-06-05 22:32:07
 * @LastEditors: LiHaiFan
 */

export interface SmartAreaOption {
  value?: number;
  label?: string;
  disabled?: boolean;
  children?: SmartAreaOption[];
}
