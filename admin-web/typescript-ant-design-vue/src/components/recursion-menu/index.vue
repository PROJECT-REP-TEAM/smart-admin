<!--
 * @Author: zhuoda
 * @Date: 2021-08-17 19:09:40
 * @LastEditTime: 2021-08-27
 * @LastEditors: zhuoda
 * @Description: 
 * @FilePath: /smart-admin/src/components/recursion-menu/index.vue
-->
<template>
  <a-menu
    :selectedKeys="selectedKeys"
    :openKeys="openKeys"
    mode="inline"
    theme="dark"
    :inline-collapsed="collapsed"
  >
    <template v-for="item in menuTree" :key="item.name">
      <template v-if="!item.meta.hideInMenu">
        <template v-if="$lodash.isEmpty(item.children)">
          <a-menu-item :key="item.name" @click="turnToPage(item)">
            <template #icon>
              <component :is="$antIcons[item.meta.icon]" />
            </template>
            {{ item.meta.title }}
          </a-menu-item>
        </template>
        <template v-else>
          <sub-menu :menu-info="item" :key="item.name" @turnToPage="turnToPage" />
        </template>
      </template>
    </template>
  </a-menu>
</template>
<script lang="ts">
import { defineComponent, ref, computed } from "vue";
import SubMenu from "./sub-menu.vue";
import { router } from "/@/router/index";
import { MENU_TYPE_ENUM } from "/@/constants/system/menu/menu-enum";
import { RouteRecord, useRoute } from "vue-router";

export default defineComponent({
  name: "RecursionMenu",
  components: {
    SubMenu,
  },
  props: {
    menuList: {
      type: Array,
      default: () => {
        return [];
      },
    },
    collapsed: {
      type: Boolean,
      default: false,
    },
  },
  setup(prop, context) {
    const collapsed = ref<boolean>(false);
    let currentRoute = useRoute();
    const toggleCollapsed = () => {
      collapsed.value = !collapsed.value;
    };

    // 菜单树
    let routes = router.getRoutes();
    const menuTree = computed(() => {
      return routes.filter((e) => e.meta.menuType == MENU_TYPE_ENUM.CATALOG.value);
    });

    const selectedKeys = computed(() => {
      return [currentRoute.name];
    });

    const openKeys = computed(() => {
      return (currentRoute.meta.parentMenuList || []).map(
        (e: Record<string, string>) => e.name
      );
    });

    // 页面跳转
    const turnToPage = (route: RouteRecord | string) => {
      console.log(route);
      let { name, params, query } = {};
      if (typeof route === "string") {
        name = route;
      } else {
        name = route.name;
        params = route.params;
        query = route.query;
      }
      if (name.indexOf("isTurnByHref_") > -1) {
        window.open(name.split("_")[1]);
        return;
      }
      router.push({
        name,
        params,
        query,
      });
    };

    return {
      selectedKeys,
      openKeys,
      turnToPage,
      toggleCollapsed,
      menuTree,
    };
  },
  components: {
    SubMenu,
  },
});
</script>
