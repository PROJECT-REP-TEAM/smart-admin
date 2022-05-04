<template>
  <!--
    标签页，共两部分：
    1、标签
    2、标签操作区
  -->
  <el-row style="border-bottom: 1px solid #eeeeee; position: relative">
    <div class="smart-page-tag">
      <el-tabs class="smart-page-tag-tabs" tab-position="top" v-model="selectedKey" @tab-click="selectTab">
        <el-tab-pane v-for="item in tagNav" :name="item.menuName">
          <template #label>
            {{ item.menuTitle }}
            <el-icon
              @click.stop="closeTag(item, false)"
              v-if="item.menuName != homePageName"
              class="smart-page-tag-close"
              ><close />
            </el-icon>
          </template>
        </el-tab-pane>
      </el-tabs>
    </div>
    <div class="smart-page-tag-operate">
      <el-dropdown size="small" placement="bottom-end">
        <div class="smart-page-tag-operate-icon">
          <grid />
        </div>
        <template #dropdown>
          <el-dropdown-menu>
            <el-dropdown-item @click="closeByMenu(false)">关闭其他</el-dropdown-item>
            <el-dropdown-item @click="closeByMenu(true)">关闭所有</el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>
    </div>
  </el-row>
</template>

<script setup>
import { computed, ref } from "vue";
import { useUserStore } from "/@/store/modules/user";
import { useRoute, useRouter } from "vue-router";
import { appDefaultConfig } from "/@/config/app-config";

const router = useRouter();
const mode = ref("top");
const homePageName = appDefaultConfig.homePageName;
const tagNav = computed(() => useUserStore().getTagNav || []);
const selectedKey = computed(() => {
  let currentRoute = useRoute();
  return currentRoute.name;
});
function selectTab(val) {
  let name = val.props.name;
  // 寻找tag
  let tag = tagNav.value.find((e) => e.menuName == name);
  if (!tag) {
    router.push({ name: appDefaultConfig.homePageName });
    return;
  }
  router.push({ name, query: tag.menuQuery });
}
function closeByMenu(closeAll) {
  let find = tagNav.value.find((e) => e.menuName == selectedKey.value);
  if (!find || closeAll) {
    closeTag(null, true);
  } else {
    closeTag(find, true);
  }
}
function closeTag(item, closeAll) {
  // 关闭单个tag
  if (item && !closeAll) {
    let goName = appDefaultConfig.homePageName;
    let goQuery = undefined;
    if (item.fromMenuName && tagNav.value.some((e) => e.menuName == item.fromMenuName)) {
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
}
</script>
<style scoped lang="scss">
$smart-page-tag-operate-width: 40px;

.smart-page-tag-operate {
  z-index: 100;
  width: $smart-page-tag-operate-width;
  height: $smart-page-tag-operate-width;
  background-color: #ffffff;
  font-size: 17px;
  padding-right: 10px;
  cursor: pointer;
  color: #606266;
  display: flex;
  align-items: center;
  justify-content: center;

  .smart-page-tag-operate-icon {
    width: 20px;
    height: 20px;
    transition: all 1s;
    transform-origin: 10px 10px;
  }

  .smart-page-tag-operate-icon:hover {
    width: 20px;
    height: 20px;
    transform: rotate(360deg);
  }
}

.smart-page-tag-operate:hover {
  // color: $primary-color;
}

.smart-page-tag {
  position: relative;
  box-sizing: border-box;
  display: flex;
  align-content: center;
  align-items: center;
  justify-content: space-between;
  height: $page-tag-height;
  padding-right: 20px;
  padding-left: 20px;
  user-select: none;
  background: #fff;
  width: calc(100% - $smart-page-tag-operate-width);

  .smart-page-tag-tabs {
    width: 100%;
  }

  .smart-page-tag-close {
    margin-left: 5px;
    font-size: 10px;
    color: #8c8c8c;
  }
}
:deep(.el-tabs__header) {
  margin-bottom: 0;
}

:deep(.el-tabs__item) {
  padding: 5px 18px;
  margin: 0px 5px;
  // background-color: #e8f4ff;
}
</style>
