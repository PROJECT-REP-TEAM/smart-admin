<!--
 * @Author: zhuoda
 * @Date: 2021-08-14 16:23:30
 * @LastEditTime: 2022-06-06 00:08:16
 * @LastEditors: LiHaiFan
 * @Description:
 * @FilePath: \typescript-element-plus-vue\src\views\system\employee\department\components\employee-list\index.vue
-->
<template>
  <el-card class="employee-container">
    <div class="header">
      <div class="header-title">部门人员</div>
      <div class="query-operate" v-privilege="'department:query'">
        <el-radio-group v-model="params.disabledFlag" style="margin: 8px; flex-shrink: 0" @change="queryEmployeeByKeyword">
          <el-radio-button :label="undefined">全部</el-radio-button>
          <el-radio-button :label="true">禁用</el-radio-button>
          <el-radio-button :label="false">启用</el-radio-button>
        </el-radio-group>
        <el-input v-model="params.keyword" placeholder="姓名/手机号/登录账号"></el-input>
        <el-button type="primary" style="margin-left: 10px" @click="queryEmployeeByKeyword" icon="Search"> 查询 </el-button>
        <el-button @click="reset" icon="RefreshLeft"> 重置 </el-button>
      </div>
    </div>
    <div class="btn-group">
      <el-button type="primary" @click="showDrawer()" v-privilege="'employee:add'">添加成员</el-button>
      <el-button @click="updateEmployeeDepartment" v-privilege="'employee:updateDepartment'">调整部门</el-button>
    </div>

    <el-table height="500" size="small" :data="tableData" :row-key="rowKey" border @select="onSelect" @select-all="onSelect">
      <el-table-column type="selection" width="55"> </el-table-column>
      <el-table-column prop="actualName" label="姓名" width="110"> </el-table-column>
      <el-table-column prop="phone" label="手机号" width="110"> </el-table-column>
      <el-table-column prop="gender" label="性别" width="50">
        <template #default="scope">
          <span>{{ $smartEnumPlugin.getDescByValue('GENDER_ENUM', scope.row.gender) }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="loginName" label="登录账号" width="100"> </el-table-column>
      <el-table-column prop="disabledFlag" label="状态" width="60">
        <template #default="scope">
          <span>{{ scope.row.disabledFlag ? '禁用' : '启用' }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="roleNameList" label="角色" min-width="100">
        <template #default="scope">
          {{ scope.row.roleNameList.join(',') }}
        </template>
      </el-table-column>
      <el-table-column prop="departmentName" label="部门" min-width="200"></el-table-column>
      <el-table-column prop="collegeDeptName" label="院系" min-width="150"></el-table-column>
      <el-table-column prop="operate" label="操作" width="170">
        <template #default="scope">
          <el-button type="text" @click="showDrawer(scope.row)" v-privilege="'employee:edit'">编辑</el-button>
          <el-button type="text" v-privilege="'employee:updateDisabled'" @click="updateDisabled(scope.row.employeeId, scope.row.disabledFlag)">{{
            scope.row.disabledFlag ? '启用' : '禁用'
          }}</el-button>
          <el-button type="text" @click="resetPassword(scope.row.employeeId)" v-privilege="'employee:resetPwd'">重置密码</el-button>
        </template>
      </el-table-column>
    </el-table>
    <div class="smart-query-table-page">
      <el-pagination
        background
        v-model:currentPage="params.pageNum"
        v-model:page-size="params.pageSize"
        :page-sizes="PAGE_SIZE_OPTIONS"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
        @size-change="queryEmployee"
        @current-change="queryEmployee"
      >
      </el-pagination>
    </div>
    <OperateEmployeeModal ref="operateEmployeeModal" @reloadList="queryEmployee" />
    <UpdateEmployeeDepartmentModal ref="updateEmployeeDepartmentModal" @reloadList="queryEmployee" />
  </el-card>
</template>
<script setup lang="ts">
  import { computed, reactive, ref, watch } from 'vue';
  import { employeeApi } from '/@/api/system/employee/employee-api';
  import { useSpinStore } from '/@/store/modules/system/spin';
  import _ from 'lodash';
  import { PAGE_SIZE, PAGE_SIZE_OPTIONS } from '/@/constants/common';
  import OperateEmployeeModal from '../operate-employee-modal/index.vue';
  import UpdateEmployeeDepartmentModal from '../update-employee-department-modal/index.vue';
  import { smartSentry } from '/@/lib/smart-sentry';
  import { ElMessage, ElMessageBox } from 'element-plus';
  import lodash from 'lodash';
  import { EmployeeQueryDto } from '/@/api/system/employee/model/employee-query-dto';
  import { EmployeeVo } from '/@/api/system/employee/model/employee-vo';
  import { EmployeeUpdateDto } from '/@/api/system/employee/model/employee-update-dto';

  // ----------------------- 以下是字段定义 emits props ---------------------

  //props
  const props = defineProps<{
    departmentId?: number;
  }>();

  // ----------------------- 表格/列表/ 搜索 ---------------------
  const tableData = ref<EmployeeVo[]>();
  let defaultParams: EmployeeQueryDto = {
    departmentId: undefined,
    disabledFlag: undefined,
    keyword: undefined,
    searchCount: undefined,
    pageNum: 1,
    pageSize: PAGE_SIZE,
    sortItemList: undefined,
  };
  const params = reactive({ ...defaultParams });
  const total = ref();

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
      selectionRows.value = [];
    } catch (error) {
      console.error(error);
      smartSentry.captureException(error);
    } finally {
      useSpinStore().hide();
    }
  }

  // 根据关键字 查询
  async function queryEmployeeByKeyword() {
    useSpinStore().show();
    try {
      params.pageNum = 1;
      params.departmentId = undefined;
      let res = await employeeApi.queryEmployee(params);
      tableData.value = res.data.list;
      total.value = res.data.total;
      selectedRowKeys.value = [];
      selectionRows.value = [];
    } catch (error) {
      console.error(error);
      smartSentry.captureException(error);
    } finally {
      useSpinStore().hide();
    }
  }

  // ----------------------- 多选操作 ---------------------
  const rowKey = ref('employeeId');
  const selectedRowKeys = ref<number[]>([]);
  const selectionRows = ref<EmployeeVo[]>([]);
  // 是否有选中：用于 批量操作按钮的禁用
  const hasSelected = computed(() => selectedRowKeys.value.length > 0);
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

  // 批量更新员工部门
  const updateEmployeeDepartmentModal = ref();

  function updateEmployeeDepartment() {
    if (!hasSelected.value) {
      ElMessage.warning('请选择要调整部门的员工');
      return;
    }
    updateEmployeeDepartmentModal.value.showModal(selectedRowKeys.value);
  }

  // ----------------------- 添加、修改、禁用、重置密码 ------------------------------------

  const operateEmployeeModal = ref(); //组件

  // 展示编辑弹窗
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

  // 重置密码
  function resetPassword(employeeId: number) {
    ElMessageBox.confirm('确定要重置密码为123456吗', '提醒', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
    }).then(async () => {
      useSpinStore().show();
      try {
        await employeeApi.resetPassword(employeeId);
        ElMessage.success('重置成功');
        await queryEmployee();
      } catch (error) {
        console.log(error);
        smartSentry.captureException(error);
      } finally {
        useSpinStore().hide();
      }
    });
  }

  // 禁用 / 启用
  function updateDisabled(employeeId: number, disabledFlag: boolean) {
    ElMessageBox.confirm(`确定要${disabledFlag ? '启用' : '禁用'}吗?`, '提醒', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
    }).then(async () => {
      useSpinStore().show();
      try {
        await employeeApi.updateDisabled(employeeId);
        ElMessage.success(`${disabledFlag ? '启用' : '禁用'}成功`);
        queryEmployee();
      } catch (error) {
        console.log(error);
        smartSentry.captureException(error);
      } finally {
        useSpinStore().hide();
      }
    });
  }
</script>
<style scoped lang="scss">
  .employee-container {
    overflow: scroll;
  }
  .header {
    display: flex;
    align-items: center;

    .header-title {
      margin-top: 10px;
      margin-bottom: 0.5em;
      color: #000000d9;
      font-weight: 600;
      font-size: 16px;
      line-height: 1.5;
    }
  }
  .query-operate {
    margin-left: auto;
    display: flex;
    align-items: center;
  }
  .btn-group {
    margin: 10px 0;
  }
</style>
