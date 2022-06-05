<template>
  <el-form class="smart-query-form">
    <el-row class="smart-query-form-row">
      <el-form-item label="商品名称" class="smart-query-form-item">
        <el-input style="width: 300px" v-model="queryForm.searchWord" placeholder="商品名称" />
      </el-form-item>

      <el-form-item label="商品分类" class="smart-query-form-item">
        <category-tree v-model:value="queryForm.categoryId" placeholder="请选择商品分类" :categoryType="CATEGORY_TYPE_ENUM.GOODS.value" />
      </el-form-item>

      <el-form-item label="快速筛选" class="smart-query-form-item">
        <el-radio-group v-model="queryForm.shelvesFlag" @change="ajaxQuery">
          <el-radio-button :label="undefined">全部</el-radio-button>
          <el-radio-button :label="true">上架</el-radio-button>
          <el-radio-button :label="false">下架</el-radio-button>
        </el-radio-group>
      </el-form-item>

      <el-form-item class="smart-query-form-item smart-margin-left10">
        <el-button type="primary" @click="ajaxQuery" icon="Search">查询</el-button>
        <el-button @click="resetQuery" icon="RefreshLeft">重置</el-button>
      </el-form-item>
    </el-row>
  </el-form>

  <el-card shadow="hover">
    <el-row class="smart-table-btn-block">
      <div class="smart-table-operate-block">
        <el-button @click="addGoods" type="primary" size="small" icon="Plus">新建</el-button>
        <el-button @click="confirmBatchDelete" type="danger" size="small" :disabled="selectedRowKeys.length == 0" icon="Delete">批量删除 </el-button>
      </div>
    </el-row>

    <el-table size="small" :data="tableData" border :rowKey="rowKey" v-loading="tableLoading" @select="onSelect" @select-all="onSelect">
      <el-table-column type="selection" width="55" />
      <el-table-column prop="categoryName" label="商品分类"></el-table-column>
      <el-table-column prop="goodsType" label="商品类型">
        <template #default="scope">
          <span>{{ $smartEnumPlugin.getDescByValue('GOODS_TYPE_ENUM', scope.row.goodsType) }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="goodsName" label="商品名称"></el-table-column>
      <el-table-column prop="price" label="商品价格"></el-table-column>
      <el-table-column prop="shelvesFlag" label="商品状态">
        <template #default="scope">
          <span>{{ scope.row.shelvesFlag ? '上架' : '下架' }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="remark" label="备注"></el-table-column>
      <el-table-column prop="createTime" label="创建时间"></el-table-column>
      <el-table-column fixed="right" label="操作">
        <template #default="scope">
          <el-button @click="addGoods(scope.row)" link type="primary">编辑</el-button>
          <el-button @click="showDataTracer(scope.row.goodsId)" link type="primary">操作记录</el-button>
        </template>
      </el-table-column>
    </el-table>

    <div class="smart-query-table-page">
      <el-pagination
        background
        v-model:currentPage="queryForm.pageNum"
        v-model:page-size="queryForm.pageSize"
        :page-sizes="PAGE_SIZE_OPTIONS"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
        @size-change="ajaxQuery"
        @current-change="ajaxQuery"
      >
      </el-pagination>
    </div>

    <GoodsOperateModal ref="operateModal" @reloadList="ajaxQuery" />
    <!-- 商品的操作记录 -->
    <GoodsDataTracerList ref="dataTracerRef" />
  </el-card>
</template>
<script lang="ts" setup>
  import { ElMessage, ElMessageBox } from 'element-plus';
  import GoodsOperateModal from './components/goods-operate-modal.vue';
  import GoodsDataTracerList from './components/goods-data-tracer-modal.vue';
  import { reactive, ref, onMounted } from 'vue';
  import { PAGE_SIZE_OPTIONS } from '/@/constants/common';
  import { useSpinStore } from '/@/store/modules/system/spin';
  import { goodsApi } from '/@/api/business/goods/goods-api';
  import lodash from 'lodash';
  import { CATEGORY_TYPE_ENUM } from '/@/constants/business/category';
  import CategoryTree from '/@/components/category-tree-select/index.vue';
  import { GoodsQueryForm } from '/@/api/business/goods/model/goods-query-form';
  import { GoodsVo } from '/@/api/business/goods/model/goods-vo';
  import { GoodsUpdateForm } from '/@/api/business/goods/model/goods-update-form';

  const queryFormState: GoodsQueryForm = {
    searchWord: '',
    categoryId: undefined,
    shelvesFlag: undefined,
    goodsType: undefined,
    pageNum: 1,
    pageSize: 10,
  };
  const queryForm = reactive<GoodsQueryForm>({ ...queryFormState });
  const tableLoading = ref(false);
  const rowKey = ref('goodsId');
  const selectedRowKeys = ref<number[]>([]);
  const selectionRows = ref<GoodsVo[]>([]);
  const tableData = ref<GoodsVo[]>([]);
  const total = ref(0);
  const operateModal = ref();
  const dataTracerRef = ref();

  // 选择行
  async function onSelect(selection: GoodsVo[]) {
    // 找到当页未被勾选项
    let unchecked = lodash.xorBy(tableData.value, selection, rowKey.value);
    // 合并当前与选中项
    selectedRowKeys.value = lodash.unionBy(
      selectedRowKeys.value,
      selection.map((e: any) => e[rowKey.value])
    );
    selectionRows.value = lodash.unionBy(selectionRows.value, selection, rowKey.value);
    // 移除未被勾选项
    selectedRowKeys.value = lodash.differenceBy(
      selectedRowKeys.value,
      unchecked.map((e: any) => e[rowKey.value])
    );
    selectionRows.value = lodash.differenceBy(selectionRows.value, unchecked, rowKey.value);
  }

  // 显示操作记录弹窗
  function showDataTracer(goodsId: number) {
    dataTracerRef.value.showDrawer(goodsId);
  }

  function resetQuery() {
    Object.assign(queryForm, queryFormState);
    ajaxQuery();
  }

  async function ajaxQuery() {
    try {
      tableLoading.value = true;
      let responseModel = await goodsApi.queryGoodsList(queryForm);
      const list = responseModel.data.list;
      total.value = responseModel.data.total;
      tableData.value = list;
    } catch (e) {
      console.log(e);
    } finally {
      tableLoading.value = false;
    }
  }

  function confirmBatchDelete() {
    ElMessageBox.confirm('确定要删除选中商品吗?', '提示', {
      confirmButtonText: '删除',
      cancelButtonText: '取消',
      type: 'error',
    })
      .then(() => {
        batchDelete();
      })
      .catch(() => {});
  }

  const batchDelete = async () => {
    try {
      useSpinStore().show();
      let deleteDto = {
        goodsIdList: selectedRowKeys.value,
      };
      await goodsApi.deleteGoods(deleteDto);
      ElMessage.success('删除成功');
      await ajaxQuery();
    } catch (e) {
      console.log(e);
    } finally {
      useSpinStore().hide();
    }
  };

  function addGoods(goodsData?: GoodsUpdateForm) {
    operateModal.value.showDrawer(goodsData);
  }

  onMounted(ajaxQuery);
</script>
