<!--
 * @Author: zhuoda
 * @Date: 2021-08-12 16:09:09
 * @LastEditTime: 2021-08-17 11:01:09
 * @LastEditors: zhuoda
 * @Description: 部门
 * @FilePath: /smart-admin/src/views/system/employee/department/index.vue
-->
<template>
  <div class='height100'>
    <el-row :gutter='16' class='height100'>
      <el-col :span='6'>
        <DepartmentTree ref='departmentTree' />
      </el-col>
      <el-col :span='18' class='height100'>
        <div class='employee-box height100'>
          <ChildDepartmentList style='flex-grow: 1' :breadcrumb='breadcrumb' :selectDeptChild='selectDeptChild' />
          <EmployeeList class='employee' :departmentId='selectDeptId' />
        </div>
      </el-col>
    </el-row>
  </div>
</template>
<script setup>
  import { computed, ref } from 'vue';
  import DepartmentTree from './components/department-tree/index.vue';
  import ChildDepartmentList from './components/child-department-list/index.vue';
  import EmployeeList from './components/employee-list/index.vue';
  import _ from 'lodash';
  // ----------------------- 以下是字段定义 emits props ---------------------
  // 子组件
  const departmentTree = ref();
  // ----------------------- 以下是计算属性 watch监听 ------------------------
  const breadcrumb = computed(() => {
    if (departmentTree.value) {
      return departmentTree.value.breadcrumb;
    }
    return [];
  });
  const selectDeptChild = computed(() => {
    if (departmentTree.value) {
      return departmentTree.value.selectDeptChild;
    }
    return [];
  });
  const selectDeptId = computed(() => {
    if (departmentTree.value) {
      let selectedKeys = departmentTree.value.selectedKeys;
      return _.isEmpty(selectedKeys) ? null : selectedKeys[0];
    }
    return null;
  });
  // ----------------------- 以下是生命周期 ---------------------------------

  // ----------------------- 以下是方法 ------------------------------------

  // ----------------------- 以下是暴露的方法内容 ----------------------------
  defineExpose({});
</script>
<style scoped lang='scss'>
  .height100 {
    height: 100%;
  }

  .employee-box {
    display: flex;
    flex-direction: column;

    .employee {
      flex-grow: 2;
      margin-top: 10px;
    }
  }
</style>
