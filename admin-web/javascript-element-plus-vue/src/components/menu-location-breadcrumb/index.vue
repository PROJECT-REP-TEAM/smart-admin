<!--
 * @Author: LiHaiFan
 * @Date: 2021-11-15 18:18:59
 * @LastEditTime: 2021-11-16 09:53:35
 * @LastEditors: LiHaiFan
 * @Description:
 * @FilePath: /employment-admin-web/src/components/menu-location-breadcrumb/index.vue
-->
<template>
  <div>
    <el-breadcrumb separator=">">
      <el-breadcrumb-item v-for="(item, index) in parentMenuList" :key="index">
        {{ item.title }}
      </el-breadcrumb-item>
      <el-breadcrumb-item>{{ currentRoute.meta.title }}</el-breadcrumb-item>
    </el-breadcrumb>
  </div>
</template>
<script setup>
  import { useRoute } from 'vue-router';
  import { useUserStore } from '/@/store/modules/user';
  import { computed } from 'vue';
  // ----------------------- 以下是公用变量 emits props ----------------
  let props = defineProps({
    value: Number,
  });
  let emits = defineEmits(['update:value']);
  let currentRoute = useRoute();
  // ----------------------- 以下是暴露的方法内容 -----------------------
  defineExpose({});
  // ----------------------- 以下是生命周期 ----------------------------

  // ----------------------- 以下是业务内容 ----------------------------
  const parentMenuList = computed(() => {
    let currentName = currentRoute.name;
    if (!currentName || typeof currentName !== 'string') {
      return [];
    }
    let menuParentIdListMap = useUserStore().getMenuParentIdListMap;
    return menuParentIdListMap?.get(currentName) || [];
  });
</script>
