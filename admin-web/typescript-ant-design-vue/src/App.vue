<!--
 * @Description: App
 * @Author: zhuoda
 * @Date: 2021-08-03
 * @LastEditTime: 2022-05-16 21:19:09
 * @LastEditors: LiHaiFan
-->
<template>
  <a-config-provider :locale="zh_CN">
    <!---全局loading，常用于表单提交--->
    <a-spin :spinning="spinning" size="large">
      <RouterView />
    </a-spin>
  </a-config-provider>
</template>

<script setup lang="ts">
import zh_CN from "ant-design-vue/lib/locale-provider/zh_CN";
import { onMounted, computed } from "vue";
import { loginApi } from "./api/system/login/login";
import { useUserStore } from "./store/modules/system/user";
import { useAppConfigStore } from "/@/store/modules/system/app-config";
import { useSpinStore } from "/@/store/modules/system/spin";

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
