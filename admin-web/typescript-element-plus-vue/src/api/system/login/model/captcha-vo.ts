/*
 * @Author: zhuoda
 * @Date: 2021-08-03 22:27:11
 * @LastEditTime: 2022-01-07
 * @LastEditors: zhuoda
 * @Description:
 */

/**
 *
 * @export
 * @interface CaptchaVO
 */
export interface CaptchaVO {
  /**
   * 验证码唯一标识
   * @type {string}
   * @memberof CaptchaVO
   */
  captchaUuid: string;
  /**
   * 验证码Base64图片
   * @type {string}
   * @memberof CaptchaVO
   */
  captchaBase64Image: string;
}
