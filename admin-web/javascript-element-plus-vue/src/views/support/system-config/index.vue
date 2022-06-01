<template>
  <el-form class="smart-query-form">
    <el-row class="smart-query-form-row">
      <el-form-item label="参数Key" class="smart-query-form-item">
        <el-input
            style="width: 300px"
            v-model="queryForm.configKey"
            placeholder="请输入key"
        />
      </el-form-item>

      <el-form-item class="smart-query-form-item smart-margin-left10">
        <el-button type="primary" @click="ajaxQuery" icon='Search'>
          查询
        </el-button>
        <el-button @click="resetQuery" icon='RefreshLeft'>
          重置
        </el-button>
      </el-form-item>
    </el-row>
  </el-form>

  <el-card shadow='hover'>
    <el-row class="smart-table-btn-block">
      <div class="smart-table-operate-block">
        <el-button @click="toEditOrAdd()" type="primary" size="small" icon='Plus'>
          新建
        </el-button>
      </div>
      <div class="smart-table-setting-block"></div>
    </el-row>

    <el-table
      size='small'
      :data='tableData'
      border
      :rowKey='rowKey'
      v-loading='tableLoading'
    >
      <el-table-column prop='systemConfigId' label='参数id' width='80'></el-table-column>
      <el-table-column prop='configKey' label='参数key'></el-table-column>
      <el-table-column prop='configName' label='参数名称'></el-table-column>
      <el-table-column prop='configValue' label='参数值'></el-table-column>
      <el-table-column prop='remark' label='备注'></el-table-column>
      <el-table-column fixed='right' label='操作'>
        <template #default='scope'>
          <el-button @click="toEditOrAdd(scope.row)" link type='primary'>编辑</el-button>
        </template>
      </el-table-column>
    </el-table>

    <div class="smart-query-table-page">
      <el-pagination
        background
        v-model:currentPage='queryForm.pageNum'
        v-model:page-size='queryForm.pageSize'
        :page-sizes='PAGE_SIZE_OPTIONS'
        layout='total, sizes, prev, pager, next, jumper'
        :total='total'
        @size-change='ajaxQuery'
        @current-change='ajaxQuery'
      >
      </el-pagination>
    </div>
  </el-card>
  <SystemConfigOperateModal ref="operateModal" @reloadList="resetQuery" />
</template>
<script setup>
import { reactive, ref, onMounted } from "vue";
import { systemConfigApi } from "/@/api/support/system-config-api";
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

const queryFormState = {
  configKey: "",
  pageNum: 1,
  pageSize: 10,
};
const queryForm = reactive({ ...queryFormState });

const tableLoading = ref(false);
const rowKey = ref('systemConfigId');
const tableData = ref([]);
const total = ref(0);



function resetQuery() {
  Object.assign(queryForm, queryFormState);
  ajaxQuery();
}
async function ajaxQuery (){
  try {
    tableLoading.value = true;
    let responseModel= await systemConfigApi.queryList(queryForm);
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
function toEditOrAdd(rowData) {
  operateModal.value.showModal(rowData);
}

onMounted(ajaxQuery);
</script>
