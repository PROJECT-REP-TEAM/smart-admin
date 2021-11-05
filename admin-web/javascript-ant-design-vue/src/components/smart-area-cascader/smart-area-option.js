/*
 * @Description:地区类型
 * @Author: zhuoda
 * @Date: 2021-08-18
 * @LastEditTime: 2021-08-18
 * @LastEditors: zhuoda
 */

export interface SmartAreaOption {
  value: number;
  label: string;
  disabled?: boolean;
  children?: SmartAreaOption[];
}
