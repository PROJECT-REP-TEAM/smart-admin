<!--
 * @Description:
 * @version:
 * @Author: lidoudou
 * @Date: 2021-08-27 17:55:48
 * @LastEditors: LiHaiFan
 * @LastEditTime: 2022-06-05 17:27:27
-->
<template>
  <el-card size="small" :bordered="false">
    <el-table
      size="small"
      :data="tableData"
      :rowKey="
        (record, index) => {
          return index;
        }
      "
      bordered
    >
      <el-table-column prop="createTime" label="操作时间" width="150"></el-table-column>
      <el-table-column prop="operatorName" label="操作人" width="80"></el-table-column>
      <el-table-column prop="operateTypeDesc" label="操作类型" width="80"></el-table-column>
      <el-table-column prop="operateContent" label="操作备注"></el-table-column>
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
      />
    </div>
  </el-card>
</template>
<script lang="ts" setup>
  import { reactive, onMounted, ref, watch } from 'vue';
  import type { ResponseModel } from '/@/api/base-model/response-model';
  import { PageResultModel } from '/@/api/base-model/page-result-model';
  import { PAGE_SIZE_OPTIONS, PAGE_SIZE } from '/@/constants/common';
  import { DataTracerQueryForm } from '/@/api/support/data-tracer/model/data-tracer-query-form';
  import { DataTracerVo } from '/@/api/support/data-tracer/model/data-tracer-vo';
  import { dataTracerApi } from '/@/api/support/data-tracer/data-tracer-api';
  import { smartSentry } from '/@/lib/smart-sentry';
  const props = defineProps<{
    businessId: number;
    businessType: number;
  }>();

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
