<template>
  <a-layout-header :class="[headerTheme, 'admin-header']">
    <div :class="['admin-header-wide', layout, 'fluid']">
      <router-link
        v-if="isMobile || layout === 'head'"
        to="/"
        :class="['logo', isMobile ? null : 'pc', headerTheme]"
      >
        <img width="32" src="/@/assets/img/logo.png" />
        <h1 v-if="!isMobile">{{ projectName }}</h1>
      </router-link>
      <a-divider v-if="isMobile" type="vertical" />
      <a-icon
        v-if="layout !== 'head'"
        class="trigger"
        :type="collapsed ? 'menu-unfold' : 'menu-fold'"
        @click="toggleCollapse"
      />
      <div
        v-if="layout !== 'side' && !isMobile"
        class="admin-header-menu"
        :style="`width: ${menuWidth};`"
      >
        <i-menu
          class="head-menu"
          :theme="headerTheme"
          mode="horizontal"
          :options="menuData"
          @select="onSelectMenu"
        />
      </div>
      <div :class="['admin-header-right', headerTheme]">
        <header-search class="header-item" @active="(val) => (searchActive = val)" />
        <a-tooltip class="header-item" title="帮助文档" placement="bottom">
          <a href="https://iczer.gitee.io/vue-antd-admin-docs/" target="_blank">
            <a-icon type="question-circle-o" />
          </a>
        </a-tooltip>
        <header-notice class="header-item" />
        <header-avatar class="header-item" />
        <a-dropdown class="lang header-item">
          <div>
            <a-icon type="global" />
            {{ langAlias }}
          </div>
          <a-menu @click="(val) => setLang(val.value)" :selected-keys="[lang]">
            <a-menu-item v-for="lang in i18nList" :key="lang.value"
              >{{ lang.text.toLowerCase() + " " + lang.text }}
            </a-menu-item>
          </a-menu>
        </a-dropdown>
      </div>
    </div>
  </a-layout-header>
</template>

<script >
import HeaderSearch from "./components/header-search.vue";
import HeaderNotice from "./components/header-notice.vue";
import HeaderAvatar from "./components/header-avatar.vue";

import { computed, defineComponent, ref } from "vue";
import { useAppConfigStore } from "/@/store/modules/system/app-config";
import { useProjectConfigStore } from "/@/store/modules/system/project-config";
import { i18nList } from "/@/i18n";

export default defineComponent({
  name: "SmartHeader",
  components: {
    HeaderSearch,
    HeaderAvatar,
    HeaderNotice,
  },
  props: {
    // 折叠菜单
    collapsed: {
      type: Boolean,
      default: false,
    },
    // 折叠菜单
    menuData: {
      type: Array,
      default: () => {
        return new Array();
      },
    },
  },
  setup: function (props, { emit }) {
    const appConfigStore = useAppConfigStore();
    const projectConfigStore = useProjectConfigStore();
    //搜索状态
    const searchActive = ref(false);

    const headerTheme = computed(() => {
      if (
        appConfigStore.layout == "side" &&
        appConfigStore.theme == "dark" &&
        !appConfigStore.isMobile
      ) {
        return "light";
      }
      return appConfigStore.theme;
    });

    const onSelectMenu = (selectMenu: string) => {
      emit("selectMenu", selectMenu);
    };

    const onToggleCollapse = () => {
      emit("toggleCollapse");
    };

    const onSelectLanguage = (languageValue: string) => {
      appConfigStore.setLanguage(languageValue);
    };

    // 菜单宽度
    const menuWidth = computed(() => {
      const headWidth =
        (appConfigStore.layout as string) === "head" ? "100% - 188px" : "100%";
      const extraWidth = searchActive.value ? "600px" : "400px";
      return `calc(${headWidth} - ${extraWidth})`;
    });

    return {
      i18nList,
      // 头部主题
      headerTheme,
      // 侧边栏宽度
      menuWidth,
      // 选中事件
      onSelectMenu,
      // 选择语言
      onSelectLanguage,
      // 隐藏菜单
      onToggleCollapse,
      // 是否是手机端
      isMobile: computed(() => appConfigStore.isMobile),
      // 布局
      layout: computed(() => appConfigStore.layout),
      // 项目名字
      projectName: computed(() => projectConfigStore.projectName),
    };
  },
});
</script>

<style lang="less" scoped>
@import "index";
</style>
