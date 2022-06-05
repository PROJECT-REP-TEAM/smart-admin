/*
 * @Author: zhuoda
 * @Date: 2021-08-12 17:53:15
 * @LastEditTime: 2021-09-01
 * @LastEditors: zhuoda
 * @Description:
 * @FilePath: /smart-admin/src/api/system/department/model/department-tree-vo.ts
 */
import {DepartmentVo} from "/@/api/system/department/model/department-vo";

/**
 * The version of the OpenAPI document: 1.0.0
 *
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */

export interface DepartmentTreeVo extends DepartmentVo {
  /**
   * 子部门
   */
  children?: Array<DepartmentTreeVo>;

  /**
   * 同级下一个元素id
   */
  nextId?: number;
  /**
   * 同级上一个元素id
   */
  preId?: number;
}
