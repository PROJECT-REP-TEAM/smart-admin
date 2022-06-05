<!--
 * @Author: LiHaiFan
 * @Date: 2022-06-05 15:26:04
 * @LastEditTime: 2022-06-05 18:13:47
 * @LastEditors: LiHaiFan
 * @Description: 
 * @FilePath: \typescript-element-plus-vue\src\layout\components\smart-footer\index.vue
-->

<template>
  <div class="footer">
    <div class="version">{{ projectName }} V{{ version }}, 发布于 {{ buildTime }}</div>
    <div class="copyright">
      <a target="_blank" :href="copyrightUrl">{{ copyright }} </a>
    </div>
  </div>
</template>


<script lang="ts" setup>
import { computed } from "vue";
import { useProjectConfigStore } from "/@/store/modules/system/project-config";

const projectConfigStore = useProjectConfigStore();
const projectName = computed(() => projectConfigStore.projectName);
const copyright = computed(() => projectConfigStore.copyright);
const copyrightUrl = computed(() => projectConfigStore.copyrightUrl);
const version = computed(() => projectConfigStore.version);
const buildTime = computed(() => projectConfigStore.buildTime);
</script>
<style lang="scss" scoped>
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
