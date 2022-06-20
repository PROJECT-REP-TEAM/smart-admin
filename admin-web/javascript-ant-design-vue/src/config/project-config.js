/*
 * @Author: zhuoda
 * @Date: 2021-08-03 20:27:11
 * @LastEditTime: 2022-06-20
 * @LastEditors: zhuoda
 * @Description:
 * @FilePath: /smart-admin/src/config/project-config.ts
 */
import dayjs from 'dayjs';

/**
 * 项目默认配置
 */

export const projectDefaultConfig = {
  // 项目名称
  projectName: 'SmartAdmin',
  // 版权信息
  copyright: 'Copyright ©2015-' + dayjs().format('YYYY') + '版权所有: 1024创新实验室 ',
  // 点击版权的跳转
  copyrightUrl: 'http://www.1024lab.net',
  // 版本: 1.0.0
  version: '2.0.0-beta',
  // build时间戳
  buildTime: dayjs().format('YYYY-MM-DD HH:mm:ss'),
};
