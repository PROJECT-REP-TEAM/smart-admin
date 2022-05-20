import { postRequest } from '/@/lib/axios';
import { ResponseModel } from '/@/api/base-model/response-model';
import {GoodsAddForm} from "./model/goods-add-form";
import {GoodsDelDto} from "./model/goods-del-dto";
import {GoodsQueryForm} from "./model/goods-query-form";
import { GoodsUpdateForm } from './model/goods-update-form';
import { GoodsVo } from './model/goods-vo';
import { PageResultModel } from '../../base-model/page-result-model';

export const goodsApi = {
  // 添加商品 by zhuoda
  addGoods: (param: GoodsAddForm) => {
    return postRequest<ResponseModel<String>>('/admin/goods/add', param);
  },
  // 删除 by zhuoda
  deleteGoods: (param: GoodsDelDto) => {
    return postRequest<ResponseModel<String>>('/admin/goods/del', param);
  },
  // 分页查询 by zhuoda
  queryGoodsList: (param: GoodsQueryForm) => {
    return postRequest<ResponseModel<PageResultModel<GoodsVo>>>('/admin/goods/query', param);
  },
  // POST /admin/goods/update
  // 更新商品 by zhuoda
  updateGoods: (param: GoodsUpdateForm) => {
    return postRequest<ResponseModel<String>>('/admin/goods/update', param);
  }
};
