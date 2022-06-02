<!--
 * @Author: zhuoda
 * @Date: 2021-08-12 16:11:06
 * @LastEditTime: 2022-06-02
 * @LastEditors: zhuoda
 * @Description: 角色
 * @FilePath: /smart-admin/src/views/system/employee/role/index.vue
-->
<template>
  <div class="height100">
    <a-row :gutter="10" type="flex" class="height100">
      <a-col flex="200px">
        <RoleList ref="roleList" />
      </a-col>
      <a-col flex="1">
        <RoleSetting />
      </a-col>
    </a-row>
  </div>
</template>
<script setup>
import _ from "lodash";
import { computed, provide, ref } from "vue";
import RoleList from "./components/role-list/index.vue";
import RoleSetting from "./components/role-setting/index.vue";
// ----------------------- 以下是字段定义 emits props ---------------------
defineProps({
  value: Object,
});
defineEmits("update:value");

let roleList = ref();
// ----------------------- 以下是计算属性 watch监听 ------------------------
const selectRoleId = computed(() => {
  if (!roleList.value) {
    return null;
  }
  return roleList.value.selectRoleId;
});
provide("selectRoleId", selectRoleId);
// ----------------------- 以下是生命周期 ---------------------------------

// ----------------------- 以下是方法 ------------------------------------

// ----------------------- 以下是暴露的方法内容 ----------------------------
defineExpose({});
</script>
<style scoped lang="less">
.height100 {
  height: 100%;
}
</style>
