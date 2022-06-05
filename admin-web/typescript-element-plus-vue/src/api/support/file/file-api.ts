/*
 * @Description:
 * @version:
 * @Author: zhuoda
 * @Date: 2021-08-17 23:32:36
 * @LastEditors: zhuoda
 * @LastEditTime: 2021-08-18 14:35:52
 */
import { ResponseModel } from '../../base-model/response-model';
import { FileUploadVo } from './model/file-upload-vo';
import { postRequest } from '/@/lib/axios';

export const fileApi = {
  // 文件上传 by zhuoda
  uploadUrl: '/file/upload',
  uploadFile: (param: any, folder: number) => {
    return postRequest<ResponseModel<FileUploadVo>>(`/support/file/upload?folder=${folder}`, param);
  },
};
