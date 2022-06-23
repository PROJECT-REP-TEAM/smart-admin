<!--
 * @Description: 底部信息
 * @Author: zhuoda
 * @Date: 2021-08-03
 * @LastEditTime: 2022-06-23
 * @LastEditors: zhuoda
-->
<template>
  <div class="version" v-show="footerFlag">
    <a target="_blank" href="https://www.1024lab.net">
      SmartAdmin V2.x, 发布于 2022.07 (河南 · 洛阳) @copyright 1024创新实验室 2015-{{
        currentYear
      }}
    </a>
  </div>
</template>
<script setup>
import dayjs from "dayjs";
import { computed } from "@vue/reactivity";
import { useAppConfigStore } from "/@/store/modules/system/app-config";

const footerFlag = computed(() => useAppConfigStore().$state.footerFlag);

const currentYear = dayjs().year();
</script>

<style lang="less" scoped>
.version {
  font-size: 14px;
  color: rgba(0, 0, 0, 0.45);
  a {
    color: rgba(0, 0, 0, 0.45);
  }
  a:hover {
    color: @primary-color;
  }
}
</style>
