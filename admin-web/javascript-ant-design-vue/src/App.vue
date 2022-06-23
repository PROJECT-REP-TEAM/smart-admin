<!--
 * @Description: App
 * @Author: zhuoda
 * @Date: 2021-08-03
 * @LastEditTime: 2022-06-23
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
  import dayjs from 'dayjs';
  import { computed } from 'vue';
  import { messages } from '/@/i18n/index';
  import { useAppConfigStore } from '/@/store/modules/system/app-config';
  import { useSpinStore } from '/@/store/modules/system/spin';

  const antdLocale = computed(() => messages[useAppConfigStore().language].antdLocale);
  const dayjsLocale = computed(() => messages[useAppConfigStore().language].dayjsLocale);
  dayjs.locale(dayjsLocale);

  let spinStore = useSpinStore();
  const spinning = computed(() => spinStore.loading);
</script>
