<!--
 * @Author: zhuoda
 * @Date: 2021-08-17 11:03:34
 * @LastEditTime: 2022-06-06 00:18:18
 * @LastEditors: LiHaiFan
 * @Description:
 * @FilePath: \typescript-element-plus-vue\src\views\system\employee\department\components\update-employee-department-modal\index.vue
-->
<template>
  <el-dialog v-model="visible" title="调整部门" destroy-on-close>
    <DepartmentTree ref="departmentTree" :height="400" :showMenu="false" />
    <template #footer>
      <el-button @click="closeModal">取消</el-button>
      <el-button type="primary" @click="handleOk">提交</el-button>
    </template>
  </el-dialog>
</template>
<script setup lang="ts">
  import _ from 'lodash';
  import { nextTick, ref } from 'vue';
  import DepartmentTree from '../department-tree/index.vue';
  import { employeeApi } from '/@/api/system/employee/employee-api';
  import { useSpinStore } from '/@/store/modules/system/spin';
  import { ElMessage } from 'element-plus';
  // ----------------------- 以下是字段定义 emits props ---------------------
  // 子组件
  const departmentTree = ref();
  // emit
  const emit = defineEmits<{
    (e: 'reloadList'): void;
  }>();
  const employeeIdList = ref<number[]>([]);
  const visible = ref(false);
  // ----------------------- 以下是计算属性 watch监听 ------------------------

  // ----------------------- 以下是生命周期 ---------------------------------

  // ----------------------- 以下是方法 ------------------------------------
  async function showModal(selectEmployeeId: number[]) {
    employeeIdList.value = selectEmployeeId;
    visible.value = true;
    await nextTick(() => {
      // 打开窗口时候初始化数据
      departmentTree.value.selectedKeys = [];
      departmentTree.value.queryDepartmentTree();
    });
  }
  function closeModal() {
    departmentTree.value.selectedKey = [];
    visible.value = false;
  }
  async function handleOk() {
    useSpinStore().show();
    try {
      if (_.isEmpty(employeeIdList.value)) {
        ElMessage.warning('请选择要调整的员工');
        return;
      }
      let departmentId = departmentTree.value.selectedKey;
      if (!departmentId) {
        ElMessage.warning('请选择要调整的部门');
        return;
      }
      let params = {
        employeeIdList: employeeIdList.value,
        departmentId: departmentId,
      };
      await employeeApi.batchUpdateDepartmentEmployee(params);
      ElMessage.success('操作成功');
      emit('reloadList');
      closeModal();
    } catch (error) {
      console.log(error);
    } finally {
      useSpinStore().hide();
    }
  }
  // ----------------------- 以下是暴露的方法内容 ----------------------------
  defineExpose({
    showModal,
  });
</script>
