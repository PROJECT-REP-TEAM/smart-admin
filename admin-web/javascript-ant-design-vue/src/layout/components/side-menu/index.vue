<!--
 * @Description: 
 * @Author: zhuoda
 * @Date: 2021-11-05
 * @LastEditTime: 2022-06-22
 * @LastEditors: zhuoda
-->

<template>
  <!--
    左侧菜单分为两部分：
    1、顶部logo区域，包含 logo和名称
    2、下方菜单区域
   -->

  <!-- 顶部logo区域 -->
  <div class="logo" v-if="!collapsed">
    <img :src="logoImg" />
  </div>
  <div class="min-logo" v-if="collapsed">
    <img :src="logoMinImg" />
  </div>

  <!-- 下方菜单区域： 这里使用一个递归菜单解决 -->
  <recursion-menu :collapsed="collapsed" ref="menu" />
</template>

<script setup>
import RecursionMenu from "./recursion-menu.vue";
import { computed, watch, ref } from "vue";
import logoImg from "/@/assets/images/logo/smart-admin-logo.png";
import logoMinImg from "/@/assets/images/logo/smart-admin-logo-min.png";

const props = defineProps({
  collapsed: {
    type: Boolean,
    required: false,
    default: false,
  },
});

const menu = ref();

watch(
  () => props.collapsed,
  (newValue, oldValue) => {
    // 如果是展开菜单的话，重新获取更新菜单的展开项: openkeys和selectKeys
    if (!newValue) {
      menu.value.updateOpenKeysAndSelectKeys();
    }
  }
);
</script>

<style lang="less" scoped>
.shadow {
  box-shadow: 2px 0 6px rgba(0, 21, 41, 0.35);
}
.side-menu {
  min-height: 100vh;
  overflow-y: auto;
  z-index: 10;

  .min-logo {
    height: @header-user-height;
    position: fixed;
    line-height: @header-user-height;
    padding: 0px 15px 0px 15px;
    width: auto;
    z-index: 9999;
    background-color: #001529;

    -webkit-transition: all 0.3s;
    transition: all 0.3s;
    overflow: hidden;
  }

  .logo {
    height: @header-user-height;
    position: fixed;
    line-height: @header-user-height;
    padding: 0px 15px 0px 15px;
    width: 197px;
    z-index: 9999;
    background-color: #001529;

    -webkit-transition: all 0.3s;
    transition: all 0.3s;
    overflow: hidden;
    &.light {
      background-color: #fff;
      h1 {
        color: @primary-color;
      }
    }
    h1 {
      color: @menu-dark-highlight-color;
      font-size: 20px;
      margin: 0 0 0 12px;
      display: inline-block;
      vertical-align: middle;
    }
    img {
      width: 100%;
      vertical-align: middle;
    }
  }
}
.menu {
  padding: 16px 0;
}
</style>
