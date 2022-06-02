<!--
 * @Author: zhuoda
 * @Date: 2021-08-25 17:09:44
 * @LastEditTime: 2022-06-02
 * @LastEditors: zhuoda
 * @Description:
 * @FilePath: /smart-admin/src/components/side-expand/side-menu/top-menu.vue
-->
<template>
  <div class="top-menu-container">
    <!-- 顶部logo区域 -->
    <div class="logo">
      <h3 style="color: white">SmartAdmin</h3>
    </div>
    <!-- 一级菜单展示 -->
    <a-menu
      :selectedKeys="selectedKeys"
      mode="inline"
      theme="dark"
      :inline-collapsed="collapsed"
    >
      <template v-for="item in props.menuTree" :key="item.menuId">
        <template v-if="item.visibleFlag">
          <a-menu-item :key="item.menuId.toString()" @click="selectMenu(item)">
            <template #icon>
              <component :is="$antIcons[item.icon]" />
            </template>
            {{ item.menuName }}
          </a-menu-item>
        </template>
      </template>
    </a-menu>
  </div>
</template>
<script setup>
import _ from "lodash";
import { computed, ref, watch } from "vue";
import { useRoute } from "vue-router";
import { appDefaultConfig } from "/@/config/app-config";
import { MENU_TYPE_ENUM } from "/@/constants/system/menu/menu-enum";
import { router } from "/@/router";

// ----------------------- 以下是字段定义 emits props ---------------------
const props = defineProps({
  menuTree: Array,
});
const selectedMenu = ref();
let currentRoute = useRoute();
// ----------------------- 以下是计算属性 watch监听 ------------------------
const selectedKeys = computed(() => {
  if (selectedMenu.value) {
    return [selectedMenu.value.menuId.toString()];
  }
  return (currentRoute.meta.parentMenuList || []).map((e) => e.name);
});
watch(
  currentRoute,
  () => {
    selectedMenu.value = undefined;
    let menuList = props.menuTree?.map((e) => e.menuId.toString());
    let parentIdList = _.intersection(menuList, selectedKeys.value);
    if (parentIdList.length > 0) {
      let parentId = parentIdList[0];
      let parentItem = props.menuTree?.find((e) => e.menuId == Number(parentId));
      selectedMenu.value = parentItem;
    }
  },
  {
    immediate: true,
  }
);
// ----------------------- 以下是生命周期 ---------------------------------
// ----------------------- 以下是方法 ------------------------------------
// 页面跳转
function selectMenu(route) {
  selectedMenu.value = route;
  if (
    route.menuType == MENU_TYPE_ENUM.MENU.value &&
    (_.isEmpty(route.children) || route.children?.every((e) => !e.visibleFlag))
  ) {
    router.push({ name: route.menuId.toString() });
  }
}
function goHome() {
  router.push({ name: appDefaultConfig.homePageName });
}
// ----------------------- 以下是暴露的方法内容 ----------------------------
defineExpose({
  selectedMenu,
});
</script>
<style scoped lang="less">
.top-menu-container {
  height: 100%;
  background: #001529;
}
.logo {
  overflow: hidden;
  display: flex;
  align-items: center;
  justify-content: center;
  height: 67px;
  img {
    width: 83px;
    cursor: pointer;
  }
}
</style>
