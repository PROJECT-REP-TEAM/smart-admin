<!--
 * @Author: zhuoda
 * @Date: 2021-08-28 15:00:40
 * @LastEditTime: 2021-08-28 16:37:00
 * @LastEditors: zhuoda
 * @Description: 
 * @FilePath: /smart-admin/src/views/system/employee/role/components/role-tree/role-tree-menu.vue
-->
<template>
  <li v-for="module in props.tree" :key="module.menuId">
    <div class="menu" :style="{ marginLeft: `${props.index * 4}%` }">
      <a-checkbox
        @change="selectCheckbox(module)"
        class="checked-box-label"
        :value="module.menuId"
        >{{ module.menuName }}
      </a-checkbox>
      <div
        v-if="
          module.children &&
          module.children.some((e) => e.menuType == MENU_TYPE_ENUM.POINTS.value)
        "
      >
        <RoleTreePoint :tree="module.children" @selectCheckbox="selectCheckbox" />
      </div>
    </div>
    <template
      v-if="
        module.children &&
        !module.children.some((e) => e.menuType == MENU_TYPE_ENUM.POINTS.value)
      "
    >
      <RoleTreeMenu :tree="module.children" :index="props.index + 1" />
    </template>
  </li>
</template>
<script setup lang="ts">
import { MenuSimpleTreeVo } from "/@/api/system/menu/model/menu-simple-tree-vo";
import { MENU_TYPE_ENUM } from "/@/constants/system/menu";
import { useRoleStore } from "/@/store/modules/system/role";
import RoleTreePoint from "./role-tree-point.vue";
// ----------------------- 以下是字段定义 emits props ---------------------
let props = withDefaults(
  defineProps<{
    tree?: MenuSimpleTreeVo[];
    index?: number;
  }>(),
  {
    index: 0,
  }
);
defineEmits<{
  (e: "update:value"): void;
}>();
let roleStore = useRoleStore();
// ----------------------- 以下是计算属性 watch监听 ------------------------

// ----------------------- 以下是生命周期 ---------------------------------

// ----------------------- 以下是方法 ------------------------------------
function selectCheckbox(module: MenuSimpleTreeVo) {
  if (!module.menuId) {
    return;
  }
  // 是否勾选
  let checkedData = roleStore.checkedData;
  let findIndex = checkedData.indexOf(module.menuId);
  // 选中
  if (findIndex == -1) {
    // 选中本级以及子级
    roleStore.addCheckedDataAndChildren(module);
    // 选中上级
    roleStore.selectUpperLevel(module);
    // 是否有关联菜单 有则选中
    if (module.contextMenuId) {
      roleStore.addCheckedData(module.contextMenuId);
    }
  } else {
    // 取消选中本级以及子级
    roleStore.deleteCheckedDataAndChildren(module);
  }
}
// ----------------------- 以下是暴露的方法内容 ----------------------------
defineExpose({});
</script>
<style scoped lang="less"></style>
