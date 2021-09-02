<!--
 * @Author: zhuoda
 * @Date: 2021-08-25 17:52:43
 * @LastEditTime: 2021-08-27
 * @LastEditors: zhuoda
 * @Description: 
 * @FilePath: /smart-admin/src/components/side-expand/side-menu/recursion-menu.vue
-->
<template>
  <div class="resursion-container">
    <!-- 顶部顶级菜单名称 -->
    <div class="top-menu">
      <span class="ant-menu">{{ props.selectedMenu?.menuName }}</span>
    </div>
    <!-- 次级菜单展示 -->
    <a-menu :selectedKeys="selectedKeys" :openKeys="openKeys" mode="inline">
      <template v-for="item in props.selectedMenu?.children" :key="item.menuId">
        <template v-if="item.visibleFlag">
          <template v-if="$lodash.isEmpty(item.children)">
            <a-menu-item :key="item.menuId.toString()" @click="turnToPage(item)">
              <template #icon v-if="item.icon">
                <component :is="$antIcons[item.icon]" />
              </template>
              {{ item.menuName }}
            </a-menu-item>
          </template>
          <template v-else>
            <sub-menu :menu-info="item" :key="item.menuId" @turnToPage="turnToPage" />
          </template>
        </template>
      </template>
    </a-menu>
  </div>
</template>
<script setup lang="ts">
import { computed } from "vue";
import { useRoute } from "vue-router";
import { router } from "/@/router";
import SubMenu from "./sub-menu.vue";
import { MenuTreeVo } from "/@/api/system/menu/model/menu-tree-vo";

// ----------------------- 以下是字段定义 emits props ---------------------

let props = defineProps<{
  selectedMenu?: MenuTreeVo;
}>();
defineEmits<{
  (e: "update:value"): void;
}>();
let currentRoute = useRoute();
// ----------------------- 以下是计算属性 watch监听 ------------------------
const selectedKeys = computed(() => {
  return [currentRoute.name];
});

const openKeys = computed(() => {
  return (currentRoute.meta.parentMenuList || []).map(
    (e: Record<string, string>) => e.name
  );
});
// ----------------------- 以下是生命周期 ---------------------------------

// ----------------------- 以下是方法 ------------------------------------
// 页面跳转
function turnToPage(route: MenuTreeVo) {
  router.push({ name: route.menuId.toString() });
}
// ----------------------- 以下是暴露的方法内容 ----------------------------
defineExpose({});
</script>
<style scoped lang="less">
.resursion-container {
  height: 100%;
  background: #ffffff;
}
.top-menu {
  overflow: hidden;
  display: flex;
  align-items: center;
  justify-content: center;
  height: @header-user-height;
  font-size: 16px;
  color: #515a6e;
  border-bottom: 1px solid #f3f3f3;
  border-right: 1px solid #f3f3f3;
}
</style>
