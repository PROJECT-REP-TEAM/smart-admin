<!--
 * @Author: zhuoda
 * @Date: 2021-08-25 17:09:44
 * @LastEditTime: 2021-11-17 10:12:25
 * @LastEditors: LiHaiFan
 * @Description:
 * @FilePath: /employment-admin-web/src/components/side-expand/side-menu/top-menu.vue
-->
<template>
  <div class="top-menu-container">
    <!-- 顶部logo区域 -->
    <div class="logo">
      <h3 style="color: white">SmartAdmin</h3>
    </div>
    <!-- 一级菜单展示 -->
    <el-menu :default-active="defaultActive" background-color="#001529" active-text-color="#fff" text-color="#ffffffa6">
      <template v-for="item in props.menuTree" :key="item.menuId">
        <template v-if="item.visibleFlag">
          <el-menu-item :index="item.menuId" @click="selectMenu(item)">
<!--            <component style="width: 20px; margin-right: 3px" :is="$icons[item.icon]" />-->
            {{ item.menuName }}
          </el-menu-item>
        </template>
      </template>
    </el-menu>
  </div>
</template>
<script setup>
  import _ from 'lodash';
  import { computed, ref, watch, nextTick } from 'vue';
  import { useRoute } from 'vue-router';
  import { appDefaultConfig } from '/@/config/app-config';
  import { MENU_TYPE_ENUM } from '/@/constants/system/menu-enum';
  import { router } from '/@/router';
  import { useUserStore } from '/@/store/modules/user';

  // ----------------------- 以下是字段定义 emits props ---------------------
  const props = defineProps({
    menuTree: Array,
  });
  const selectedMenu = ref();
  let currentRoute = useRoute();
  // ----------------------- 以下是计算属性 watch监听 ------------------------
  const parentMenuIdList = computed(() => {
    let currentName = currentRoute.name;
    if (!currentName || typeof currentName !== 'string') {
      return [];
    }
    let menuParentIdListMap = useUserStore().getMenuParentIdListMap;
    let parentMenuList = menuParentIdListMap?.get(currentName) || [];
    return parentMenuList.map((e) => e.name);
  });
  const defaultActive = computed(() => {
    if (!selectedMenu.value || !selectedMenu.value.menuId) {
      return;
    }
    return selectedMenu.value.menuId;
  });
  watch(
    currentRoute,
    () => {
      selectedMenu.value = undefined;
      nextTick(() => {
        let menuList = props.menuTree.map((e) => e.menuId.toString());
        let parentIdList = _.intersection(menuList, parentMenuIdList.value);
        if (parentIdList.length > 0) {
          let parentId = parentIdList[0];
          let parentItem = props.menuTree?.find((e) => e.menuId == Number(parentId));
          selectedMenu.value = parentItem;
        }
      });
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
    if (route.menuType == MENU_TYPE_ENUM.MENU.value && (_.isEmpty(route.children) || route.children?.every((e) => !e.visibleFlag))) {
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
<style scoped lang="scss">
  .el-menu-item:hover {
    color: #fff;
  }

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
      width: 100px;
      cursor: pointer;
    }
  }
  :deep(.el-menu) {
    border-right-width: 0;
  }
</style>
