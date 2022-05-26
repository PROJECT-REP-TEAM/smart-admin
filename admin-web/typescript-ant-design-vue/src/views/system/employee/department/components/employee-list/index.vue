<!--
 * @Author: zhuoda
 * @Date: 2021-08-14 16:23:30
 * @LastEditTime: 2021-08-26 10:18:56
 * @LastEditors: zhuoda
 * @Description:
 * @FilePath: /smart-admin/src/views/system/employee/department/components/employee-list/index.vue
-->
<template>
  <a-card class="employee-container">
    <div class="header">
      <a-typography-title :level="4">部门人员</a-typography-title>
      <div class="query-operate">
        <a-radio-group v-model:value="params.disabledFlag" style="margin: 8px; flex-shrink: 0" @change="queryEmployee">
          <a-radio-button :value="undefined">全部</a-radio-button>
          <a-radio-button :value="true">禁用</a-radio-button>
          <a-radio-button :value="false">启用</a-radio-button>
        </a-radio-group>
        <a-input-search v-model:value="params.keyword" placeholder="请输入姓名/手机号" @search="queryEmployee">
          <template #enterButton>
            <a-button v-privilege="'system:employee:query'" style="margin-left: 8px"  type="primary">
              <template #icon>
                <ReloadOutlined />
              </template>
              查询
            </a-button>
          </template>
        </a-input-search>
            <a-button @click="reset">
              <template #icon>
              <SearchOutlined />
              </template>
              重置
            </a-button>
      </div>
    </div>
    <div class="btn-group">
      <a-button v-privilege="'system:employee:add'" class="btn" type="primary" @click="showDrawer()">添加成员</a-button>
      <a-button v-privilege="'system:employee:department:update'" class="btn" @click="updateEmployeeDepartment">调整部门</a-button>
      <a-button v-privilege="'system:employee:delete'" class="btn" @click="batchDelete">批量删除</a-button>
    </div>

    <a-table
      :row-selection="{ selectedRowKeys: selectedRowKeys, onChange: onSelectChange }"
      size="small"
      :columns="columns"
      rowKey="employeeId"
      :data-source="tableData"
      :pagination="false"
      :scroll="{ y: 250 }"
    >
      <template #bodyCell="{ text, record, index, column }">
        <template v-if="column.dataIndex === 'disabledFlag'" >
          <span>{{ text ? '禁用' : '启用' }}</span>
        </template>
        <template v-else-if="column.dataIndex === 'gender'">
          <span>{{ $smartEnumPlugin.getDescByValue('GENDER_ENUM', text) }}</span>
        </template>
        <template v-else-if="column.dataIndex === 'operate'">
          <a-button v-privilege="'system:employee:update'" type="link" size="small" @click="showDrawer(record)">编辑</a-button>
          <a-button v-privilege="'system:employee:password:reset'" type="link" size="small" @click="resetPassword(record.employeeId)">重置密码</a-button>
          <a-button v-privilege="'system:employee:disabled'" type="link" @click="updateDisabled(record.employeeId, record.disabledFlag)">{{ record.disabledFlag ? '启用' : '禁用' }}</a-button>
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
        :show-total="(total) => `共${total}条`"
      />
    </div>
    <OperateEmployeeModal ref="operateEmployeeModal" @reloadList="queryEmployee" />
    <UpdateEmployeeDepartmentModal ref="updateEmployeeDepartmentModal" @reloadList="queryEmployee" />
  </a-card>
</template>
<script setup lang="ts">
  import { Key } from 'ant-design-vue/lib/_util/type';
  import { computed, createVNode, reactive, ref, watch } from 'vue';
  import { employeeApi } from '/@/api/system/employee/employee-api';
  import { EmployeeQueryDto } from '/@/api/system/employee/model/employee-query-dto';
  import { EmployeeVo } from '/@/api/system/employee/model/employee-vo';
  import { useSpinStore } from '/@/store/modules/system/spin';
  import OperateEmployeeModal from '../operate-employee-modal/index.vue';
  import { EmployeeUpdateDto } from '/@/api/system/employee/model/employee-update-dto';
  import _ from 'lodash';
  import { ExclamationCircleOutlined } from '@ant-design/icons-vue';
  import { message, Modal } from 'ant-design-vue';
  import UpdateEmployeeDepartmentModal from '../update-employee-department-modal/index.vue';
  import { PAGE_SIZE, PAGE_SIZE_OPTIONS } from '/@/constants/common';

  // ----------------------- 以下是字段定义 emits props ---------------------
  //组件
  const operateEmployeeModal = ref();
  const updateEmployeeDepartmentModal = ref();
  //props
  const props = defineProps<{
    departmentId?: number;
  }>();
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
    },
    {
      title: '登录账号',
      dataIndex: 'loginName',
    },
    {
      title: '状态',
      dataIndex: 'disabledFlag',
    },
    {
      title: '操作',
      dataIndex: 'operate',
      width: 300,
    },
  ];
  const tableData = ref<EmployeeVo[]>();
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
  let selectedRowKeys = ref<Key[]>([]);
  let selectedRows = ref<EmployeeVo[]>([]);
  // ----------------------- 以下是计算属性 watch监听 ------------------------
  watch(
    () => props.departmentId,
    () => {
      params.pageNum = 1;
      queryEmployee();
    },
    { immediate: true }
  );
  const hasSelected = computed(() => selectedRowKeys.value.length > 0);
  // ----------------------- 以下是生命周期 ---------------------------------

  // ----------------------- 以下是方法 ------------------------------------
  function reset() {
    Object.assign(params, defaultParams);
    queryEmployee();
  }
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
  function showDrawer(rowData?: EmployeeVo) {
    let params: EmployeeUpdateDto = {};
    if (rowData) {
      params = <EmployeeUpdateDto>_.cloneDeep(rowData);
      params.disabledFlag = params.disabledFlag ? 1 : 0;
    } else if (props.departmentId) {
      params.departmentId = props.departmentId;
    }
    operateEmployeeModal.value.showDrawer(params);
  }
  function resetPassword(id: number) {
    Modal.confirm({
      title: '提醒',
      icon: createVNode(ExclamationCircleOutlined),
      content: '确定要重置密码为123456吗?',
      okText: '确定',
      okType: 'danger',
      async onOk() {
        useSpinStore().show();
        try {
          await employeeApi.resetPassword(id);
          message.success('重置成功');
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

  function onSelectChange(keyArray: Key[], selectRows: EmployeeVo[]): void {
    selectedRowKeys.value = keyArray;
    selectedRows.value = selectRows;
  }

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

  function updateEmployeeDepartment() {
    if (!hasSelected.value) {
      message.warning('请选择要调整部门的员工');
      return;
    }
    const employeeIdArray = selectedRows.value.map((e) => e.employeeId);
    updateEmployeeDepartmentModal.value.showModal(employeeIdArray);
  }
  // ----------------------- 以下是暴露的方法内容 ----------------------------
  defineExpose({});
</script>
<style scoped lang="less">
  .employee-container {
    height: 100%;
  }
  .header {
    display: flex;
    align-items: center;
    justify-content: space-between;
  }
  .query-operate {
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
