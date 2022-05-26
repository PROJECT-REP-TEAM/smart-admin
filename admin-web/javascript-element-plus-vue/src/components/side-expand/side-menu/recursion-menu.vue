<!--
 * @Author: LiHaiFan
 * @Date: 2021-08-25 17:52:43
 * @LastEditTime: 2021-11-17 10:07:54
 * @LastEditors: LiHaiFan
 * @Description:
 * @FilePath: /employment-admin-web/src/components/side-expand/side-menu/recursion-menu.vue
-->
<template>
  <div class="resursion-container">
    <!-- 顶部顶级菜单名称 -->
    <div class="top-menu">
      <span class="el-breadcrumb">{{ props.selectedMenu.menuName }}</span>
    </div>
    <!-- 次级菜单展示 -->
    <el-menu :default-active="nowRoute" router>
      <template v-for="item in props.selectedMenu.children" :key="item.menuId">
        <template v-if="item.visibleFlag">
          <template v-if="$lodash.isEmpty(item.children)">
            <el-menu-item :index="item.path">
<!--              <component style="width: 20px; margin-right: 3px" :is="$icons[item.icon]" />-->
              {{ item.menuName }}
            </el-menu-item>
          </template>
          <template v-else>
            <sub-menu :menu-info="item" @turnToPage="turnToPage" />
          </template>
        </template>
      </template>
    </el-menu>
  </div>
</template>
<script setup>
  import { computed, watch } from 'vue';
  import { useRoute } from 'vue-router';
  import { router } from '/@/router';
  import SubMenu from './sub-menu.vue';
  import _ from 'lodash';

  // ----------------------- 以下是字段定义 emits props ---------------------
  let props = defineProps({
    selectedMenu: Object,
  });
  let currentRoute = useRoute();
  // ----------------------- 以下是计算属性 watch监听 ------------------------
  const nowRoute = computed(() => {
    return currentRoute.path;
  });

  watch(
    () => props.selectedMenu,
    (e) => {
      if (!e?.children || _.isEmpty(e?.children) || e?.children.every((e) => !e.visibleFlag)) {
        return;
      }
      if (e.children.some((x) => x.menuId == currentRoute.name)) {
        return;
      }
      let visibleMenu = e?.children.filter((e) => e.visibleFlag);
      turnToPage(visibleMenu[0]);
    },
    {
      immediate: true,
    }
  );
  // ----------------------- 以下是生命周期 ---------------------------------

  // ----------------------- 以下是方法 ------------------------------------
  // 页面跳转
  function turnToPage(route) {
    router.push({ name: route.menuId.toString() });
  }
  // ----------------------- 以下是暴露的方法内容 ----------------------------
  defineExpose({});
</script>
<style scoped lang="scss">
  .resursion-container {
    height: 100%;
    background: #ffffff;
  }
  .top-menu {
    overflow: hidden;
    display: flex;
    align-items: center;
    justify-content: center;
    height: $header-user-height;
    font-size: 16px;
    color: #515a6e;
    border-bottom: 1px solid #f3f3f3;
    border-right: 1px solid #f3f3f3;
  }
</style>
