<!--
 * @Author: zhuoda
 * @Date: 2021-08-03 10:27:11
 * @LastEditTime: 2021-08-26
 * @LastEditors: zhuoda
 * @Description: 
 * @FilePath: /smart-admin/src/components/side-expand/side-menu/sub-menu.vue
-->
<template>
  <a-sub-menu :key="props.menuInfo?.menuId">
    <template #icon>
      <component :is="$antIcons[props.menuInfo?.icon]" />
    </template>
    <template #title>{{ props.menuInfo?.menuName }}</template>
    <template v-for="item in props.menuInfo?.children" :key="item.menuId">
      <template v-if="item.visibleFlag">
        <template v-if="!item.children">
          <a-menu-item :key="item.menuId" @click="turnToPage(item)">
            <template #icon>
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
  </a-sub-menu>
</template>
<script setup lang="ts">
import { MenuTreeVo } from "/@/api/system/menu/model/menu-tree-vo";
// ----------------------- 以下是字段定义 emits props ---------------------
let props = defineProps<{
  menuInfo?: MenuTreeVo;
}>();
let emits = defineEmits<{
  (e: "turnToPage", value: MenuTreeVo): void;
}>();
// ----------------------- 以下是计算属性 watch监听 ------------------------

// ----------------------- 以下是生命周期 ---------------------------------

// ----------------------- 以下是方法 ------------------------------------
const turnToPage = (route: MenuTreeVo) => {
  emits("turnToPage", route);
};
// ----------------------- 以下是暴露的方法内容 ----------------------------
defineExpose({});
</script>
<style scoped lang="less">
::v-deep(.ant-menu-item-selected) {
  border-right: 3px !important;
}
</style>
