<template>
  <!--
    左侧菜单分为两部分：
    1、顶部logo区域，包含 logo和名称
    2、下方菜单区域
   -->

  <!-- 顶部logo区域 -->
  <div :class="['logo', theme]">
      <img src="/@/assets/img/logo.png"/>
      <h1>{{ projectName }}</h1>
  </div>

  <!-- 下方菜单区域： 这里使用一个递归菜单解决 -->
  <recursion-menu  :collapsed="collapsed" />

</template>

<script lang="ts">
import RecursionMenu from "/@/components/recursion-menu/index.vue";
import {computed, defineComponent} from "vue";
import {useAppConfigStore} from "/@/store/modules/system/app-config";
import {useProjectConfigStore} from "/@/store/modules/system/project-config";


export default defineComponent({
  name: "SideMenu",
  components: {RecursionMenu},
  props: {
    collapsible: {
      type: Boolean,
      required: false,
      default: false
    },
    collapsed: {
      type: Boolean,
      required: false,
      default: false
    },
    menuData: {
      type: Array,
      required: true
    },
    theme: {
      type: String,
      required: false,
      default: "dark"
    }
  },
  setup(props, {emit}) {
    let appConfigStore = useAppConfigStore();
    let projectConfigStore = useProjectConfigStore();

    const onSelect = (obj) => {
      emit("menuSelect", obj);
    };

    return {
      onSelect,
      isMobile: computed(() => appConfigStore.isMobile),
      projectName: computed(() => projectConfigStore.projectName)
    };
  }
});
</script>

<style lang="less" scoped>
@import "index";
</style>
