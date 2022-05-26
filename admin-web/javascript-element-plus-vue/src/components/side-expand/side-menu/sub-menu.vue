<!--
 * @Author: LiHaiFan
 * @Date: 2021-08-03 10:27:11
 * @LastEditTime: 2021-11-16
 * @LastEditors: zhuoda
 * @Description: 
 * @FilePath: /employment-admin-web/src/components/side-expand/side-menu/sub-menu.vue
-->
<template>
  <el-submenu :index="props.menuInfo.path">
    <i :class="props.menuInfo.icon"></i>
    {{ props.menuInfo.menuName }}
    <template v-for="item in props.menuInfo?.children" :key="item.menuId">
      <template v-if="item.visibleFlag">
        <template v-if="!item.children">
          <el-menu-item :index="item.path">
            <i v-if="item.icon" :class="item.icon"></i>
            {{ item.menuName }}
          </el-menu-item>
        </template>
        <template v-else>
          <sub-menu :menu-info="item" @turnToPage="turnToPage" />
        </template>
      </template>
    </template>
  </el-submenu>
</template>
<script setup>
// ----------------------- 以下是字段定义 emits props ---------------------
let props = defineProps({
  menuInfo: Array,
});
const emits = defineEmits(["turnToPage"]);
// ----------------------- 以下是计算属性 watch监听 ------------------------

// ----------------------- 以下是生命周期 ---------------------------------

// ----------------------- 以下是方法 ------------------------------------
const turnToPage = (route) => {
  emits("turnToPage", route);
};
// ----------------------- 以下是暴露的方法内容 ----------------------------
defineExpose({});
</script>
<style scoped lang="scss"></style>
