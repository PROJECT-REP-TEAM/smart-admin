<!--
 * @Author: zhuoda
 * @Date: 2021-08-03 10:27:11
 * @LastEditTime: 2022-06-14
 * @LastEditors: zhuoda
 * @Description:
 * @FilePath: /smart-admin/src/layout/smart-layout.vue
-->
<template>
  <!--左侧菜单 模式-->
  <smart-side-layout />
  <!--左侧展开菜单 模式-->
<!--   <smart-side-expand-layout />-->
</template>
<script setup>
import { computed } from "vue";
import { useAppConfigStore } from "/@/store/modules/system/app-config";
import SmartSideLayout from "/@/layout/smart-side-layout.vue";
import SmartSideExpandLayout from "./smart-side-expand-layout.vue";

const appConfigStore = useAppConfigStore();
const layout = computed(() => appConfigStore.layout);
</script>
