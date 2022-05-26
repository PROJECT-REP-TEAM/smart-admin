

<template>
  <RouterView v-loading.fullscreen.lock="spinning" />
</template>
<script setup>
  import { computed, onMounted } from 'vue';
import { useSpinStore } from "/@/store/modules/spin";
import { useUserStore } from '/@/store/modules/user';
import { loginApi } from '/@/api/system/login';
import { useAppConfigStore } from '/@/store/modules/app-config';

let spinStore = useSpinStore();
const spinning = computed(() => spinStore.loading);

async function getLoginInfo() {
  let token = useUserStore().getToken;
  if (!token) return;
  const res = await loginApi.getLoginInfo();
  useUserStore().setMenuTree(res.data);
  useUserStore().setPointsList(res.data);
}

// 更新屏幕宽度，以判断是否为isMobile模式，进行适配
let appConfigStore = useAppConfigStore();
function triggerReSize() {
  appConfigStore.setCurrentScreenWidth(document.body.clientWidth);
}
onMounted(() => {
  //获取登录信息
  getLoginInfo();
  //更新屏幕宽度
  triggerReSize();
});

window.onresize = () => {
  triggerReSize();
};

</script>
<style>
</style>
