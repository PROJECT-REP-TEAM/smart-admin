<!--
 * @Author: zhuoda
 * @Date: 2021-08-30 14:38:05
 * @LastEditTime: 2021-08-30 15:14:06
 * @LastEditors: zhuoda
 * @Description: 
 * @FilePath: /smart-admin/src/components/select-employee-modal/index.vue
-->
<template>
  <a-modal v-model:visible="visible" :width="900" title="选择人员" @cancel="closeModal" @ok="selectData">
    <a-form class="smart-query-form">
      <a-row class="smart-query-form-row">
        <a-form-item label="关键字" class="smart-query-form-item">
          <a-input style="width: 150px" v-model:value="params.keyword" placeholder="商品名称" />
        </a-form-item>
        <a-form-item label="部门" class="smart-query-form-item">
          <DepartmentTreeSelect style="width: 150px" ref="departmentTreeSelect" v-model:value="params.departmentId" />
        </a-form-item>
        <a-form-item label="状态" class="smart-query-form-item">
          <a-select style="width: 150px" v-model:value="params.disabledFlag" placeholder="请选择状态" allowClear>
            <a-select-option :key="1"> 禁用 </a-select-option>
            <a-select-option :key="0"> 启用 </a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item class="smart-query-form-item smart-margin-left10">
          <a-button type="primary" @click="queryEmployee">
            <template #icon>
              <ReloadOutlined />
            </template>
            查询
          </a-button>
          <a-button @click="reset" class="smart-margin-left10">
            <template #icon>
              <SearchOutlined />
            </template>
            重置
          </a-button>
        </a-form-item>
      </a-row>
    </a-form>
    <a-table
      :row-selection="{ selectedRowKeys: selectedRowKeyList, onChange: onSelectChange }"
      :loading="tableLoading"
      size="small"
      :columns="columns"
      :data-source="tableData"
      :pagination="false"
      rowKey="id"
      :scroll="{ y: 300 }"
    >
      <template #disabledFlag="{ text }">
        <span>{{ text ? '禁用' : '启用' }}</span>
      </template>
      <template #gender="{ text }">
        <span>{{ $smartEnumPlugin.getDescByValue('GENDER_ENUM', text) }}</span>
      </template>
    </a-table>
    <div class="smart-query-table-page">
      <a-pagination
        showSizeChanger
        showQuickJumper
        show-less-items
        :pageSizeOptions="PAGE_SIZE_OPTIONS"
        :defaultPageSize="params.pageSize"
        v-model:current="params.pageNum"
        v-model:pageSize="params.pageSize"
        :total="total"
        @change="queryEmployee"
        @showSizeChange="queryEmployee"
        :show-total="(total) => `共${total}条`"
      />
    </div>
  </a-modal>
</template>
<script setup lang="ts">
  import { computed, reactive, ref } from 'vue';
  import { employeeApi } from '/@/api/system/employee/employee-api';
  import { EmployeeQueryDto } from '/@/api/system/employee/model/employee-query-dto';
  import { EmployeeVo } from '/@/api/system/employee/model/employee-vo';
  import { PAGE_SIZE, PAGE_SIZE_OPTIONS } from '/@/constants/common';
  import DepartmentTreeSelect from '/@/components/department-tree-select/index.vue';
  import { message } from 'ant-design-vue';
  // ----------------------- 以下是字段定义 emits props ---------------------
  const emits = defineEmits<{
    (e: 'selectData', value: EmployeeVo[]): void;
  }>();
  const visible = ref(false);
  const tableLoading = ref<boolean>(false);
  const departmentTreeSelect = ref();
  //字段
  const columns = [
    {
      title: '姓名',
      dataIndex: 'actualName',
    },
    {
      title: '手机号',
      dataIndex: 'phone',
    },
    {
      title: '性别',
      dataIndex: 'gender',
      slots: { customRender: 'gender' },
    },
    {
      title: '登录账号',
      dataIndex: 'loginName',
    },
    {
      title: '状态',
      dataIndex: 'disabledFlag',
      slots: { customRender: 'disabledFlag' },
    },
  ];
  const tableData = ref<EmployeeVo[]>([]);
  let defaultParams: EmployeeQueryDto = {
    departmentId: undefined,
    disabledFlag: undefined,
    employeeIdList: undefined,
    keyword: undefined,
    searchCount: undefined,
    pageNum: 1,
    pageSize: PAGE_SIZE,
    sortItemList: undefined,
  };
  const params = reactive<EmployeeQueryDto>({ ...defaultParams });
  const total = ref<number>();
  let selectedRowKeyList = ref<number[]>([]);
  // ----------------------- 以下是计算属性 watch监听 ------------------------
  const hasSelected = computed(() => selectedRowKeyList.value.length > 0);
  // ----------------------- 以下是生命周期 ---------------------------------

  // ----------------------- 以下是方法 ------------------------------------
  async function showModal(selectEmployeeId?: number[]) {
    selectedRowKeyList.value = selectEmployeeId || [];
    visible.value = true;
    queryEmployee();
  }
  function reset() {
    Object.assign(params, defaultParams);
    queryEmployee();
  }
  async function queryEmployee() {
    tableLoading.value = true;
    try {
      let res = await employeeApi.queryEmployee(params);
      tableData.value = res.data.list;
      total.value = res.data.total;
    } catch (error) {
      console.error(error);
    } finally {
      tableLoading.value = false;
    }
  }
  function onSelectChange(selectedRowKeys: number[]) {
    selectedRowKeyList.value = selectedRowKeys;
  }
  function closeModal() {
    Object.assign(params, defaultParams);
    selectedRowKeyList.value = [];
    visible.value = false;
  }
  function selectData() {
    if (!hasSelected.value) {
      message.warning('请选择角色人员');
      return;
    }
    let selectVoList = tableData.value.filter((e) => selectedRowKeyList.value.includes(e.id));
    emits('selectData', selectVoList);
    closeModal();
  }
  // ----------------------- 以下是暴露的方法内容 ----------------------------
  defineExpose({
    showModal,
  });
</script>
<style scoped lang="less"></style>
