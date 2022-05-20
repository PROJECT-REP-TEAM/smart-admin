<!--
 * @Description:
 * @Author: zhuoda
 * @Date: 2021-08-03
 * @LastEditTime: 2021-08-28
 * @LastEditors: zhuoda
-->
<template>
  <a-space :size="10">
    <div class="user-space-item">
      <header-avatar />
    </div>
  </a-space>
</template>

<script lang="ts">
import HeaderAvatar from "./header-avatar.vue";

export default {
  components: { HeaderAvatar },
};
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
</style>
