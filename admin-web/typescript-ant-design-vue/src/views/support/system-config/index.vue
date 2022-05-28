<template>
  <a-form class="smart-query-form">
    <a-row class="smart-query-form-row">
      <a-form-item label="参数Key" class="smart-query-form-item">
        <a-input
            style="width: 300px"
            v-model:value="queryForm.configKey"
            placeholder="请输入key"
        />
      </a-form-item>

      <a-form-item class="smart-query-form-item smart-margin-left10">
        <a-button type="primary" @click="ajaxQuery">
          <template #icon>
            <ReloadOutlined />
          </template>
          查询
        </a-button>
        <a-button @click="resetQuery">
          <template #icon>
            <SearchOutlined />
          </template>
          重置
        </a-button>
      </a-form-item>
    </a-row>
  </a-form>

  <a-card size="small" :bordered="false" :hoverable="true">
    <a-row class="smart-table-btn-block">
      <div class="smart-table-operate-block">
        <a-button @click="toEditOrAdd()" type="primary" size="small">
          <template #icon>
            <PlusOutlined />
          </template>
          新建
        </a-button>
      </div>
      <div class="smart-table-setting-block"></div>
    </a-row>

    <a-table
      :scroll="{ x: 1300 }"
      size="small"
      :dataSource="tableData"
      :columns="columns"
      rowKey="systemConfigId"
      :pagination="false"
    >
      <template #bodyCell="{ text, record, index, column }">
        <template v-if="column.dataIndex === 'action'">
          <a-button @click="toEditOrAdd(record)" type="link">编辑</a-button>
        </template>
      </template>
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
  <SystemConfigOperateModal ref="operateModal" @reloadList="resetQuery" />
</template>
<script lang="ts" setup>
import { reactive, ref, onMounted } from "vue";
import { systemConfigApi } from "/@/api/support/system-config/system-config-api";
import type { ResponseModel } from "/@/api/base-model/response-model";
import { SystemConfigVo } from "/@/api/support/system-config/model/system-config-vo";
import { SystemConfigQueryForm } from "/@/api/support/system-config/model/system-config-query-form";
import { PageResultModel } from "/@/api/base-model/page-result-model";
import { PAGE_SIZE_OPTIONS } from "/@/constants/common";
import SystemConfigOperateModal from "./components/system-config-operate-modal.vue";

const columns = reactive([
  {
    title: "参数id",
    width: 80,
    dataIndex: "systemConfigId",
  },
  {
    title: "参数key",
    dataIndex: "configKey",
  },
  {
    title: "参数名称",
    dataIndex: "configName",
  },
  {
    title: "参数值",
    dataIndex: "configValue",
  },
  {
    title: "备注",
    dataIndex: "remark",
  },
  {
    title: "操作",
    dataIndex: "action",
    fixed: "right",
  },
]);

const queryFormState: SystemConfigQueryForm = {
  configKey: "",
  pageNum: 1,
  pageSize: 10,
};
const queryForm = reactive<SystemConfigQueryForm>({ ...queryFormState });

const tableLoading = ref<Boolean>(false);
const tableData = ref<SystemConfigVo[]>([]);
const total = ref<Number>(0);



function resetQuery() {
  Object.assign(queryForm, queryFormState);
  ajaxQuery();
}
async function ajaxQuery (){
  try {
    tableLoading.value = true;
    let responseModel: ResponseModel<
      PageResultModel<SystemConfigVo>
    > = await systemConfigApi.queryList(queryForm);
    const list = responseModel.data.list;
    total.value = responseModel.data.total;
    tableData.value = list;
  } catch (e) {
    console.log(e);
  } finally {
    tableLoading.value = false;
  }
};

const operateModal = ref();
function toEditOrAdd(rowData: SystemConfigVo) {
  operateModal.value.showModal(rowData);
}

onMounted(ajaxQuery);
</script>
