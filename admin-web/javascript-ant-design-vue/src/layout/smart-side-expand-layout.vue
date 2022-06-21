<!--
 * @Author: zhuoda
 * @Date: 2021-08-25 21:52:23
 * @LastEditTime: 2022-06-14
 * @LastEditors: zhuoda
 * @Description:
 * @FilePath: /smart-admin/src/layout/smart-side-expand-layout.vue
-->
<template>
  <a-layout :class="['admin-layout', 'smart-scroll']" style="min-height: 100%">
    <!-- 侧边菜单 side-menu -->
    <a-layout-sider
      theme="light"
      :class="['side-menu', 'smart-scroll']"
      :collapsed="collapsed"
      :trigger="null"
    >
      <!-- 左侧菜单 -->
      <side-expand-menu :collapsed="collapsed" />
    </a-layout-sider>

    <!--
  中间内容，一共三部分：
  1、顶部
  2、中间内容区域
  3、底部（一般是公司版权信息）
 -->
    <a-layout
      class="admin-layout-main smart-scroll"
      :style="`height: ${windowHeight}px`"
      id="smartAdminMain"
    >
      <!-- 顶部头部信息 -->
      <a-layout-header class="smart-layout-header">
        <a-row justify="space-between" class="smart-layout-header-user">
          <a-col class="smart-layout-header-left">
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
          <a-col class="smart-layout-header-right">
            <header-user-space />
          </a-col>
        </a-row>
        <page-tag />
      </a-layout-header>

      <!--中间内容-->
      <a-layout-content class="admin-layout-content" id="smartAdminLayoutContent">
        <!--不keepAlive的iframe使用单个iframe组件-->
        <iframeIndex v-show="iframeNotKeepAlivePageFlag"
                     :key="route.name"
                     :name="route.name"
                     :url="route.meta.frameUrl"></iframeIndex>
        <!--keepAlive的iframe 每个页面一个iframe组件-->
        <iframeIndex v-for="item in keepAliveIframePages"
                     v-show="route.name == item.name"
                     :key="item.name"
                     :name="item.name"
                     :url="item.meta.frameUrl"></iframeIndex>
        <!--非iframe使用router-view-->
        <router-view v-show="!iframeNotKeepAlivePageFlag && keepAliveIframePages.every(e=>route.name != e.name)"
                     v-slot="{ Component }">
          <keep-alive :include="keepAliveIncludes">
            <component :is="Component" />
          </keep-alive>
        </router-view>
      </a-layout-content>

      <!-- footer 版权公司信息 -->
      <a-layout-footer class="smart-layout-footer"> <SmartFooter /></a-layout-footer>

      <a-back-top :target="backTopTarget" :visibilityHeight="80" />
    </a-layout>
  </a-layout>
</template>
<script setup>
import SideExpandMenu from "./components/side-expand-menu/index.vue";
import SmartFooter from "./components/smart-footer/index.vue";
import HeaderUserSpace from "./components/header-user-space/index.vue";
import { ref, onMounted } from "vue";
import MenuLocationBreadcrumb from "./components/menu-location-breadcrumb/index.vue";
import PageTag from "./components/page-tag/index.vue";
import watermark from "/@/lib/smart-wartermark";
import { useUserStore } from "/@/store/modules/system/user";
import IframeIndex from "/@/components/iframe/iframe-index.vue"
import { smartKeepAlive } from "./smart-keep-alive"

// ----------------------- 以下是字段定义 emits props ---------------------
const windowHeight = window.innerHeight;
const collapsed = ref(false);

// ----------------------- 以下是计算属性 watch监听 ------------------------
// ----------------------- 以下是生命周期 ---------------------------------
onMounted(() => {
  watermark.set("smartAdminLayoutContent", useUserStore().actualName);
});
// ----------------------- 以下是方法 ------------------------------------
const backTopTarget = () => {
  return document.getElementById("smartAdminMain");
};
// ----------------------- keep-alive相关 -----------------------
let { route,keepAliveIncludes,iframeNotKeepAlivePageFlag,keepAliveIframePages } = smartKeepAlive();
// ----------------------- 以下是暴露的方法内容 ----------------------------
defineExpose({});
</script>
<style scoped lang="less">
.smart-layout-header {
  background: #fff;
  padding: 0;
  height: @header-height;
  z-index: 999;
}

.smart-layout-header-user {
  height: @header-user-height;
  border-bottom: 1px solid #f6f6f6;
}

.smart-layout-header-left {
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

.smart-layout-header-right {
  display: flex;
  height: @header-user-height;
}

.admin-layout {
  .side-menu {
    flex: 0 !important;
    min-width: inherit !important;
    max-width: none !important;
    width: auto !important;
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
    overflow-x: hidden;
    overflow-y: scroll;
  }

  .admin-layout-content {
    min-height: auto;
    position: relative;
    padding: 10px;
  }
}

.smart-layout-footer {
  position: relative;
}
</style>
