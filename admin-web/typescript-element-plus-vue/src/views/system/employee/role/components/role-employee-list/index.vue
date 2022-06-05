<!--
 * @Author: zhuoda
 * @Date: 2021-08-30 10:52:22
 * @LastEditTime: 2022-06-06 00:43:20
 * @LastEditors: LiHaiFan
 * @Description:
 * @FilePath: \typescript-element-plus-vue\src\views\system\employee\role\components\role-employee-list\index.vue
-->
<template>
  <div>
    <div class="header">
      <p>管理拥有当前角色权限的人员列表</p>
      <div>
        <el-button v-privilege="'system:role:employee:add'" class="button-style" v-if="selectRoleId" type="primary" @click="addRoleEmployee"
          >添加成员</el-button
        >
        <el-button
          v-privilege="'system:role:employee:batch:delete'"
          class="button-style"
          v-if="selectRoleId"
          type="danger"
          danger
          @click="batchDelete"
          >批量移除</el-button
        >
      </div>
    </div>
    <el-table ref="table" @select="onSelect" @select-all="onSelect" size="small" :data="tableData" v-loading="tableLoading" :row-key="rowKey" border>
      <el-table-column type="selection" width="55"> </el-table-column>
      <el-table-column prop="actualName" label="姓名" width="150"> </el-table-column>
      <el-table-column prop="phone" label="手机号" width="150"> </el-table-column>
      <el-table-column prop="loginName" label="登录账号" width="100"> </el-table-column>
      <el-table-column prop="departmentName" label="部门" minWidth="100"></el-table-column>
      <el-table-column prop="disabledFlag" label="状态" width="60">
        <template #default="scope">
          <span>{{ scope.row.disabledFlag ? '禁用' : '启用' }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="operate" label="操作" width="80">
        <template #default="scope">
          <el-button type="text" size="small" @click="deleteEmployeeRole(scope.row.employeeId)" v-privilege="'role:deleteEmployee'">移除</el-button>
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
    <SelectEmployeeModal ref="selectEmployeeModal" @selectData="selectData" />
  </div>
</template>
<script setup lang="ts">
  import { computed, inject, reactive, Ref, ref, watch } from 'vue';
  import { roleApi } from '/@/api/system/role/role-api';
  import { PAGE_SIZE, PAGE_SIZE_OPTIONS } from '/@/constants/common';
  import { useSpinStore } from '/@/store/modules/system/spin';
  import SelectEmployeeModal from '/@/components/select-employee-modal/index.vue';
  import lodash from 'lodash';
  import { ElMessage, ElMessageBox } from 'element-plus';
  import { smartSentry } from '/@/lib/smart-sentry';
  import { EmployeeVo } from '/@/api/system/employee/model/employee-vo';
  import { RoleQueryDto } from '/@/api/system/role/model/role-query-dto';
  // ----------------------- 以下是字段定义 emits props ---------------------
  let selectEmployeeModal = ref();
  let selectRoleId = inject('selectRoleId') as Ref<number>;
  const tableData = ref<EmployeeVo[]>([]);
  const tableLoading = ref(false);
  const rowKey = ref('employeeId');
  const selectedRowKeys = ref<number[]>([]);
  const selectionRows = ref<EmployeeVo[]>([]);
  const defaultQueryForm: RoleQueryDto = {
    pageNum: 1,
    pageSize: PAGE_SIZE,
    roleId: undefined,
    keywords: undefined,
  };
  const queryForm = reactive<RoleQueryDto>({ ...defaultQueryForm });
  const total = ref(0);
  // ----------------------- 以下是计算属性 watch监听 ------------------------
  watch(
    () => selectRoleId.value,
    () => queryRoleEmployee()
  );
  const hasSelected = computed(() => selectedRowKeys.value.length > 0);
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
    ElMessageBox.confirm('确定要删除该角色成员么？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
    }).then(async () => {
      useSpinStore().show();
      try {
        await roleApi.deleteEmployeeRole(employeeId, selectRoleId.value);
        ElMessage.success('移除成功');
        await queryRoleEmployee();
      } catch (e) {
        console.error(e);
        smartSentry.captureException(e);
      } finally {
        useSpinStore().hide();
      }
    });
  }
  // 选择行
  async function onSelect(selection: EmployeeVo[]) {
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
  function batchDelete() {
    if (!hasSelected.value) {
      ElMessage.warning('请选择要删除的角色成员');
      return;
    }
    ElMessageBox.confirm('确定移除这些角色成员吗？', '提示', {
      confirmButtonText: '',
      cancelButtonText: '',
      type: 'warning',
    }).then(async () => {
      useSpinStore().show();
      try {
        let params = {
          employeeIdList: selectedRowKeys.value,
          roleId: selectRoleId.value,
        };
        await roleApi.deleteEmployeeList(params);
        ElMessage.success('移除成功');
        selectedRowKeys.value = [];
        selectionRows.value = [];
        await queryRoleEmployee();
      } catch (e) {
        console.error(e);
        smartSentry.captureException(e);
      } finally {
        useSpinStore().hide();
      }
    });
  }
  async function addRoleEmployee() {
    let res = await roleApi.getRoleAllEmployee(selectRoleId.value);
    let selectedIdList = res.data.map((e) => e.employeeId) || [];
    selectEmployeeModal.value.showModal(selectedIdList);
  }
  async function selectData(list:EmployeeVo[]) {
    if (lodash.isEmpty(list)) {
      ElMessage.warning('请选择角色人员');
      return;
    }
    useSpinStore().show();
    try {
      let params = {
        employeeIdList: list.map((e) => e.employeeId),
        roleId: selectRoleId.value,
      };
      await roleApi.addRoleEmployeeList(params);
      ElMessage.success('添加成功');
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
<style scoped lang="scss">
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
