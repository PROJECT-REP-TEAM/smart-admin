<template>
  <a-layout :class="['admin-layout', 'smart-scroll']" style="min-height: 100%">
    <!-- 侧边菜单 side-menu -->
    <a-layout-sider
      :class="['side-menu', 'smart-scroll']"
      :width="sideMenuWidth"
      :collapsed="collapsed"
      :theme="theme"
    >
      <!-- 左侧菜单 -->
      <side-menu :collapsed="collapsed" />
    </a-layout-sider>

    <!--
      中间内容，一共三部分：
      1、顶部
      2、中间内容区域
      3、底部（一般是公司版权信息）
     -->
    <a-layout
      id="smartAdminMain"
      :style="`height: ${windowHeight}px`"
      class="admin-layout-main smart-scroll"
    >
      <!-- 顶部头部信息 -->
      <a-layout-header class="layout-header">
        <a-row class="layout-header-user" justify="space-between">
          <a-col class="layout-header-left">
            <span class="collapsed-button">
              <menu-unfold-outlined
                v-if="collapsed"
                class="trigger"
                @click="() => (collapsed = !collapsed)"
              />
              <menu-fold-outlined
                v-else
                class="trigger"
                @click="() => (collapsed = !collapsed)"
              />
            </span>
            <span class="location-breadcrumb">
              <menu-location-breadcrumb />
            </span>
          </a-col>
          <!---用戶操作区域-->
          <a-col class="layout-header-right">
            <header-user-space />
          </a-col>
        </a-row>
        <page-tag />
      </a-layout-header>

      <!--中间内容-->
      <a-layout-content id="smartAdminLayoutContent" class="admin-layout-content">
        <!--不keepAlive的iframe使用单个iframe组件-->
        <iframeIndex
          v-show="iframeNotKeepAlivePageFlag"
          :key="route.name"
          :name="route.name"
          :url="route.meta.frameUrl"
        ></iframeIndex>
        <!--keepAlive的iframe 每个页面一个iframe组件-->
        <iframeIndex
          v-for="item in keepAliveIframePages"
          v-show="route.name == item.name"
          :key="item.name"
          :name="item.name"
          :url="item.meta.frameUrl"
        ></iframeIndex>
        <!--非iframe使用router-view-->
        <router-view
          v-show="
            !iframeNotKeepAlivePageFlag &&
            keepAliveIframePages.every((e) => route.name != e.name)
          "
          v-slot="{ Component }"
        >
          <keep-alive :include="keepAliveIncludes">
            <component :is="Component" />
          </keep-alive>
        </router-view>
      </a-layout-content>

      <!-- footer 版权公司信息 -->
      <a-layout-footer class="layout-footer">
        <smart-footer />
      </a-layout-footer>

      <a-back-top :target="backTopTarget" :visibilityHeight="80" />
    </a-layout>
  </a-layout>
</template>

<script setup>
import SideMenu from "./components/side-menu/index.vue";
import SmartFooter from "./components/smart-footer/index.vue";
import HeaderUserSpace from "./components/header-user-space/index.vue";
import { computed, onMounted, ref } from "vue";
import MenuLocationBreadcrumb from "./components/menu-location-breadcrumb/index.vue";
import PageTag from "./components/page-tag/index.vue";
import watermark from "/@/lib/smart-wartermark";
import { useUserStore } from "/@/store/modules/system/user";
import IframeIndex from "/@/components/iframe/iframe-index.vue";
import { smartKeepAlive } from "./smart-keep-alive";
import { useAppConfigStore } from "../store/modules/system/app-config";

const sideMenuWidth = computed(() => useAppConfigStore().$state.sideMenuWidth);
const theme = computed(() => useAppConfigStore().$state.sideMenuTheme);
const windowHeight = window.innerHeight;
const collapsed = ref(false);
onMounted(() => {
  watermark.set("smartAdminLayoutContent", useUserStore().actualName);
});
const backTopTarget = () => {
  return document.getElementById("smartAdminMain");
};
// ----------------------- keep-alive相关 -----------------------
let {
  route,
  keepAliveIncludes,
  iframeNotKeepAlivePageFlag,
  keepAliveIframePages,
} = smartKeepAlive();
</script>

<style lang="less" scoped>
.layout-header {
  background: #fff;
  padding: 0;
  height: @header-height;
  z-index: 999;
}

.layout-header-user {
  height: @header-user-height;
  border-bottom: 1px solid #f6f6f6;
}

.layout-header-left {
  display: flex;
  height: @header-user-height;

  .collapsed-button {
    margin-left: 10px;
    line-height: @header-user-height;
  }

  .location-breadcrumb {
    margin-left: 15px;
    line-height: @header-user-height;
  }
}

.layout-header-right {
  display: flex;
  height: @header-user-height;
}

.layout-container {
  height: calc(100vh - @header-height);
  overflow-x: hidden;
  overflow-y: auto;
}

.admin-layout {
  .side-menu {
    height: 100vh;
    overflow: scroll;

    &.fixed-side {
      position: fixed;
      height: 100vh;
      left: 0;
      top: 0;
    }
  }

  .virtual-side {
    transition: all 0.2s;
  }

  .virtual-header {
    transition: all 0.2s;
    opacity: 0;

    &.fixed-tabs.multi-page:not(.fixed-header) {
      height: 0;
    }
  }

  .admin-layout-main {
    overflow: hidden;
  }

  .admin-layout-content {
    min-height: auto;
    position: relative;
    padding: 10px;
  }
}

.layout-footer {
  position: relative;
}
</style>
