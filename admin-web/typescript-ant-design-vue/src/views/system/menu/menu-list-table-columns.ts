/*
 * @Description:表格列
 * @Author: zhuoda
 * @Date: 2021-08-26
 * @LastEditTime: 2021-09-01 20:59:53
 * @LastEditors: zhuoda
 */

import { reactive } from 'vue';
export const columns = reactive([
  {
    title: '名称',
    dataIndex: 'menuName',
    key: 'ID',
  },
  {
    title: '类型',
    dataIndex: 'menuType',
    slots: { customRender: 'type' },
  },
  {
    title: '图标',
    dataIndex: 'icon',
    slots: { customRender: 'icon' },
    width: 50,
  },
  {
    title: '组件',
    dataIndex: 'component',
    ellipsis: true,
  },
  {
    title: '路由',
    dataIndex: 'path',
    ellipsis: true,
  },
  {
    title: '权限',
    dataIndex: 'perms',
    ellipsis: true,
  },
  {
    title: '外链',
    dataIndex: 'frameFlag',
    slots: { customRender: 'frameFlag' },
  },
  {
    title: '缓存',
    dataIndex: 'cacheFlag',
    slots: { customRender: 'cacheFlag' },
  },
  {
    title: '禁用',
    dataIndex: 'disabledFlag',
    slots: { customRender: 'disabledFlag' },
  },
  {
    title: '顺序',
    dataIndex: 'sort',
  },
  {
    title: '创建时间',
    dataIndex: 'createTime',
    width: 150,
  },
  {
    title: '操作',
    dataIndex: 'menuId',
    slots: { customRender: 'operate' },
    width: 150,
  },
]);
