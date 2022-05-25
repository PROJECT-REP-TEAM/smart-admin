<!--
 * @Author: zhuoda
 * @Date: 2021-08-03 10:27:11
 * @LastEditTime: 2021-08-25 21:20:10
 * @LastEditors: zhuoda
 * @Description:
 * @FilePath: /smart-admin/src/components/menu-location-breadcrumb/index.vue
-->
<template>
  <a-breadcrumb separator=">" style="display: inline">
    <a-breadcrumb-item v-for="(item, index) in parentMenuList" :key="index">{{ item.title }}</a-breadcrumb-item>
    <a-breadcrumb-item>{{ currentRoute.meta.title }}</a-breadcrumb-item>
  </a-breadcrumb>
</template>
<script setup lang="ts">
  import { computed } from '@vue/reactivity';
  import { useRoute } from 'vue-router';
  import { useUserStore } from '/@/store/modules/system/user';
  // ----------------------- 以下是字段定义 emits props ---------------------
  let currentRoute = useRoute();
  // ----------------------- 以下是计算属性 watch监听 ------------------------
  const parentMenuList = computed(() => {
    let currentName = currentRoute.name;
    if(!currentName || typeof currentName !== 'string'){
      return [];
    }
    let menuParentIdListMap = useUserStore().getMenuParentIdListMap;
    return menuParentIdListMap?.get(currentName) || [];
  });
  // ----------------------- 以下是生命周期 ---------------------------------

  // ----------------------- 以下是方法 ------------------------------------

  // ----------------------- 以下是暴露的方法内容 ----------------------------
  defineExpose({});
</script>
<style scoped lang="less"></style>
