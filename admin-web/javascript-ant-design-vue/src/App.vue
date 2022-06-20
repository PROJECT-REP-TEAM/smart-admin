<!--
 * @Description: App
 * @Author: zhuoda
 * @Date: 2021-08-03
 * @LastEditTime: 2022-06-17
 * @LastEditors: zhuoda
-->
<template>
  <a-config-provider :locale="zh_CN">
    <!---全局loading，常用于表单提交--->
    <a-spin :spinning="spinning" size="large">
      <RouterView />
    </a-spin>
  </a-config-provider>
</template>

<script setup>
import zh_CN from "ant-design-vue/lib/locale-provider/zh_CN";
import { onMounted, computed } from "vue";
import { loginApi } from "/@/api/system/login/login-api";
import { useUserStore } from "./store/modules/system/user";
import { useAppConfigStore } from "/@/store/modules/system/app-config";
import { useSpinStore } from "/@/store/modules/system/spin";
import { buildRoutes } from "/@/router/index";
import { useRouter, useRoute } from "vue-router";
const route = useRoute();

let spinStore = useSpinStore();
const spinning = computed(() => spinStore.loading);

async function getLoginInfo() {
  spinStore.show();
  try {
    let token = useUserStore().getToken;
    if (!token) return;
    //构建系统的路由
    buildRoutes();
    //获取登录用户信息
    const res = await loginApi.getLoginInfo();
    //更新用户信息到pinia
    useUserStore().setUserLoginInfo(res.data);
    //构建系统的路由
    // buildRoutes();

    // useRouter().go(1);
  } catch (e) {
  } finally {
    spinStore.hide();
  }
}

// 更新屏幕宽度，以判断是否为isMobile模式，进行适配
let appConfigStore = useAppConfigStore();
function triggerReSize() {
  appConfigStore.setCurrentScreenWidth(document.body.clientWidth);
}
onMounted(() => {
  //获取登录信息
  // getLoginInfo();
  //更新屏幕宽度
  triggerReSize();
});

window.onresize = () => {
  triggerReSize();
};
</script>
