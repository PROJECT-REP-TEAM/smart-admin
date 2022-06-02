<!--
 * @Author: zhuoda
 * @Date: 2021-08-26 10:36:54
 * @LastEditTime: 2022-06-02
 * @LastEditors: zhuoda
 * @Description:
 * @FilePath: /smart-admin/src/views/system/employee/role/components/role-setting/index.vue
-->
<template>
  <a-card title="功能列表" class="role-container">
    <a-tabs v-model:activeKey="activeKey">
      <a-tab-pane key="1" tab="功能权限">
        <RoleTree />
      </a-tab-pane>
      <a-tab-pane key="2" tab="数据范围">
        <RoleDataScope />
      </a-tab-pane>
      <a-tab-pane key="3" tab="成员管理">
        <RoleEmployeeList />
      </a-tab-pane>
    </a-tabs>
  </a-card>
</template>
<script setup>
import { ref } from "vue";
import RoleTree from "../role-tree/index.vue";
import RoleDataScope from "../role-data-scope/index.vue";
import RoleEmployeeList from "../role-employee-list/index.vue";
// ----------------------- 以下是字段定义 emits props ---------------------
defineProps({
  value: Object,
});
defineEmits("update:value");
let activeKey = ref();
// ----------------------- 以下是计算属性 watch监听 ------------------------

// ----------------------- 以下是生命周期 ---------------------------------

// ----------------------- 以下是方法 ------------------------------------

// ----------------------- 以下是暴露的方法内容 ----------------------------
defineExpose({});
</script>
<style scoped lang="less">
.role-container {
  height: 100%;
}
</style>
