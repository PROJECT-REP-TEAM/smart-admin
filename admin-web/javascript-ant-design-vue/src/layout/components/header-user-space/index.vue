<!--
 * @Description: 
 * @Author: zhuoda
 * @Date: 2021-08-03
 * @LastEditTime: 2022-06-23
 * @LastEditors: zhuoda
-->
<template>
  <a-space :size="10">
    <div class="setting">
      <a-input-search style="margin-right: 50px" placeholder="查询订单、员工、通知" enter-button="搜索" size="small" />
      <HeaderNotice ref="headerNotice" />
      <a-button type="text" @click="showSetting" class="operate-icon">
        <template #icon><switcher-outlined /></template>
        i18n
      </a-button>
      <a-button type="text" @click="showSetting" class="operate-icon">
        <template #icon><setting-outlined /></template>
      </a-button>
    </div>
    <div class="user-space-item">
      <header-avatar />
    </div>

    <HeaderSetting ref="headerSetting" />
  </a-space>
</template>

<script setup>
  import HeaderAvatar from './header-avatar.vue';
  import HeaderSetting from './header-setting.vue';
  import HeaderNotice from './header-notice.vue';
  import { ref } from 'vue';

  const headerSetting = ref();
  function showSetting() {
    headerSetting.value.show();
  }
  const headerNotice = ref();
  function showNotice() {
    headerNotice.value.showNotice();
  }
</script>

<style lang="less" scoped>
  .user-space-item {
    height: 100%;
    color: inherit;
    padding: 0 12px;
    cursor: pointer;
    align-self: center;

    a {
      color: inherit;

      i {
        font-size: 16px;
      }
    }
  }

  .user-space-item:hover {
    color: @primary-color;
    background: @hover-bg-color;
  }

  .setting {
    height: @header-user-height;
    line-height: @header-user-height;
    vertical-align: middle;
    display: flex;
    align-items: center;
  }
  .operate-icon {
    margin-left: 25px;
  }
</style>
