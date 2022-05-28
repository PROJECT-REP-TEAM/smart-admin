<!--
 * @Description:
 * @version:
 * @Author: lidoudou
 * @Date: 2021-08-27 17:55:48
 * @LastEditors: qq:23983208
 * @LastEditTime: 2022-03-09 16:02:18
-->
<template>
  <el-card size='small'
           :bordered='false'>
    <el-table size='small'
              :data='tableData'
              :rowKey='(record,index)=>{return index}'
              bordered>
      <el-table-column prop='createTime' label='操作时间' width='150'></el-table-column>
      <el-table-column prop='operatorName' label='操作人' width='80'></el-table-column>
      <el-table-column prop='operateTypeDesc' label='操作类型' width='80'></el-table-column>
      <el-table-column prop='operateContent' label='操作备注'></el-table-column>
    </el-table>

    <div class='smart-query-table-page'>
      <el-pagination
        background
        v-model:currentPage='queryForm.pageNum'
        v-model:page-size='queryForm.pageSize'
        :page-sizes='PAGE_SIZE_OPTIONS'
        layout='total, sizes, prev, pager, next, jumper'
        :total='total'
        @size-change='ajaxQuery'
        @current-change='ajaxQuery'
      />
    </div>
  </el-card>
</template>
<script setup>
  import { onMounted, reactive, ref, watch } from 'vue';
  import { PAGE_SIZE, PAGE_SIZE_OPTIONS } from '/@/constants/common';
  import { dataTracerApi } from '/@/api/support/data-tracer-api';
  import { smartSentry } from '/@/lib/smart-sentry';

  const props = defineProps({
    businessId: Number,
    businessType: Number,
  });
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

  const queryFormState = {
    pageNum: 1,
    pageSize: PAGE_SIZE,
    businessId: props.businessId,
    businessType: props.businessType,
  };
  const queryForm = reactive({ ...queryFormState });
  const tableLoading = ref(false);
  const tableData = ref([]);
  const total = ref(0);

  function resetQuery() {
    Object.assign(queryForm, queryFormState);
    ajaxQuery();
  }

  async function ajaxQuery() {
    try {
      tableLoading.value = true;
      let responseModel = await dataTracerApi.queryDataTracerLogList(queryForm);
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
    { immediate: true },
  );
  onMounted(ajaxQuery);
</script>
