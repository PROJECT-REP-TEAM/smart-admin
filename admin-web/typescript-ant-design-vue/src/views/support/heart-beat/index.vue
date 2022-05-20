<template>

  <a-card size="small" :bordered="false" :hoverable="true">
    <a-table
      :scroll="{ x: 1300 }"
      size="small"
      :dataSource="tableData"
      :columns="columns"
      rowKey="goodsId"
      :pagination="false"
      :row-selection="{ selectedRowKeys: selectedRowKeyList, onChange: onSelectChange }"
    >
    </a-table>

    <div class="smart-query-table-page">
      <a-pagination
        showSizeChanger
        showQuickJumper
        show-less-items
        :pageSizeOptions="PAGE_SIZE_OPTIONS"
        :defaultPageSize="queryForm.pageSize"
        v-model:current="queryForm.pageNum"
        v-model:pageSize="queryForm.pageSize"
        :total="total"
        @change="ajaxQuery"
        @showSizeChange="ajaxQuery"
        :show-total="(total) => `共${total}条`"
      />
    </div>
  </a-card>
</template>
<script lang="ts" setup>
import { reactive, ref, onMounted } from "vue";
import { message, Modal } from "ant-design-vue";
import { useSpinStore } from "/@/store/modules/system/spin";
import { heartBeatApi } from "/@/api/support/heart-beat/heart-beat-api";
import type { ResponseModel } from "/@/api/base-model/response-model";
import { HeartBeatVo } from "/@/api/support/heart-beat/model/heart-beat-vo";
import { HeartBeatQueryForm } from "/@/api/support/heart-beat/model/heart-beat-query-form";
import { PageResultModel } from "/@/api/base-model/page-result-model";
import { GoodsUpdateForm } from "/@/api/business/goods/model/goods-update-form";
import { PAGE_SIZE_OPTIONS } from "/@/constants/common";

const columns = reactive([
  {
    title: "项目路径",
    dataIndex: "projectPath",
  },
  {
    title: "服务器ip",
    dataIndex: "serverIp",
  },
  {
    title: "进程号",
    dataIndex: "processNo",
  },
  {
    title: "进程开启时间",
    dataIndex: "processStartTime",
  },
  {
    title: "心跳当前时间",
    dataIndex: "heartBeatTime",
  },
]);

const queryFormState: HeartBeatQueryForm = {
  pageNum: 1,
  pageSize: 10,
};
const queryForm = reactive<HeartBeatQueryForm>({ ...queryFormState });
const tableLoading = ref<Boolean>(false);
const selectedRowKeyList = ref<Number[]>([]);
const tableData = ref<HeartBeatVo[]>([]);
const total = ref<Number>(0);
const operateModal = ref();
const dataTracerRef = ref();

// 显示操作记录弹窗
function showDataTracer(goodsId: number) {
  dataTracerRef.value.showDrawer(goodsId);
}

function onSelectChange(selectedRowKeys: Number[]) {
  selectedRowKeyList.value = selectedRowKeys;
}

function resetQuery() {
  Object.assign(queryForm, queryFormState);
  ajaxQuery();
}
async function ajaxQuery (){
  try {
    tableLoading.value = true;
    let responseModel: ResponseModel<
      PageResultModel<HeartBeatVo>
    > = await heartBeatApi.queryList(queryForm);
    const list = responseModel.data.list;
    total.value = responseModel.data.total;
    tableData.value = list;
  } catch (e) {
    console.log(e);
  } finally {
    tableLoading.value = false;
  }
};

onMounted(ajaxQuery);
</script>
