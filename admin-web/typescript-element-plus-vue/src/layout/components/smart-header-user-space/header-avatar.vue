<template>
  <el-dropdown class="header-trigger">
    <div class="user-info" style="cursor: pointer">
      <span class="name">{{ userInfo.actualName }}</span>
      <el-avatar style="margin: 0 10px" :size="24">
        {{ avatarName }}
      </el-avatar>
    </div>
    <template #dropdown>
      <el-dropdown-menu class="avatar-menu">
        <el-dropdown-item @click="onLogout">退出登录</el-dropdown-item>
      </el-dropdown-menu>
    </template>
  </el-dropdown>
</template>
<script setup lang="ts">
  import { computed, ref } from '@vue/reactivity';
  import { onMounted } from '@vue/runtime-core';
  import { useRouter } from 'vue-router';
  import { useUserStore } from '/@/store/modules/system/user';
  import { clearAllCoolies } from '/@/utils/cookie-util';
  import { localClear } from '/@/utils/local-util';
  // 头像背景颜色
  const AVATAR_BACKGROUND_COLOR_ARRAY = ['#f56a00', '#87d068', '#1890ff'];

  // ----------------------- 以下是字段定义 emits props ---------------------
  const avatarName = ref('');
  const router = useRouter();
  // ----------------------- 以下是计算属性 watch监听 ------------------------
  const userInfo = computed(() => useUserStore().getUserInfo);

  // ----------------------- 以下是方法 ------------------------------------
  const onLogout = () => {
    localClear();
    clearAllCoolies();
    useUserStore().logout();
    router.push({ name: 'Login' });
  };

  function updateAvatar() {
    const name = useUserStore().getUserInfo.actualName;
    if (name) {
      avatarName.value = name.substr(0, 1);
      const avatar = document.getElementById('smartAdminAvatar');
      if (avatar) {
        avatar.style.backgroundColor = AVATAR_BACKGROUND_COLOR_ARRAY[hashcode(avatarName.value) % 3];
      }
    }
  }

  function hashcode(str: string): number {
    let hash = 0,
      i,
      chr;
    if (str.length === 0) return hash;
    for (i = 0; i < str.length; i++) {
      chr = str.charCodeAt(i);
      hash = (hash << 5) - hash + chr;
      hash |= 0; // Convert to 32bit integer
    }
    return hash;
  }

  onMounted(updateAvatar);

  // ----------------------- 以下是暴露的方法内容 ----------------------------
  defineExpose({});
</script>
<style lang="scss">
  .user-info {
    display: flex;
    align-items: center;
    justify-content: center;
  }
</style>
