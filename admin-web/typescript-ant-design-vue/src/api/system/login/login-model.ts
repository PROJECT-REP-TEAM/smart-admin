/*
 * @Author: zhuoda
 * @Date: 2021-08-03 10:27:11
 * @LastEditTime: 2021-08-18 20:03:44
 * @LastEditors: zhuoda
 * @Description: 
 * @FilePath: /smart-admin/src/api/system/login/login-model.ts
 */

/**
 *
 * @export
 * @interface EmployeeLoginFormDto
 */
export interface EmployeeLoginFormDto {
    /**
     * 验证码
     * @type {string}
     * @memberof EmployeeLoginFormDto
     */
    code?: string;
    /**
     * 验证码uuid
     * @type {string}
     * @memberof EmployeeLoginFormDto
     */
    codeUuid?: string;
    /**
     *
     * @type {string}
     * @memberof EmployeeLoginFormDto
     */
    loginName: string;
    /**
     *
     * @type {string}
     * @memberof EmployeeLoginFormDto
     */
    loginPwd: string;
}
