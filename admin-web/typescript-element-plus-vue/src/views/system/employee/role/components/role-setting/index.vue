<!--
 * @Author: zhuoda
 * @Date: 2021-08-26 10:36:54
 * @LastEditTime: 2022-06-05 22:59:33
 * @LastEditors: LiHaiFan
 * @Description:
 * @FilePath: \typescript-element-plus-vue\src\views\system\employee\role\components\role-setting\index.vue
-->
<template>
  <el-card title='功能列表' class='role-container'>
    <el-tabs v-model='activeKey'>
      <el-tab-pane name='1' label='功能权限'>
        <RoleTree />
      </el-tab-pane>
      <el-tab-pane name='2' label='数据范围'>
        <RoleDataScope />
      </el-tab-pane>
      <el-tab-pane name='3' label='成员管理'>
        <RoleEmployeeList />
      </el-tab-pane>
    </el-tabs>
  </el-card>
</template>
<script setup lang="ts">
  import { ref } from 'vue';
  import RoleTree from '../role-tree/index.vue';
  import RoleDataScope from '../role-data-scope/index.vue';
  import RoleEmployeeList from '../role-employee-list/index.vue';
  // ----------------------- 以下是字段定义 emits props ---------------------
  let activeKey = ref("1");
  // ----------------------- 以下是计算属性 watch监听 ------------------------

  // ----------------------- 以下是生命周期 ---------------------------------

  // ----------------------- 以下是方法 ------------------------------------

  // ----------------------- 以下是暴露的方法内容 ----------------------------
  defineExpose({});
</script>
<style scoped lang='scss'>
  .role-container {
    height: 100%;
  }
</style>
