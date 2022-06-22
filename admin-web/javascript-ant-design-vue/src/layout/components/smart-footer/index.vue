<!--
 * @Description: 底部信息
 * @Author: zhuoda
 * @Date: 2021-08-03
 * @LastEditTime: 2022-06-22
 * @LastEditors: zhuoda
-->
<template>
  <div class="footer" v-show="footerFlag">
    <div class="version">SmartAdmin V2.x, 发布于 2022.07 (河南 · 洛阳)</div>
    <div class="copyright">
      <a target="_blank" href="https://www.1024lab.net"
        >@copyright 1024创新实验室 2015-{{ currentYear }}
      </a>
    </div>
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
.footer {
  text-align: center;
  color: rgba(0, 0, 0, 0.45);

  .copyright {
    font-size: 14px;
    a {
      color: rgba(0, 0, 0, 0.45);
    }
    a:hover {
      color: rgba(0, 0, 0, 0.85);
    }
  }
}
</style>
