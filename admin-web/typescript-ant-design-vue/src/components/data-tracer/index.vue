<!--
 * @Description:
 * @version:
 * @Author: lidoudou
 * @Date: 2021-08-27 17:55:48
 * @LastEditors: qq:23983208
 * @LastEditTime: 2022-03-09 16:02:18
-->
<template>
  <a-card size="small"
    :bordered="false">
    <a-table size="small"
      :dataSource="tableData"
      :columns="columns"
      :rowKey="(record,index)=>{return index}"
      :pagination="false"
      bordered />

    <div class="smart-query-table-page">
      <a-pagination showSizeChanger
        showQuickJumper
        show-less-items
        :pageSizeOptions="PAGE_SIZE_OPTIONS"
        :defaultPageSize="queryForm.pageSize"
        v-model:current="queryForm.pageNum"
        v-model:pageSize="queryForm.pageSize"
        :total="total"
        @change="ajaxQuery"
        @showSizeChange="ajaxQuery"
        :show-total="(total) => `共${total}条`" />
    </div>
  </a-card>
</template>
<script lang="ts" setup>
import { reactive, onMounted, ref, watch } from 'vue';
import type { ResponseModel } from '/@/api/base-model/response-model';
import { PageResultModel } from '/@/api/base-model/page-result-model';
import { PAGE_SIZE_OPTIONS, PAGE_SIZE } from '/@/constants/common';
import { DataTracerQueryForm } from '/@/api/business/data-tracer/model/data-tracer-query-form';
import { DataTracerVo } from '/@/api/business/data-tracer/model/data-tracer-vo';
import { dataTracerApi } from '/@/api/business/data-tracer/data-tracer-api';
import { smartSentry } from '/@/lib/smart-sentry';

const props = defineProps<{
  businessId: number;
  businessType: number;
}>();
const columns = reactive([
  {
    title: '操作时间',
    dataIndex: 'createTime',
    width: 150,
  },
  {
    title: '操作人',
    dataIndex: 'operatorName',
    width: 80,
  },
  {
    title: '操作类型',
    dataIndex: 'operateTypeDesc',
    width: 80,
  },
  {
    title: '操作备注',
    dataIndex: 'operateContent',
  },
]);

const queryFormState: DataTracerQueryForm = {
  pageNum: 1,
  pageSize: PAGE_SIZE,
  businessId: props.businessId,
  businessType: props.businessType,
};
const queryForm = reactive<DataTracerQueryForm>({ ...queryFormState });
const tableLoading = ref<Boolean>(false);
const tableData = ref<DataTracerVo[]>([]);
const total = ref<Number>(0);

function resetQuery() {
  Object.assign(queryForm, queryFormState);
  ajaxQuery();
}
async function ajaxQuery() {
  try {
    tableLoading.value = true;
    let responseModel: ResponseModel<PageResultModel<DataTracerVo>> = await dataTracerApi.queryDataTracerLogList(queryForm);
    const list = responseModel.data.list;
    total.value = responseModel.data.total;
    tableData.value = list;
  } catch (e) {
    console.log(e);
    smartSentry.captureException(e);
  } finally {
    tableLoading.value = false;
  }
}
// ========= 定义 watch 监听 ===============
watch(
  () => props.businessId,
  (e) => {
    if (e) {
      queryForm.businessId = e;
      ajaxQuery();
    }
  },
  { immediate: true }
);
onMounted(ajaxQuery);
</script>
