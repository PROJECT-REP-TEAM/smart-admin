<!--
 * @Author: zhuoda
 * @Date: 2021-08-30 10:52:22
 * @LastEditTime: 2021-08-30 15:20:03
 * @LastEditors: zhuoda
 * @Description: 
 * @FilePath: /smart-admin/src/views/system/employee/role/components/role-employee-list/index.vue
-->
<template>
  <div>
    <div class="header">
      <p>管理拥有当前角色权限的人员列表</p>
      <div>
        <a-button class="button-style" v-if="selectRoleId" type="primary" @click="addRoleEmployee">添加成员</a-button>
        <a-button class="button-style" v-if="selectRoleId" type="primary" danger @click="batchDelete">批量移除</a-button>
      </div>
    </div>
    <a-table
      :loading="tableLoading"
      :dataSource="tableData"
      :columns="columns"
      :pagination="false"
      :scroll="{ y: 400 }"
      rowKey="id"
      :row-selection="{ selectedRowKeys: selectedRowKeyList, onChange: onSelectChange }"
    >
      <template #disabledFlag="{ text }">
        <span>{{ text ? '禁用' : '启用' }}</span>
      </template>
      <template #gender="{ text }">
        <span>{{ $smartEnumPlugin.getDescByValue('GENDER_ENUM', text) }}</span>
      </template>
      <template #operate="{ record }">
        <a @click="deleteEmployeeRole(record.id)">移除</a>
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
        @change="queryRoleEmployee"
        @showSizeChange="queryRoleEmployee"
        :show-total="(total) => `共${total}条`"
      />
    </div>
    <SelectEmployeeModal ref="selectEmployeeModal" @selectData="selectData" />
  </div>
</template>
<script setup lang="ts">
  import { message, Modal } from 'ant-design-vue';
  import { computed, inject, reactive, ref, Ref, watch } from 'vue';
  import { EmployeeVo } from '/@/api/system/employee/model/employee-vo';
  import { RoleEmployeeBatchDto } from '/@/api/system/role/model/role-employee-batch-dto';
  import { RoleQueryDto } from '/@/api/system/role/model/role-query-dto';
  import { roleApi } from '/@/api/system/role/role-api';
  import { PAGE_SIZE, PAGE_SIZE_OPTIONS } from '/@/constants/common';
  import { useSpinStore } from '/@/store/modules/system/spin';
  import SelectEmployeeModal from '/@/components/select-employee-modal/index.vue';
  import _ from 'lodash';
  // ----------------------- 以下是字段定义 emits props ---------------------
  let selectEmployeeModal = ref();
  let selectRoleId = inject('selectRoleId') as Ref<number>;
  const columns = reactive([
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
    {
      title: '操作',
      slots: { customRender: 'operate' },
      width: 150,
    },
  ]);
  const tableData = ref<EmployeeVo[]>([]);
  const tableLoading = ref<boolean>(false);
  const selectedRowKeyList = ref<number[]>([]);
  const defaultQueryForm: RoleQueryDto = {
    pageNum: 1,
    pageSize: PAGE_SIZE,
    roleId: undefined,
    roleName: undefined,
  };
  const queryForm = reactive<RoleQueryDto>({ ...defaultQueryForm });
  const total = ref<number>(0);
  // ----------------------- 以下是计算属性 watch监听 ------------------------
  watch(
    () => selectRoleId.value,
    () => queryRoleEmployee()
  );
  const hasSelected = computed(() => selectedRowKeyList.value.length > 0);
  // ----------------------- 以下是生命周期 ---------------------------------
  queryRoleEmployee();
  // ----------------------- 以下是方法 ------------------------------------
  async function queryRoleEmployee() {
    try {
      tableLoading.value = true;
      queryForm.roleId = selectRoleId.value;
      let res = await roleApi.queryRoleEmployee(queryForm);
      tableData.value = res.data.list;
      total.value = res.data.total;
    } catch (e) {
      console.log(e);
    } finally {
      tableLoading.value = false;
    }
  }
  // 删除角色成员方法
  async function deleteEmployeeRole(employeeId: number) {
    Modal.confirm({
      title: '提示',
      content: '确定要删除该角色成员么？',
      okText: '确定',
      okType: 'danger',
      async onOk() {
        useSpinStore().show();
        try {
          await roleApi.deleteEmployeeRole(employeeId, selectRoleId.value);
          message.success('移除成功');
          await queryRoleEmployee();
        } catch (e) {
          console.error(e);
        } finally {
          useSpinStore().hide();
        }
      },
      cancelText: '取消',
      onCancel() {},
    });
  }
  function onSelectChange(selectedRowKeys: number[]) {
    selectedRowKeyList.value = selectedRowKeys;
  }
  function batchDelete() {
    if (!hasSelected.value) {
      message.warning('请选择要删除的角色成员');
      return;
    }
    Modal.confirm({
      title: '提示',
      content: '确定移除这些角色成员吗？',
      okText: '确定',
      okType: 'danger',
      async onOk() {
        useSpinStore().show();
        try {
          let params: RoleEmployeeBatchDto = {
            employeeIdList: selectedRowKeyList.value,
            roleId: selectRoleId.value,
          };
          await roleApi.deleteEmployeeList(params);
          message.success('移除成功');
          selectedRowKeyList.value = [];
          await queryRoleEmployee();
        } catch (e) {
          console.error(e);
        } finally {
          useSpinStore().hide();
        }
      },
      cancelText: '取消',
      onCancel() {},
    });
  }
  async function addRoleEmployee() {
    let res = await roleApi.getRoleAllEmployee(selectRoleId.value);
    let selectedIdList = res.data.map((e) => e.id) || [];
    selectEmployeeModal.value.showModal(selectedIdList);
  }
  async function selectData(list: EmployeeVo[]) {
    if (_.isEmpty(list)) {
      message.warning('请选择角色人员');
      return;
    }
    useSpinStore().show();
    try {
      let params: RoleEmployeeBatchDto = {
        employeeIdList: list.map((e) => e.id),
        roleId: selectRoleId.value,
      };
      await roleApi.addRoleEmployeeList(params);
      message.success('添加成功');
      await queryRoleEmployee();
    } catch (e) {
      console.error(e);
    } finally {
      useSpinStore().hide();
    }
  }
  // ----------------------- 以下是暴露的方法内容 ----------------------------
  defineExpose({});
</script>
<style scoped lang="less">
  .header {
    display: flex;
    align-items: center;
    justify-content: space-between;
    margin: 20px 0;
  }
  .button-style {
    margin: 0 10px;
  }
</style>
