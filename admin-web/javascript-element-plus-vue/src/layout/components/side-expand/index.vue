<!--
 * @Author: LiHaiFan
 * @Date: 2021-08-25 17:07:41
 * @LastEditTime: 2021-11-15
 * @LastEditors: zhuoda
 * @Description:
 * @FilePath: /employment-admin-web/src/components/side-expand/side-menu/index.vue
-->
<template>
  <div class="menu-container">
    <!-- logo 一级导航 -->
    <TopMenu ref="topMenu" class="topMenu" :menuTree="menuTree" />
    <!-- 次级导航 -->
    <RecursionMenu
      v-if="showRecursionMenu"
      class="recursionMenu"
      :selectedMenu="selectedMenu"
    />
  </div>
</template>
<script setup>
import TopMenu from "./top-menu.vue";
import RecursionMenu from "./recursion-menu.vue";
import { useUserStore } from "/@/store/modules/user";
import { ref,computed } from "vue";
// ----------------------- 以下是字段定义 emits props ---------------------
const topMenu = ref();
// ----------------------- 以下是计算属性 watch监听 ------------------------
const menuTree = computed(() => useUserStore().getMenuTree || []);
const selectedMenu = computed(() => {
  if (topMenu.value) {
    return topMenu.value.selectedMenu;
  }
  return {};
});
const showRecursionMenu = computed(
  () =>
    selectedMenu.value &&
    selectedMenu.value.children &&
    selectedMenu.value.children.some((e) => e.visibleFlag)
);
// ----------------------- 以下是生命周期 ---------------------------------

// ----------------------- 以下是方法 ------------------------------------

// ----------------------- 以下是暴露的方法内容 ----------------------------
defineExpose({});
</script>
<style scoped lang="scss">
.menu-container {
  display: flex;
  height: 100%;
  .topMenu {
    width: 114px;
    flex-shrink: 0;
  }
  .recursionMenu {
    min-width: 126px;
    flex: 1;
  }
}
</style>
