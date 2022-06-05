<template>
  <el-form class="smart-query-form">
    <el-row class="smart-query-form-row">
      <el-form-item label="关键字" class="smart-query-form-item">
        <el-input style="width: 300px" v-model="queryForm.searchWord" placeholder="关键字" />
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
        <el-button @click="addOrUpdateKey" type="primary" size="small" icon="Plus">新建</el-button>
        <el-button @click="confirmBatchDelete" type="danger" size="small" :disabled="selectedRowKeys.length == 0" icon="Delete"> 批量删除 </el-button>
      </div>
      <div class="smart-table-setting-block"></div>
    </el-row>

    <el-table size="small" :data="tableData" border :rowKey="rowKey" v-loading="tableLoading" @select="onSelect" @select-all="onSelect">
      <el-table-column type="selection" width="55" />
      <el-table-column prop="dictKeyId" label="ID" width="90"></el-table-column>
      <el-table-column prop="keyCode" label="编码">
        <template #default="scope">
          <el-button link type="primary" @click.stop="showValueList(scope.row.dictKeyId)">{{ scope.row.keyCode }}</el-button>
        </template>
      </el-table-column>
      <el-table-column prop="keyName" label="名称"></el-table-column>
      <el-table-column prop="remark" label="备注"></el-table-column>
      <el-table-column fixed="right" label="操作">
        <template #default="scope">
          <el-button @click="addOrUpdateKey(scope.row)" link type="primary">编辑</el-button>
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

    <DictKeyOperateModal ref="operateModal" @reloadList="ajaxQuery" />
    <!-- 值列表 -->
    <DictValueModal ref="dictValueModal" />
  </el-card>
</template>
<script lang="ts" setup>
  import DictKeyOperateModal from './components/dict-key-operate-modal.vue';
  import DictValueModal from './components/dict-value-modal.vue';
  import { reactive, ref, onMounted } from 'vue';
  import { useSpinStore } from '/@/store/modules/system/spin';
  import { dictApi } from '/@/api/support/dict/dict-api';
  import { PAGE_SIZE_OPTIONS } from '/@/constants/common';
  import lodash from 'lodash';
  import { ElMessage, ElMessageBox } from 'element-plus';
  import { DictKeyQueryForm } from '/@/api/support/dict/model/dict-key-query-form';
  import { DictKeyVo } from '/@/api/support/dict/model/dict-key-vo';
  import { DictKeyUpdateForm } from '/@/api/support/dict/model/dict-key-update-form';

  const queryFormState: DictKeyQueryForm = {
    searchWord: '',
    pageNum: 1,
    pageSize: 10,
  };
  const queryForm = reactive<DictKeyQueryForm>({ ...queryFormState });
  const tableLoading = ref(false);
  const rowKey = ref('dictKeyId');
  const selectedRowKeys = ref<number[]>([]);
  const selectionRows = ref<DictKeyVo[]>([]);
  const tableData = ref<DictKeyVo[]>([]);
  const total = ref(0);
  const operateModal = ref();
  const dictValueModal = ref();

  // 选择行
  async function onSelect(selection: DictKeyVo[]) {
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
  function showValueList(dictKeyId: number) {
    dictValueModal.value.showModal(dictKeyId);
  }

  function resetQuery() {
    Object.assign(queryForm, queryFormState);
    ajaxQuery();
  }
  async function ajaxQuery() {
    try {
      tableLoading.value = true;
      let responseModel = await dictApi.keyQuery(queryForm);
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
    ElMessageBox.confirm('确定要删除选中Key吗?', '提示', {
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
      await dictApi.keyDelete(selectedRowKeys.value);
      ElMessage.success('删除成功');
      await ajaxQuery();
    } catch (e) {
      console.log(e);
    } finally {
      useSpinStore().hide();
    }
  };

  function addOrUpdateKey(rowData?: DictKeyUpdateForm) {
    operateModal.value.showModal(rowData);
  }

  onMounted(ajaxQuery);
</script>
