<!--
 * @Author: zhuoda
 * @Date: 2021-08-03 10:27:11
 * @LastEditTime: 2021-08-28 16:48:50
 * @LastEditors: zhuoda
 * @Description:
 * @FilePath: /smart-admin/src/layout/components/smart-header-user-space/header-avatar.vue
-->

<template>
  <a-dropdown class="header-trigger">
    <div style="cursor: pointer">
      <span class="name">{{ userInfo.actualName }}</span>
      <a-avatar style="margin: 0 10px" :size="24" id="smartAdminAvatar">
        {{ avatarName }}
      </a-avatar>
    </div>
    <template #overlay>
      <a-menu :class="['avatar-menu']">
        <a-menu-item @click="onLogout">
          <span>退出登录</span>
        </a-menu-item>
      </a-menu>
    </template>
  </a-dropdown>
</template>
<script setup >
import { computed, ref } from "@vue/reactivity";
import { onMounted } from "@vue/runtime-core";
import { useRouter } from "vue-router";
import { useUserStore } from "/@/store/modules/system/user";
import { clearAllCoolies } from "/@/utils/cookie-util";
import { localClear } from "/@/utils/local-util";
// 头像背景颜色
const AVATAR_BACKGROUND_COLOR_ARRAY = ["#f56a00", "#87d068", "#1890ff"];

// ----------------------- 以下是字段定义 emits props ---------------------
const avatarName = ref("");
const router = useRouter();
// ----------------------- 以下是计算属性 watch监听 ------------------------
const userInfo = computed(() => useUserStore().getUserInfo);

// ----------------------- 以下是方法 ------------------------------------
const onLogout = () => {
  localClear();
  clearAllCoolies();
  useUserStore().logout();
  router.push({ name: "Login" });
};

function updateAvatar() {
  const name = useUserStore().getUserInfo.actualName;
  if (name) {
    avatarName.value = name.substr(0, 1);
    const avatar = document.getElementById("smartAdminAvatar");
    if (avatar) {
      avatar.style.backgroundColor =
        AVATAR_BACKGROUND_COLOR_ARRAY[hashcode(avatarName.value) % 3];
    }
  }
}

function hashcode(str) {
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
<style lang="less" scoped>
.header-trigger {
  height: @header-user-height;
  line-height: @header-user-height;

  .avatar {
    vertical-align: middle;
  }

  .name {
    margin-left: 5px;
    font-weight: 500;
    vertical-align: middle;
  }
}
</style>
