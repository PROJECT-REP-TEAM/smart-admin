<!--
 * @Description: App
 * @Author: zhuoda
 * @Date: 2021-08-03
 * @LastEditTime: 2022-06-22
 * @LastEditors: zhuoda
-->
<template>
  <a-config-provider :locale="antdLocale">
    <!---全局loading，常用于表单提交--->
    <a-spin :spinning="spinning" size="large">
      <RouterView />
    </a-spin>
  </a-config-provider>
</template>

<script setup>
import { computed } from "vue";
import { useRoute } from "vue-router";
import { useSpinStore } from "/@/store/modules/system/spin";
import { useAppConfigStore } from "/@/store/modules/system/app-config";
import { messages } from "/@/i18n/index";
import dayjs from "dayjs";

const route = useRoute();

const antdLocale = computed(() => messages[useAppConfigStore().language].antdLocale);
const dayjsLocale = computed(() => messages[useAppConfigStore().language].dayjsLocale);
dayjs.locale(dayjsLocale);

let spinStore = useSpinStore();
const spinning = computed(() => spinStore.loading);
</script>
