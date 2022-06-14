<template>
  <a-dropdown class="header-trigger">
    <div @click.prevent>
        <GlobalOutlined/>
        中文
    </div>
    <template #overlay>
      <a-menu>
        <a-menu-item>
          <a href="javascript:;">英文</a>
        </a-menu-item>
        <a-menu-item>
          <a href="javascript:;">繁体中文</a>
        </a-menu-item>
        <a-menu-item>
          <a href="javascript:;">日文</a>
        </a-menu-item>
      </a-menu>
    </template>
  </a-dropdown>

</template>
<script >
import {defineComponent} from "vue";
import { GlobalOutlined } from '@ant-design/icons-vue';

export default defineComponent({
  name: "I18nSelector",
  components: {
    GlobalOutlined
  }
});
</script>
<style lang="less" scoped>
.header-trigger{
  height: @header-height;
  line-height: @header-height;
}
</style>