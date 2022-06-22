<!--
 * @Description: App
 * @Author: zhuoda
 * @Date: 2021-08-03
 * @LastEditTime: 2022-06-22
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
import { computed } from "vue";
import { useRoute } from "vue-router";
import { useSpinStore } from "/@/store/modules/system/spin";

const route = useRoute();

let spinStore = useSpinStore();
const spinning = computed(() => spinStore.loading);
</script>
