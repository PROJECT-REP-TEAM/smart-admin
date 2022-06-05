/*
 * @Author: zhuoda
 * @Date: 2021-08-03 10:27:11
 * @LastEditTime: 2021-09-01
 * @LastEditors: zhuoda
 * @Description:
 * @FilePath: /smart-admin/src/config/project-config.ts
 */
import { ProjectConfig } from '/@/types/config';
import moment from 'moment';

/**
 * 项目默认配置
 */

export const projectDefaultConfig: ProjectConfig = {
  // 项目名称
  projectName: 'SmartAdmin',
  // 版权信息
  copyright: 'Copyright ©2015-' + moment().format('YYYY') + '版权所有: 1024创新实验室 ',
  // 点击版权的跳转
  copyrightUrl: 'http://www.1024lab.net',
  // 版本: 1.0.0
  version: '2.0.0-beta',
  // build时间戳
  buildTime: moment().format('YYYY-MM-DD HH:mm:ss'),
};
