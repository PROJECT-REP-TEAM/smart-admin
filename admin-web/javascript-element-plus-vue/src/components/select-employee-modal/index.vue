<!--
 * @Author: LiHaiFan
 * @Date: 2021-08-30 14:38:05
 * @LastEditTime: 2021-11-23 20:10:35
 * @LastEditors: LiHaiFan
 * @Description:
 * @FilePath: /employment-admin-web/src/components/select-employee-modal/index.vue
-->
<template>
  <el-dialog v-model='visible' :width='900' title='选择人员' destroy-on-close>
    <el-form class='smart-query-form'>
      <el-row class='smart-query-form-row'>
        <el-form-item label='关键字' class='smart-query-form-item'>
          <el-input style='width: 150px' v-model='params.keyword' placeholder='关键字' />
        </el-form-item>
        <el-form-item label='部门' class='smart-query-form-item'>
          <DepartmentTreeSelect width='150px' ref='departmentTreeSelect' v-model='params.departmentId' />
        </el-form-item>
        <el-form-item label='状态' class='smart-query-fo`rm-item'>
          <el-select style='width: 150px' v-model='params.disabledFlag' placeholder='请选择状态' clearable>
            <el-option :value='1' label='禁用'></el-option>
            <el-option :value='0' label='启用'></el-option>
          </el-select>
        </el-form-item>
        <el-form-item class='smart-query-form-item smart-margin-left10'>
          <el-button type='primary' @click='queryEmployee' icon='Search'> 查询</el-button>
          <el-button @click='reset' class='smart-margin-left10' icon='RefreshLeft'> 重置</el-button>
        </el-form-item>
      </el-row>
    </el-form>
    <el-table
      ref='table'
      @select='onSelect'
      @select-all='onSelect'
      size='small'
      :data='tableData'
      v-loading='tableLoading'
      :row-key='rowKey'
      border
    >
      <el-table-column type='selection' width='55'></el-table-column>
      <el-table-column prop='actualName' label='姓名' width='150'></el-table-column>
      <el-table-column prop='phone' label='手机号' minWidth='150'></el-table-column>
      <el-table-column prop='gender' label='性别' width='60'>
        <template #default='scope'>
          <span>{{ $smartEnumPlugin.getDescByValue('GENDER_ENUM', scope.row.gender) }}</span>
        </template>
      </el-table-column>
      <el-table-column prop='disabledFlag' label='状态' width='60'>
        <template #default='scope'>
          <span>{{ scope.row.disabledFlag ? '禁用' : '启用' }}</span>
        </template>
      </el-table-column>
      <el-table-column prop='loginName' label='登录账号' width='150'></el-table-column>
    </el-table>
    <div class='smart-query-table-page'>
      <el-pagination
        background
        v-model:currentPage='params.pageNum'
        v-model:page-size='params.pageSize'
        :page-sizes='PAGE_SIZE_OPTIONS'
        layout='total, sizes, prev, pager, next, jumper'
        :total='total'
        @size-change='queryEmployee'
        @current-change='queryEmployee'
      >
      </el-pagination>
    </div>
    <template #footer>
      <el-button style='margin-right: 8px' @click='closeModal'>取消</el-button>
      <el-button type='primary' @click='selectData'>确认</el-button>
    </template>
  </el-dialog>
</template>
<script setup>
  import { computed, nextTick, reactive, ref } from 'vue';
  import { employeeApi } from '/@/api/system/employee-api';
  import { PAGE_SIZE, PAGE_SIZE_OPTIONS } from '/@/constants/common';
  import DepartmentTreeSelect from '/@/components/department-tree-select/index.vue';
  import { smartSentry } from '/@/lib/smart-sentry';
  import { ElMessage } from 'element-plus';
  import lodash from 'lodash';
  // ----------------------- 以下是字段定义 emits props ---------------------
  const emits = defineEmits(['selectData']);
  const visible = ref(false);
  const tableLoading = ref(false);
  const departmentTreeSelect = ref();
  const tableData = ref([]);
  let defaultParams = {
    departmentId: undefined,
    disabledFlag: undefined,
    employeeIdList: undefined,
    keyword: undefined,
    searchCount: undefined,
    pageNum: 1,
    pageSize: PAGE_SIZE,
    sortItemList: undefined,
  };
  const params = reactive({ ...defaultParams });
  const total = ref();
  const rowKey = ref('employeeId');
  const selectedRowKeys = ref([]);
  const selectionRows = ref([]);
  let table = ref();
  // ----------------------- 以下是计算属性 watch监听 ------------------------
  const hasSelected = computed(() => selectedRowKeys.value.length > 0);
  // ----------------------- 以下是生命周期 ---------------------------------

  // ----------------------- 以下是方法 ------------------------------------
  async function showModal(selectEmployeeId) {
    selectedRowKeys.value = selectEmployeeId || [];
    visible.value = true;
    queryEmployee();
  }

  function reset() {
    Object.assign(params, defaultParams);
    departmentTreeSelect.value.handleClear();
    queryEmployee();
  }

  async function queryEmployee() {
    tableLoading.value = true;
    try {
      let res = await employeeApi.queryEmployee(params);
      tableData.value = res.data.list;
      total.value = res.data.total;
      await nextTick(() => {
        for (const employeeId of selectedRowKeys.value) {
          let data = tableData.value.find((e) => e.employeeId === employeeId);
          if (!data) {
            continue;
          }
          table.value.toggleRowSelection(data, true);
        }
      });
    } catch (error) {
      console.error(error);
      smartSentry.captureException(error);
    } finally {
      tableLoading.value = false;
    }
  }

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

  function closeModal() {
    Object.assign(params, defaultParams);
    selectedRowKeys.value = [];
    selectionRows.value = [];
    visible.value = false;
  }

  function selectData() {
    if (!hasSelected.value) {
      ElMessage.warning('请选择角色人员');
      return;
    }
    emits('selectData', selectedRowKeys.value);
    closeModal();
  }

  // ----------------------- 以下是暴露的方法内容 ----------------------------
  defineExpose({
    showModal,
  });
</script>
<style scoped lang='scss'></style>
