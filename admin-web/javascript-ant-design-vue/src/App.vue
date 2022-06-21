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
      <RouterView/>
    </a-spin>
  </a-config-provider>
</template>

<script setup>
import zh_CN from "ant-design-vue/lib/locale-provider/zh_CN";
import {computed, onMounted} from "vue";
import {useAppConfigStore} from "/@/store/modules/system/app-config";
import {useSpinStore} from "/@/store/modules/system/spin";
import {useRoute} from "vue-router";

const route = useRoute();

let spinStore = useSpinStore();
const spinning = computed(() => spinStore.loading);

// 更新屏幕宽度，以判断是否为isMobile模式，进行适配
let appConfigStore = useAppConfigStore();

function triggerReSize() {
  appConfigStore.setCurrentScreenWidth(document.body.clientWidth);
}

onMounted(() => {
  //更新屏幕宽度
  triggerReSize();
});

window.onresize = () => {
  triggerReSize();
};
</script>
