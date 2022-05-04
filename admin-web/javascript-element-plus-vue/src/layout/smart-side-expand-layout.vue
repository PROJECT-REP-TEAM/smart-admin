
<template>
  <el-container :class="['admin-layout', 'smart-scroll']" style="min-height: 100%">
    <!-- 侧边菜单 side-menu -->
    <el-aside
      theme="light"
      :class="['side-menu', 'smart-scroll']"
      :collapsed="collapsed"
      :trigger="null"
    >
      <expand-side-menu />
    </el-aside>

    <!--
  中间内容，一共三部分：
  1、顶部
  2、中间内容区域
  3、底部（一般是公司版权信息）
 -->
    <el-container
      class="admin-layout-main smart-scroll"
      :style="`height: ${windowHeight}px`"
      id="smartAdminMain"
    >
      <!-- 顶部头部信息 -->
      <el-header class="smart-layout-header">
        <div class="smart-layout-header-user">
          <div class="smart-layout-header-left">
            <span class="location-breadcrumb">
              <menu-location-breadcrumb />
            </span>
          </div>
          <!---用戶操作区域-->
          <div class="smart-layout-header-right">
            <smart-header-user-space />
          </div>
        </div>
        <smart-page-tag />
      </el-header>

      <!--中间内容-->
      <el-main class="admin-layout-content" id="smartAdminLayoutContent">
        <router-view />
      </el-main>

      <!-- footer 版权公司信息 -->
      <el-footer class="smart-layout-footer"> <smart-footer /></el-footer>

      <el-backtop :visibilityHeight="80" />
    </el-container>
  </el-container>
</template>
<script setup>
import { onMounted, ref } from 'vue';
import watermark from '/@/lib/smart-wartermark';
import { useUserStore } from '/@/store/modules/user';
// ----------------------- 以下是字段定义 emits props ---------------------
const windowHeight = window.innerHeight;
const collapsed = ref(false);

// ----------------------- 以下是计算属性 watch监听 ------------------------
// ----------------------- 以下是生命周期 ---------------------------------
onMounted(() => {
  watermark.set("smartAdminLayoutContent", useUserStore().userInfo.actualName);
});
// ----------------------- 以下是方法 ------------------------------------
const backTopTarget = () => {
  return document.getElementById("smartAdminMain");
};
// ----------------------- 以下是暴露的方法内容 ----------------------------
defineExpose({});
</script>

<style scoped lang="scss">
.smart-layout-header {
  background: #fff;
  padding: 0;
  height: $header-height;
  z-index: 999;
}

.smart-layout-header-user {
  height: $header-user-height;
  border-bottom: 1px solid #f6f6f6;
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.smart-layout-header-left {
  display: flex;
  align-items: center;
  justify-content: center;
  height: $header-user-height;

  .collapsed-button {
    margin-left: 10px;
    line-height: $header-user-height;
  }

  .location-breadcrumb {
    margin-left: 15px;
    line-height: $header-user-height;
  }
}

.smart-layout-header-right {
  display: flex;
  align-items: center;
  justify-content: center;
  height: $header-user-height;
}

.admin-layout {
  .side-menu {
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
    overflow-x: hidden;
  }
}

.smart-layout-footer {
  position: relative;
}
</style>
