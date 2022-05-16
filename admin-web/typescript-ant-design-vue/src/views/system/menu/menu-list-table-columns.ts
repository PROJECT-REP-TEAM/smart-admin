/*
 * @Description:表格列
 * @Author: zhuoda
 * @Date: 2021-08-26
 * @LastEditTime: 2021-09-16 10:04:45
 * @LastEditors: LiHaiFan
 */

import { reactive } from 'vue';
export const columns = reactive([
  {
    title: '名称',
    dataIndex: 'menuName',
    key: 'ID',
    width: 200,
  },
  {
    title: '类型',
    dataIndex: 'menuType',
  },
  {
    title: '图标',
    dataIndex: 'icon',
    width: 50,
  },
  {
    title: '权限',
    dataIndex: 'webPerms',
  },
  {
    title: '外链',
    dataIndex: 'frameFlag',
  },
  {
    title: '缓存',
    dataIndex: 'cacheFlag',
  },
  {
    title: '禁用',
    dataIndex: 'disabledFlag',
  },
  {
    title: '顺序',
    dataIndex: 'sort',
  },
  {
    title: '创建时间',
    dataIndex: 'createTime',
    width: 180,
  },
  {
    title: '操作',
    dataIndex: 'operate',
    width: 150,
  },
]);
