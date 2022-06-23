<!--
 * @Author: LiHaiFan
 * @Date: 2021-08-14 20:23:30
 * @LastEditTime: 2022-06-23
 * @LastEditors: zhuoda
 * @Description:
-->
<template>
  <a-card class="employee-container">
    <div class="header">
      <a-typography-title :level="5">部门人员</a-typography-title>
      <div class="query-operate" v-privilege="'department:query'">
        <a-radio-group v-model:value="params.disabledFlag" style="margin: 8px; flex-shrink: 0" @change="queryEmployeeByKeyword(false)">
          <a-radio-button :value="undefined">全部</a-radio-button>
          <a-radio-button :value="false">启用</a-radio-button>
          <a-radio-button :value="true">禁用</a-radio-button>
        </a-radio-group>
        <a-input-search v-model:value.trim="params.keyword" placeholder="姓名/手机号/登录账号" @search="queryEmployeeByKeyword(true)">
          <template #enterButton>
            <a-button style="margin-left: 8px" type="primary">
              <template #icon>
                <SearchOutlined />
              </template>
              查询
            </a-button>
          </template>
        </a-input-search>
        <a-button @click="reset">
          <template #icon>
            <ReloadOutlined />
          </template>
          重置
        </a-button>
      </div>
    </div>
    <div class="btn-group">
      <a-button class="btn" type="primary" @click="showDrawer()" v-privilege="'employee:add'" size="small">添加成员</a-button>
      <a-button class="btn" size="small" @click="updateEmployeeDepartment" v-privilege="'employee:updateDepartment'">调整部门</a-button>
      <a-button class="btn" size="small" @click="batchDelete" v-privilege="'employee:delete'">批量删除</a-button>
    </div>

    <a-table
      :row-selection="{ selectedRowKeys: selectedRowKeys, onChange: onSelectChange }"
      size="small"
      :columns="columns"
      :data-source="tableData"
      :pagination="false"
      :scroll="{ y: 250, x: 1200 }"
      row-key="employeeId"
      bordered
    >
      <template #bodyCell="{ text, record, index, column }">
        <template v-if="column.dataIndex === 'disabledFlag'">
          <a-tag :color="text ? 'error' : 'processing'">{{ text ? '禁用' : '启用' }}</a-tag>
        </template>
        <template v-else-if="column.dataIndex === 'gender'">
          <span>{{ $smartEnumPlugin.getDescByValue('GENDER_ENUM', text) }}</span>
        </template>
        <template v-else-if="column.dataIndex === 'operate'">
          <div class="smart-table-operate">
            <a-button v-privilege="'system:employee:update'" type="link" size="small" @click="showDrawer(record)">编辑</a-button>
            <a-button
              v-privilege="'system:employee:password:reset'"
              type="link"
              size="small"
              @click="resetPassword(record.employeeId, record.loginName)"
              >重置密码</a-button
            >
            <a-button v-privilege="'system:employee:disabled'" type="link" @click="updateDisabled(record.employeeId, record.disabledFlag)">{{
              record.disabledFlag ? '启用' : '禁用'
            }}</a-button>
          </div>
        </template>
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
        :show-total="showTableTotal"
      />
    </div>
    <EmployeeFormModal ref="employeeFormModal" @refresh="queryEmployee" @show-account="showAccount" />
    <EmployeeDepartmentFormModal ref="employeeDepartmentFormModal" @refresh="queryEmployee" />
    <EmployeePasswordDialog ref="employeePasswordDialog" />
  </a-card>
</template>
<script setup lang="ts">
  import { ExclamationCircleOutlined } from '@ant-design/icons-vue';
  import { message, Modal } from 'ant-design-vue';
  import _ from 'lodash';
  import { computed, createVNode, reactive, ref, watch } from 'vue';
  import { employeeApi } from '/@/api/system/employee/employee-api';
  import { PAGE_SIZE } from '/@/constants/common-const';
  import { useSpinStore } from '/@/store/modules/system/spin';
  import EmployeeFormModal from '../employee-form-modal/index.vue';
  import EmployeeDepartmentFormModal from '../employee-department-form-modal/index.vue';
  import EmployeePasswordDialog from '../employee-password-dialog/index.vue';
  import { PAGE_SIZE_OPTIONS, showTableTotal } from '/@/constants/common-const';

  // ----------------------- 以下是字段定义 emits props ---------------------

  const props = defineProps({
    departmentId: Number,
    breadcrumb: Array,
  });

  //-------------回显账号密码信息----------
  let employeePasswordDialog = ref();
  function showAccount(accountName: string, passWord: string) {
    employeePasswordDialog.value.showModal(accountName, passWord);
  }

  // ----------------------- 表格/列表/ 搜索 ---------------------
  //字段
  const columns = [
    {
      title: '姓名',
      dataIndex: 'actualName',
      width: 85,
    },
    {
      title: '手机号',
      dataIndex: 'phone',
      width: 100,
    },
    {
      title: '性别',
      dataIndex: 'gender',
      width: 60,
    },
    {
      title: '登录账号',
      dataIndex: 'loginName',
      width: 100,
    },
    {
      title: '状态',
      dataIndex: 'disabledFlag',
      width: 60,
    },
    {
      title: '角色',
      dataIndex: 'roleNameList',
      width: 100,
    },
    {
      title: '部门',
      dataIndex: 'departmentName',
      ellipsis: true,
      width: 200,
    },
    {
      title: '操作',
      dataIndex: 'operate',
      width: 120,
    },
  ];
  const tableData = ref();

  let defaultParams = {
    departmentId: undefined,
    disabledFlag: false,
    keyword: undefined,
    searchCount: undefined,
    pageNum: 1,
    pageSize: PAGE_SIZE,
    sortItemList: undefined,
  };
  const params = reactive({ ...defaultParams });
  const total = ref<number>();

  watch(
    () => props.departmentId,
    () => {
      params.pageNum = 1;
      queryEmployee();
    },
    { immediate: true }
  );

  // 搜索重置
  function reset() {
    Object.assign(params, defaultParams);
    queryEmployee();
  }

  // 查询
  async function queryEmployee() {
    useSpinStore().show();
    try {
      params.departmentId = props.departmentId;
      let res = await employeeApi.queryEmployee(params);
      tableData.value = res.data.list;
      total.value = res.data.total;
      // 清除选中
      selectedRowKeys.value = [];
      selectedRows.value = [];
    } catch (error) {
      console.error(error);
    } finally {
      useSpinStore().hide();
    }
  }

  // 根据关键字 查询
  async function queryEmployeeByKeyword(allDepartment?: boolean) {
    useSpinStore().show();
    try {
      params.pageNum = 1;
      params.departmentId = allDepartment ? undefined : props.departmentId;
      let res = await employeeApi.queryEmployee(params);
      tableData.value = res.data.list;
      total.value = res.data.total;
      // 清除选中
      selectedRowKeys.value = [];
      selectedRows.value = [];
    } catch (error) {
      console.error(error);
    } finally {
      useSpinStore().hide();
    }
  }

  // ----------------------- 多选操作 ---------------------

  let selectedRowKeys = ref([]);
  let selectedRows = ref([]);
  // 是否有选中：用于 批量操作按钮的禁用
  const hasSelected = computed(() => selectedRowKeys.value.length > 0);

  function onSelectChange(keyArray, selectRows) {
    selectedRowKeys.value = keyArray;
    selectedRows.value = selectRows;
  }

  // 批量删除员工
  function batchDelete() {
    if (!hasSelected.value) {
      message.warning('请选择要删除的员工');
      return;
    }
    const actualNameArray = selectedRows.value.map((e) => e.actualName);
    const employeeIdArray = selectedRows.value.map((e) => e.employeeId);
    Modal.confirm({
      title: '确定要删除如下员工吗?',
      icon: createVNode(ExclamationCircleOutlined),
      content: _.join(actualNameArray, ','),
      okText: '删除',
      okType: 'danger',
      async onOk() {
        useSpinStore().show();
        try {
          await employeeApi.batchDeleteEmployee(employeeIdArray);
          message.success('删除成功');
          queryEmployee();
          selectedRowKeys.value = [];
          selectedRows.value = [];
        } catch (error) {
          console.log(error);
        } finally {
          useSpinStore().hide();
        }
      },
      cancelText: '取消',
      onCancel() {},
    });
  }

  // 批量更新员工部门
  const employeeDepartmentFormModal = ref();

  function updateEmployeeDepartment() {
    if (!hasSelected.value) {
      message.warning('请选择要调整部门的员工');
      return;
    }
    const employeeIdArray = selectedRows.value.map((e) => e.employeeId);
    employeeDepartmentFormModal.value.showModal(employeeIdArray);
  }

  // ----------------------- 添加、修改、禁用、重置密码 ------------------------------------

  const employeeFormModal = ref(); //组件

  // 展示编辑弹窗
  function showDrawer(rowData) {
    let params = {};
    if (rowData) {
      params = _.cloneDeep(rowData);
      params.disabledFlag = params.disabledFlag ? 1 : 0;
    } else if (props.departmentId) {
      params.departmentId = props.departmentId;
    }
    employeeFormModal.value.showDrawer(params);
  }

  // 重置密码
  function resetPassword(id, name) {
    Modal.confirm({
      title: '提醒',
      icon: createVNode(ExclamationCircleOutlined),
      content: '确定要重置密码吗?',
      okText: '确定',
      okType: 'danger',
      async onOk() {
        useSpinStore().show();
        try {
          let { data: passWord } = await employeeApi.resetPassword(id);
          message.success('重置成功');
          employeePasswordDialog.value.showModal(name, passWord);
          queryEmployee();
        } catch (error) {
          console.log(error);
        } finally {
          useSpinStore().hide();
        }
      },
      cancelText: '取消',
      onCancel() {},
    });
  }

  // 禁用 / 启用
  function updateDisabled(id: number, disabledFlag: boolean) {
    Modal.confirm({
      title: '提醒',
      icon: createVNode(ExclamationCircleOutlined),
      content: `确定要${disabledFlag ? '启用' : '禁用'}吗?`,
      okText: '确定',
      okType: 'danger',
      async onOk() {
        useSpinStore().show();
        try {
          await employeeApi.updateDisabled(id);
          message.success(`${disabledFlag ? '启用' : '禁用'}成功`);
          queryEmployee();
        } catch (error) {
          console.log(error);
        } finally {
          useSpinStore().hide();
        }
      },
      cancelText: '取消',
      onCancel() {},
    });
  }
</script>
<style scoped lang="less">
  .employee-container {
    height: 100%;
  }
  .header {
    display: flex;
    align-items: center;
  }
  .query-operate {
    margin-left: auto;
    display: flex;
    align-items: center;
  }

  .btn-group {
    margin: 10px 0;
    .btn {
      margin-right: 8px;
    }
  }
</style>
