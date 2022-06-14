<template>
  <!--
    标签页，共两部分：
    1、标签
    2、标签操作区
  -->
  <a-row style="border-bottom: 1px solid #eeeeee; position: relative">
    <a-dropdown :trigger="['contextmenu']">
      <div class="smart-page-tag">
        <a-tabs
          :tab-position="mode"
          v-model:activeKey="selectedKey"
          size="small"
          @tabClick="selectTab"
        >
          <a-tab-pane v-for="item in tagNav" :key="item.menuName">
            <template #tab>
              <span>
                {{ item.menuTitle }}
                <close-outlined
                  @click.stop="closeTag(item, false)"
                  v-if="item.menuName != homePageName"
                  class="smart-page-tag-close"
                />
              </span>
            </template>
          </a-tab-pane>
        </a-tabs>
      </div>
      <template #overlay>
        <a-menu>
          <a-menu-item @click="closeByMenu(false)">关闭其他</a-menu-item>
          <a-menu-item @click="closeByMenu(true)">关闭所有</a-menu-item>
        </a-menu>
      </template>
    </a-dropdown>

    <a-dropdown>
      <!--标签页操作区-->
      <div class="smart-page-tag-operate">
        <div class="smart-page-tag-operate-icon">
          <AppstoreOutlined />
        </div>
      </div>
      <template #overlay>
        <a-menu>
          <a-menu-item @click="closeByMenu(false)">关闭其他</a-menu-item>
          <a-menu-item @click="closeByMenu(true)">关闭所有</a-menu-item>
        </a-menu>
      </template>
    </a-dropdown>
  </a-row>
</template>

<script>
import { computed, defineComponent, ref } from "vue";
import { CloseOutlined, AppstoreOutlined } from "@ant-design/icons-vue";
import { useAppConfigStore } from "/@/store/modules/system/app-config";
import { useUserStore } from "/@/store/modules/system/user";
import { useRoute, useRouter } from "vue-router";
import { appDefaultConfig } from "/@/config/app-config";

export default defineComponent({
  name: "SmartPageTag",
  components: {
    CloseOutlined,
    AppstoreOutlined,
  },
  setup() {
    const router = useRouter();
    const appConfigStore = useAppConfigStore();
    const pageList = new Array();
    const mode = ref("top");
    const homePageName = appDefaultConfig.homePageName;
    const callback = (val) => {
      console.log(val);
    };
    const tagNav = computed(() => useUserStore().getTagNav || []);
    const selectedKey = computed(() => {
      let currentRoute = useRoute();
      return currentRoute.name;
    });
    const selectTab = (name) => {
      // 寻找tag
      let tag = tagNav.value.find((e) => e.menuName == name);
      if (!tag) {
        router.push({ name: appDefaultConfig.homePageName });
        return;
      }
      router.push({ name, query: tag.menuQuery });
    };
    const closeByMenu = (closeAll) => {
      let find = tagNav.value.find((e) => e.menuName == selectedKey.value);
      if (!find || closeAll) {
        closeTag(null, true);
      } else {
        closeTag(find, true);
      }
    };
    const closeTag = (item, closeAll) => {
      // 关闭单个tag
      if (item && !closeAll) {
        let goName = appDefaultConfig.homePageName;
        let goQuery = undefined;
        if (
          item.fromMenuName &&
          tagNav.value.some((e) => e.menuName == item.fromMenuName)
        ) {
          goName = item.fromMenuName;
          goQuery = item.fromMenuQuery;
        } else {
          // 查询左侧tag
          let index = tagNav.value.findIndex((e) => e.menuName == item.menuName);
          if (index > 0) {
            // 查询左侧tag
            let leftTagNav = tagNav.value[index - 1];
            goName = leftTagNav.menuName;
            goQuery = leftTagNav.menuQuery;
          }
        }
        router.push({ name: goName, query: goQuery });
      } else if (!item && closeAll) {
        // 关闭所有tag
        router.push({ name: appDefaultConfig.homePageName });
      }
      // 关闭其他tag不做处理 直接调用closeTagNav
      useUserStore().closeTagNav(item ? item.menuName : null, closeAll);
    };
    return {
      closeByMenu,
      closeTag,
      selectTab,
      tagNav,
      selectedKey,
      mode,
      callback,
      pageList,
      homePageName,
      layout: computed(() => appConfigStore.layout),
      multiPageTagFlag: computed(() => appConfigStore.multiPageTagFlag),
    };
  },
});
</script>

<style scoped lang="less">
@smart-page-tag-operate-width: 40px;

.smart-page-tag-operate {
  width: @smart-page-tag-operate-width;
  height: @smart-page-tag-operate-width;
  background-color: #ffffff;
  font-size: 17px;
  text-align: center;
  vertical-align: middle;
  line-height: @smart-page-tag-operate-width;
  padding-right: 10px;
  cursor: pointer;
  color: #606266;

  .smart-page-tag-operate-icon {
    width: 20px;
    height: 20px;
    transition: all 1s;
    transform-origin: 10px 20px;
  }

  .smart-page-tag-operate-icon:hover {
    width: 20px;
    height: 20px;
    transform: rotate(360deg);
  }
}

.smart-page-tag-operate:hover {
  color: @primary-color;
}

.smart-page-tag {
  position: relative;
  box-sizing: border-box;
  display: flex;
  align-content: center;
  align-items: center;
  justify-content: space-between;
  min-height: @page-tag-height;
  padding-right: 20px;
  padding-left: 20px;
  user-select: none;
  background: #fff;
  width: calc(100% - @smart-page-tag-operate-width);

  .smart-page-tag-tabs {
    width: calc(100% - 100px);
    height: 26px;
    background-color: red;
    margin: 2px;
  }

  .smart-page-tag-close {
    margin-left: 5px;
    font-size: 10px;
    color: #8c8c8c;
  }

  /**  覆盖 ant design vue的 tabs 样式，变小一点 **/

  :deep(.ant-tabs-nav) {
    margin: 0;
    padding: 0 0 2px 0;
  }

  :deep(.ant-tabs-nav::before) {
    border-bottom: 1px solid #ffffff;
  }

  :deep(.ant-tabs-small > .ant-tabs-nav .ant-tabs-tab) {
    padding: 5px 8px 3px 10px;
  }

  :deep(.ant-tabs-tab-active) {
    background-color: #e8f4ff;
    .smart-page-tag-close {
      color: @primary-color;
    }
  }
  :deep(.ant-tabs-nav .ant-tabs-tab:hover) {
    background-color: #e8f4ff;
    .smart-page-tag-close {
      color: @primary-color;
    }
  }

  // .ant-tabs-line {
  //   ::v-deep(.ant-tabs-bar) {
  //     margin-bottom: 0px;
  //     border-bottom: 0;
  //   }

  //   ::v-deep(.ant-tabs-nav .ant-tabs-tab) {
  //     padding: 5px 18px;
  //     margin: 0px 5px;
  //   }

  //   ::v-deep(.ant-tabs-nav .ant-tabs-tab:hover) {
  //     background-color: #e8f4ff;

  //     .smart-page-tag-close {
  //       color: @primary-color;
  //     }
  //   }

  //   ::v-deep(.ant-tabs-nav .ant-tabs-tab-active) {
  //     background-color: #e8f4ff;

  //     .smart-page-tag-close {
  //       color: @primary-color;
  //     }
  //   }
  // }
}
</style>
