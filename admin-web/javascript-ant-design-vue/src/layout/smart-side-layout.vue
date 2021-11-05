<template>
  <a-layout :class="['admin-layout', 'smart-scroll']" style="min-height: 100%">
    <!-- 侧边菜单 side-menu -->
    <a-layout-sider
      :theme="theme"
      :class="['side-menu', 'smart-scroll']"
      width="256px"
      :collapsed="collapsed"
      :trigger="null"
    >
      <div :style="`height: ${windowHeight}px;overflow-y:scroll`" class="smart-scroll">
        <!-- 左侧菜单 -->
        <side-menu
          :theme="theme"
          :menuData="sideMenuData"
          :collapsed="collapsed"
          :collapsible="true"
        />
      </div>
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
            <smart-header-user-space />
          </a-col>
        </a-row>
        <smart-page-tag />
      </a-layout-header>

      <!--中间内容-->
      <a-layout-content class="admin-layout-content" id="smartAdminLayoutContent">
        <router-view ref="tabContent" />
      </a-layout-content>

      <!-- footer 版权公司信息 -->
      <a-layout-footer class="smart-layout-footer"> <smart-footer /></a-layout-footer>

      <a-back-top :target="backTopTarget" :visibilityHeight="80" />
    </a-layout>
  </a-layout>
</template>

<script lang="ts" setup>
import SmartFooter from "./components/smart-footer/index.vue";
import SmartHeaderUserSpace from "./components/smart-header-user-space/index.vue";
import SideMenu from "/@/components/side-menu/index.vue";
import { computed, onMounted, ref } from "vue";
import { useAppConfigStore } from "/@/store/modules/system/app-config";
import { MenuUnfoldOutlined, MenuFoldOutlined } from "@ant-design/icons-vue";
import MenuLocationBreadcrumb from "/@/components/menu-location-breadcrumb/index.vue";
import SmartPageTag from "./components/smart-page-tag/index.vue";
import watermark from "/@/lib/smart-wartermark";
import { useUserStore } from "/@/store/modules/system/user";

const windowHeight = window.innerHeight;
const collapsed = ref(false);

let appConfigStore = useAppConfigStore();

const sideMenuData = new Array();

const backTopTarget = () => {
  return document.getElementById("smartAdminMain");
};

const theme = computed(() => appConfigStore.theme);

onMounted(() => {
  watermark.set("smartAdminLayoutContent", useUserStore().userInfo.actualName);
});
</script>

<style lang="less" scoped>
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
