<!--
 * @Author: zhuoda
 * @Date: 2021-08-25 17:07:41
 * @LastEditTime: 2021-08-28 16:46:25
 * @LastEditors: zhuoda
 * @Description:
 * @FilePath: /smart-admin/src/components/side-expand/side-menu/index.vue
-->
<template>
  <div class="menu-container">
    <!-- logo 一级导航 -->
    <TopMenu ref="topMenu" class="topMenu" :menuTree="menuTree" />
    <!-- 次级导航 -->
    <RecursionMenu v-if="showRecursionMenu" class="recursionMenu" :selectedMenu="selectedMenu" />
  </div>
</template>
<script setup lang="ts">
  import TopMenu from './top-menu.vue';
  import RecursionMenu from './recursion-menu.vue';
  import { useUserStore } from '/@/store/modules/system/user';
  import { computed } from '@vue/reactivity';
  import { MenuTreeVo } from '/@/api/system/menu/model/menu-tree-vo';
  import { ref } from 'vue';
  // ----------------------- 以下是字段定义 emits props ---------------------
  const props = defineProps<{
    collapsed?: boolean;
  }>();
  defineEmits<{
    (e: 'update:value'): void;
  }>();
  const topMenu = ref();
  // ----------------------- 以下是计算属性 watch监听 ------------------------
  const menuTree = computed(() => useUserStore().getMenuTree || []);
  const selectedMenu = computed<MenuTreeVo>(() => {
    if (topMenu.value) {
      return topMenu.value.selectedMenu;
    }
    return {};
  });
  const showRecursionMenu = computed(
      () =>
          !props.collapsed &&
          selectedMenu.value &&
          selectedMenu.value.children &&
          selectedMenu.value.children.some((e) => e.visibleFlag)
  );
  // ----------------------- 以下是生命周期 ---------------------------------

  // ----------------------- 以下是方法 ------------------------------------

  // ----------------------- 以下是暴露的方法内容 ----------------------------
  defineExpose({});
</script>
<style scoped lang="less">
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
