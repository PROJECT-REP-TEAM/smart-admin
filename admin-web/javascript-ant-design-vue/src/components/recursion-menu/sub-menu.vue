<!--
 * @Author: zhuoda
 * @Date: 2021-08-03 10:27:11
 * @LastEditTime: 2022-06-02
 * @LastEditors: zhuoda
 * @Description: 
 * @FilePath: /smart-admin/src/components/recursion-menu/sub-menu.vue
-->
<template>
  <a-sub-menu :key="menuInfo.name">
    <template #icon>
      <component :is="$antIcons[menuInfo.meta.icon]" />
    </template>
    <template #title>{{ menuInfo.meta.title }}</template>
    <template v-for="item in menuInfo.children" :key="item.name">
      <template v-if="!item.meta.hideInMenu">
        <template v-if="!item.children">
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
  </a-sub-menu>
</template>
<script>
import { defineComponent } from "vue";

export default defineComponent({
  name: "SubMenu",
  components: {},
  props: {
    menuInfo: {
      type: Object,
      default: () => ({}),
    },
  },
  setup(prop, context) {
    const turnToPage = (route) => {
      context.emit("turnToPage", route);
    };
    return {
      turnToPage,
    };
  },
});
</script>
