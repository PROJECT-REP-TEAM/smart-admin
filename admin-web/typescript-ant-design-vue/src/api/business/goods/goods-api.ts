import { postRequest } from '/@/lib/axios';
import { ResponseModel } from '/@/api/base-model/response-model';
import {GoodsAddForm} from "./model/goods-add-form";
import {GoodsDelDto} from "./model/goods-del-dto";
import {GoodsQueryDto} from "./model/goods-query-dto";
import { GoodsUpdateForm } from './model/goods-update-form';
import { GoodsAdminVo } from './model/goods-admin-vo';
import { PageResultModel } from '../../base-model/page-result-model';

export const goodsApi = {
  // 添加商品 by zhuoda
  addGoods: (param: GoodsAddForm) => {
    return postRequest<ResponseModel<String>>('/admin/goods/add', param);
  },
  // POST /admin/goods/del
  // 删除 by zhuoda
  deleteGoods: (param: GoodsDelDto) => {
    return postRequest<ResponseModel<String>>('/admin/goods/del', param);
  },
  // POST /admin/goods/query
  // 分页查询 by zhuoda
  queryGoodsList: (param: GoodsQueryDto) => {
    return postRequest<ResponseModel<PageResultModel<GoodsAdminVo>>>('/admin/goods/query', param);
  },
  // POST /admin/goods/update
  // 更新商品 by zhuoda
  updateGoods: (param: GoodsUpdateForm) => {
    return postRequest<ResponseModel<String>>('/admin/goods/update', param);
  }
};
