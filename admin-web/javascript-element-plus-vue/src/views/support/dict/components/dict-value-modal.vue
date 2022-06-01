<template>
  <el-drawer
      :size="1300"
      v-model="visible"
      title="字典值"
      @close="onClose"
  >
    <el-form class="smart-query-form">
      <el-row class="smart-query-form-row">
        <el-form-item label="关键字" class="smart-query-form-item">
          <el-input
              style="width: 300px"
              v-model="queryForm.searchWord"
              placeholder="关键字"
          />
        </el-form-item>

        <el-form-item class="smart-query-form-item smart-margin-left10">
          <el-button type="primary" @click="ajaxQuery"  icon='Search'>
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
          <el-button @click="addOrUpdateValue" type="primary" size="small" icon='Plus'>
            新建
          </el-button>
          <el-button @click="confirmBatchDelete" type="danger" size="small" :disabled="selectedRowKeys.length == 0" icon='Delete'>
            批量删除
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
        @select='onSelect'
        @select-all='onSelect'
      >
        <el-table-column type='selection' width='55' />
        <el-table-column prop='dictValueId' label='ID' width='90'></el-table-column>
        <el-table-column prop='valueCode' label='编码'></el-table-column>
        <el-table-column prop='valueName' label='名称'></el-table-column>
        <el-table-column prop='sort' label='排序' width='80'></el-table-column>
        <el-table-column prop='remark' label='备注'></el-table-column>
        <el-table-column fixed='right' label='操作'>
          <template #default='scope'>
            <el-button @click="addOrUpdateValue(scope.row)" link type='primary'>编辑</el-button>
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
    <DictValueOperateModal ref="operateModal" @reloadList="ajaxQuery" />
  </el-drawer>
</template>
<script setup>
import {reactive, ref} from "vue";
import DictValueOperateModal from "./dict-value-operate-modal.vue";
import {PAGE_SIZE_OPTIONS } from "/@/constants/common";
import { dictApi } from "/@/api/support/dict-api";
import {useSpinStore} from "/@/store/modules/spin";
import lodash from 'lodash';
import { ElMessage,ElMessageBox } from 'element-plus';

// 是否展示抽屉
const visible = ref(false);
const dictKeyId = ref(undefined);
// ----------------------- 以下是生命周期 ------------------------

// ----------------------- 以下是方法 ------------------------
function showModal(keyId) {
  dictKeyId.value = keyId;
  visible.value = true;
  ajaxQuery();
}

function onClose() {
  visible.value = false;
  dictKeyId.value = undefined;
}

const queryFormState = {
  dictKeyId: undefined,
  searchWord: "",
  pageNum: 1,
  pageSize: 10,
};
const queryForm = reactive({ ...queryFormState });
const rowKey = ref('dictValueId');
const selectedRowKeys = ref([]);
const selectionRows = ref([]);
const tableLoading = ref(false);
const tableData = ref([]);
const total = ref(0);

// 选择行
async function onSelect(selection) {
  // 找到当页未被勾选项
  let unchecked = lodash.xorBy(tableData.value, selection, rowKey.value);
  // 合并当前与选中项
  selectedRowKeys.value = lodash.unionBy(
    selectedRowKeys.value,
    selection.map((e) => e[rowKey.value]),
  );
  selectionRows.value = lodash.unionBy(selectionRows.value, selection, rowKey.value);
  // 移除未被勾选项
  selectedRowKeys.value = lodash.differenceBy(
    selectedRowKeys.value,
    unchecked.map((e) => e[rowKey.value]),
  );
  selectionRows.value = lodash.differenceBy(selectionRows.value, unchecked, rowKey.value);
}

function resetQuery() {
  Object.assign(queryForm, queryFormState);
  ajaxQuery();
}
async function ajaxQuery() {
  try {
    tableLoading.value = true;
    queryForm.dictKeyId = dictKeyId.value;
    let responseModel = await dictApi.valueQuery(queryForm);
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
  ElMessageBox.confirm('确定要删除选中值吗?', '提示', {
    confirmButtonText: '删除',
    cancelButtonText: '取消',
    type: 'error',
  }).then(() => {
    batchDelete();
  }).catch(() => {
  });
}

const batchDelete = async () => {
  try {
    useSpinStore().show();
    await dictApi.valueDelete(selectedRowKeys.value);
    ElMessage.success("删除成功");
    await ajaxQuery();
  } catch (e) {
    console.log(e);
  } finally {
    useSpinStore().hide();
  }
};

const operateModal = ref();
function addOrUpdateValue(rowData) {
  operateModal.value.showModal(rowData,dictKeyId.value);
}

// ----------------------- 以下是暴露的方法内容 ------------------------
defineExpose({
  showModal,
});
</script>
