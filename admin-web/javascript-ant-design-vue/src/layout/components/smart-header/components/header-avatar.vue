<!--
 * @Description: 
 * @Author: zhuoda
 * @Date: 2021-08-03
 * @LastEditTime: 2021-08-28 16:34:22
 * @LastEditors: zhuoda
-->
<template>
  <a-dropdown>
    <div class="header-avatar" style="cursor: pointer">
      <a-avatar class="avatar" size="small" shape="circle" :src="avatar" />
      <span class="name">{{ name }}</span>
    </div>
    <a-menu :class="['avatar-menu']">
      <a-menu-item>
        <a-icon type="user" />
        <span>个人中心</span>
      </a-menu-item>
      <a-menu-item>
        <a-icon type="setting" />
        <span>设置</span>
      </a-menu-item>
      <a-menu-divider />
      <a-menu-item @click="onLogout">
        <a-icon style="margin-right: 8px" type="poweroff" />
        <span>退出登录</span>
      </a-menu-item>
    </a-menu>
  </a-dropdown>
</template>

<script  setup>
import { computed } from "vue";
import { useRouter } from "vue-router";
import { useAppConfigStore } from "/@/store/modules/system/app-config";
import { useUserStore } from "/@/store/modules/system/user";
import { clearAllCoolies } from "/@/utils/cookie-util";
import { localClear } from "/@/utils/local-util";

const name = computed(() => useAppConfigStore.name);
const avatar = computed(() => useAppConfigStore.avatar);

const router = useRouter();
const onLogout = () => {
  localClear();
  clearAllCoolies();
  useUserStore().logout();
  router.push({ name: "Login" });
};
</script>

<style lang="less" scoped>
.header-avatar {
  display: inline-flex;

  .avatar,
  .name {
    align-self: center;
  }

  .avatar {
    margin-right: 8px;
  }

  .name {
    font-weight: 500;
  }
}

.avatar-menu {
  width: 150px;
}
</style>
