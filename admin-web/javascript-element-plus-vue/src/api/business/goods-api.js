import { postRequest } from '/@/lib/axios';

export const goodsApi = {
  // 添加商品 by zhuoda
  addGoods: (param) => {
    return postRequest('/admin/goods/add', param);
  },
  // 删除 by zhuoda
  deleteGoods: (param) => {
    return postRequest('/admin/goods/del', param);
  },
  // 分页查询 by zhuoda
  queryGoodsList: (param) => {
    return postRequest('/admin/goods/query', param);
  },
  // POST /admin/goods/update
  // 更新商品 by zhuoda
  updateGoods: (param) => {
    return postRequest('/admin/goods/update', param);
  },
};
