<!--
 * @Author: zhuoda
 * @Date: 2021-08-03 10:27:11
 * @LastEditTime: 2022-06-02
 * @LastEditors: zhuoda
 * @Description: 
 * @FilePath: /smart-admin/src/components/menu-location-breadcrumb/index.vue
-->
<template>
  <a-breadcrumb separator=">" style="display: inline">
    <a-breadcrumb-item v-for="(item, index) in parentMenuList" :key="index">{{
      item.title
    }}</a-breadcrumb-item>
    <a-breadcrumb-item>{{ currentRoute.meta.title }}</a-breadcrumb-item>
  </a-breadcrumb>
</template>
<script setup>
import { computed } from "@vue/reactivity";
import { useRoute } from "vue-router";
let currentRoute = useRoute();
const parentMenuList = computed(() => {
  return currentRoute.meta.parentMenuList || [];
});
</script>
<style scoped lang="less"></style>
