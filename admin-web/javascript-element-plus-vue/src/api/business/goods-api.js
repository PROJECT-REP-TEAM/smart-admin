import { postRequest } from '/@/lib/axios';

export const goodsApi = {
  // 添加商品 by zhuoda
  addGoods: (param) => {
    return postRequest('/goods/add', param);
  },
  // 删除 by zhuoda
  deleteGoods: (param) => {
    return postRequest('/goods/del', param);
  },
  // 分页查询 by zhuoda
  queryGoodsList: (param) => {
    return postRequest('/goods/query', param);
  },
  // 更新商品 by zhuoda
  updateGoods: (param) => {
    return postRequest('/goods/update', param);
  },
};
