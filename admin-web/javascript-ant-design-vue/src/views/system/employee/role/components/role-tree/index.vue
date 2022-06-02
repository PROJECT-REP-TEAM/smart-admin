<!--
 * @Author: zhuoda
 * @Date: 2021-08-28 11:46:46
 * @LastEditTime: 2022-06-02
 * @LastEditors: zhuoda
 * @Description: 
 * @FilePath: /smart-admin/src/views/system/employee/role/components/role-tree/index.vue
-->
<template>
  <div>
    <div class="tree-header">
      <p>设置角色对应的功能操作、后台管理权限</p>
      <a-button v-if="selectRoleId" type="primary" @click="saveChange"> 保存 </a-button>
    </div>
    <!--CheckboxGroup 功能权限勾选部分 start-->
    <RoleTreeCheckbox :tree="tree" />
    <!--CheckboxGroup 功能权限勾选部分 end-->
  </div>
</template>
<script setup>
import { ref } from "@vue/reactivity";
import { inject, watch } from "@vue/runtime-core";
import _ from "lodash";
import { roleMenuApi } from "/@/api/system/role-menu/role-menu-api";
import { useRoleStore } from "/@/store/modules/system/role";
import RoleTreeCheckbox from "./role-tree-checkbox.vue";
import { Ref } from "vue";
import { message } from "ant-design-vue";
import { useSpinStore } from "/@/store/modules/system/spin";
import { RoleMenuDto } from "/@/api/system/role-menu/modal/role-menu-dto";
// ----------------------- 以下是字段定义 emits props ---------------------
let roleStore = useRoleStore();
let tree = ref();
let selectRoleId = inject("selectRoleId");
// ----------------------- 以下是计算属性 watch监听 ------------------------
watch(selectRoleId, () => getRoleSelectedMenu(), {
  immediate: true,
});
// ----------------------- 以下是生命周期 ---------------------------------

// ----------------------- 以下是方法 ------------------------------------
async function getRoleSelectedMenu() {
  if (!selectRoleId.value) {
    return;
  }
  let res = await roleMenuApi.getRoleSelectedMenu(selectRoleId.value);
  let data = res.data;
  if (_.isEmpty(roleStore.treeMap)) {
    roleStore.initTreeMap(data.menuTreeList || []);
  }
  roleStore.initCheckedData(data.selectedMenuId || []);
  tree.value = data.menuTreeList;
}
async function saveChange() {
  let checkedData = roleStore.checkedData;
  if (_.isEmpty(checkedData)) {
    message.error("还未选择任何权限");
    return;
  }
  let params = {
    roleId: selectRoleId.value,
    menuIdList: checkedData,
  };
  useSpinStore().show();
  try {
    await roleMenuApi.updateRoleMenu(params);
    message.success("保存成功");
  } catch (error) {
    console.log(error);
  } finally {
    useSpinStore().hide();
  }
}
// ----------------------- 以下是暴露的方法内容 ----------------------------
defineExpose({});
</script>
<style scoped lang="less">
@import "./index.less";
</style>
