<!--
 * @Author: zhuoda
 * @Date: 2021-08-17 11:03:34
 * @LastEditTime: 2021-08-19 16:57:53
 * @LastEditors: zhuoda
 * @Description: 
 * @FilePath: /smart-admin/src/views/system/employee/department/components/update-employee-department-modal/index.vue
-->
<template>
  <a-modal v-model:visible="visible" title="调整部门" :footer="null">
    <DepartmentTree ref="departmentTree" :init="false" :height="400"/>
    <div class="footer">
      <a-button style="margin-right: 8px" @click="closeModal">取消</a-button>
      <a-button type="primary" @click="handleOk">提交</a-button>
    </div>
  </a-modal>
</template>
<script setup lang="ts">
  import { message } from 'ant-design-vue';
  import _ from 'lodash';
  import { nextTick, ref } from 'vue';
  import DepartmentTree from '../department-tree/index.vue';
  import { employeeApi } from '/@/api/system/employee/employee-api';
  import { EmployeeDepartmentUpdateDto } from '/@/api/system/employee/model/employee-department-update-dto';
  import { useSpinStore } from '/@/store/modules/system/spin';
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
    nextTick(() => {
      // 打开窗口时候初始化数据
      departmentTree.value.selectedKeys = [];
      departmentTree.value.queryDepartmentTree();
    });
  }
  function closeModal() {
    departmentTree.value.selectedKeys = [];
    visible.value = false;
  }
  async function handleOk() {
    useSpinStore().show();
    try {
      if (_.isEmpty(employeeIdList.value)) {
        message.warning('请选择要调整的员工');
        return;
      }
      if (_.isEmpty(departmentTree.value.selectedKeys)) {
        message.warning('请选择要调整的部门');
        return;
      }
      let departmentId = departmentTree.value.selectedKeys[0];
      let params: EmployeeDepartmentUpdateDto = {
        employeeIdList: employeeIdList.value,
        departmentId: departmentId,
      };
      await employeeApi.batchUpdateDepartmentEmployee(params);
      message.success('操作成功');
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
<style scoped lang="less">
  .footer {
    position: absolute;
    right: 0;
    bottom: 0;
    width: 100%;
    border-top: 1px solid #e9e9e9;
    padding: 10px 16px;
    background: #fff;
    text-align: right;
    z-index: 1;
  }
</style>
