/*
 * @Description:
 * @version:
 * @Author: zhuoda
 * @Date: 2021-08-18 10:41:31
 * @LastEditors: zhuoda
 * @LastEditTime: 2021-08-18 18:02:18
 */
export interface FileUploadVo {
  /**
   * 文件id
   */
  fileId?: number;
  /**
   * fileKey
   */
  fileKey: string;
  /**
   * 文件名称
   */
  fileName?: string;
  /**
   * 文件大小
   */
  fileSize?: number;
  /**
   * 文件类型
   */
  fileType?: string;
  /**
   * fileUrl
   */
  fileUrl?: string;
  /**
   *  自定义的url用于文件上传的展示
   */
  url?: string;
}
