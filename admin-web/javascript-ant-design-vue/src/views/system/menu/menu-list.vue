<template>
  <a-form class="smart-query-form">
    <a-row class="smart-query-form-row">
      <a-form-item label="关键字" class="smart-query-form-item">
        <a-input
          style="width: 300px"
          v-model:value="queryForm.keywords"
          placeholder="菜单名称/路由地址/组件路径/权限字符串"
        />
      </a-form-item>

      <a-form-item label="类型" class="smart-query-form-item">
        <smart-enum-select
          :width="100"
          v-model:value="queryForm.menuType"
          placeholder="请选择类型"
          enum-name="MENU_TYPE_ENUM"
          @change="disabledFlagChange"
        />
      </a-form-item>

      <a-form-item label="启用" class="smart-query-form-item">
        <smart-enum-select
          :width="80"
          enum-name="FLAG_NUMBER_ENUM"
          v-model:value="queryForm.disabledFlag"
          @change="disabledFlagChange"
        />
      </a-form-item>

      <a-form-item class="smart-query-form-item smart-margin-left10">
        <a-button type="primary" @click="query">
          <template #icon>
            <ReloadOutlined />
          </template>
          查询
        </a-button>

        <a-button @click="resetQuery">
          <template #icon>
            <SearchOutlined />
          </template>
          重置
        </a-button>
        <a-button
          class="smart-margin-left20"
          @click="moreQueryConditionFlag = !moreQueryConditionFlag"
        >
          <template #icon>
            <MoreOutlined />
          </template>
          {{ moreQueryConditionFlag ? "收起" : "展开" }}
        </a-button>
      </a-form-item>
    </a-row>

    <a-row class="smart-query-form-row" v-show="moreQueryConditionFlag">
      <a-form-item label="外链" class="smart-query-form-item">
        <smart-enum-select
          :width="80"
          enum-name="FLAG_NUMBER_ENUM"
          v-model:value="queryForm.frameFlag"
          @change="disabledFlagChange"
        />
      </a-form-item>

      <a-form-item label="缓存" class="smart-query-form-item">
        <smart-enum-select
          :width="80"
          enum-name="FLAG_NUMBER_ENUM"
          v-model:value="queryForm.cacheFlag"
          @change="disabledFlagChange"
        />
      </a-form-item>

      <a-form-item label="显示" class="smart-query-form-item">
        <smart-enum-select
          :width="80"
          enum-name="FLAG_NUMBER_ENUM"
          v-model:value="queryForm.visibleFlag"
          @change="disabledFlagChange"
        />
      </a-form-item>
    </a-row>
  </a-form>

  <a-card size="small" :bordered="false" :hoverable="true">
    <a-row class="smart-table-btn-block">
      <div class="smart-table-operate-block">
        <a-button type="primary" size="small" @click="showDrawer">
          <template #icon>
            <PlusOutlined />
          </template>
          添加菜单
        </a-button>

        <a-button
          type="primary"
          danger
          size="small"
          @click="batchDelete"
          :disabled="!hasSelected"
        >
          <template #icon>
            <DeleteOutlined />
          </template>
          批量删除
        </a-button>
      </div>
      <div class="smart-table-setting-block"></div>
    </a-row>

    <a-table
      :row-selection="{ selectedRowKeys: selectedRowKeys, onChange: onSelectChange }"
      size="small"
      :defaultExpandAllRows="true"
      :dataSource="tableData"
      bordered
      :columns="columns"
      :loading="tableLoading"
      rowKey="menuId"
      :pagination="false"
    >
      <template #type="{ text }">
        <span>{{ $smartEnumPlugin.getDescByValue("MENU_TYPE_ENUM", text) }}</span>
      </template>

      <template #frameFlag="{ text }">
        <span>{{ $smartEnumPlugin.getDescByValue("FLAG_NUMBER_ENUM", text) }}</span>
      </template>

      <template #cacheFlag="{ text }">
        <span>{{ $smartEnumPlugin.getDescByValue("FLAG_NUMBER_ENUM", text) }}</span>
      </template>

      <template #disabledFlag="{ text }">
        <span>{{ $smartEnumPlugin.getDescByValue("FLAG_NUMBER_ENUM", text) }}</span>
      </template>

      <template #icon="{ text }">
        <component :is="$antIcons[text]" />
      </template>

      <template #operate="{ text, record }">
        <a-button type="link" size="small" @click="showDrawer(record)">编辑</a-button>
        <a-button danger type="link" @click="singleDelete(record)">删除</a-button>
      </template>
    </a-table>
  </a-card>

  <MenuOperateModal ref="menuOperateModal" @reloadList="query" />
</template>
<script setup>
import { reactive, ref, onMounted, computed, createVNode } from "vue";
import SmartEnumSelect from "/@/components/smart-enum-select/index.vue";
import { menuApi } from "/@/api/system/menu/menu-api";
import MenuOperateModal from "./components/menu-operate-modal.vue";
import { message, Modal } from "ant-design-vue";
import { ExclamationCircleOutlined } from "@ant-design/icons-vue";
import _ from "lodash";
import { ColumnProps } from "ant-design-vue/es/table/interface";
import { SmartLoading } from "/@/components/smart-loading";
import { columns } from "./menu-list-table-columns";

const menuOperateModal = ref();

function filterQueryForm(menuList, queryForm) {
  if (!menuList || menuList.length === 0) {
    return [];
  }

  let filterResult = [];

  for (const menu of menuList) {
    if (
      isMenuExistKeywords(menu, queryForm.keywords) &&
      isMenuExistMenuType(menu, queryForm.menuType) &&
      isMenuExistMenuFlag(menu, queryForm)
    ) {
      filterResult.push(menu);
    }
  }

  return filterResult;
}

function isMenuExistMenuFlag(menu, queryForm) {
  let frameFlagCondition = false;
  if (!_.isNil(queryForm.frameFlag)) {
    frameFlagCondition =
      !_.isNil(menu.frameFlag) && menu.frameFlag === (queryForm.frameFlag === 1);
  } else {
    frameFlagCondition = true;
  }

  let cacheFlagCondition = false;
  if (!_.isNil(queryForm.cacheFlag)) {
    cacheFlagCondition =
      !_.isNil(menu.cacheFlag) && menu.cacheFlag === (queryForm.cacheFlag === 1);
  } else {
    cacheFlagCondition = true;
  }

  let visibleFlagCondition = false;
  if (!_.isNil(queryForm.visibleFlag)) {
    visibleFlagCondition =
      !_.isNil(menu.visibleFlag) && menu.visibleFlag === (queryForm.visibleFlag === 1);
  } else {
    visibleFlagCondition = true;
  }

  let disabledFlagCondition = false;
  if (!_.isNil(queryForm.disabledFlag)) {
    disabledFlagCondition =
      !_.isNil(menu.disabledFlag) && menu.disabledFlag === (queryForm.disabledFlag === 1);
  } else {
    disabledFlagCondition = true;
  }

  return (
    frameFlagCondition &&
    cacheFlagCondition &&
    visibleFlagCondition &&
    disabledFlagCondition
  );
}

function isMenuExistMenuType(menu, menuType) {
  if (!menuType) {
    return true;
  }

  if (menu.menuType && menu.menuType === menuType) {
    return true;
  }
  return false;
}

function isMenuExistKeywords(menu, keywords) {
  if (!keywords) {
    return true;
  }

  if (menu.component && menu.component.indexOf(keywords) > -1) {
    return true;
  }

  if (menu.menuName && menu.menuName.indexOf(keywords) > -1) {
    return true;
  }
  if (menu.path && menu.path.indexOf(keywords) > -1) {
    return true;
  }
  if (menu.perms && menu.perms.indexOf(keywords) > -1) {
    return true;
  }
  return false;
}

function findTopMenuArray(menuList) {
  let topMenuList = [];
  const menuIdSet = new Set();
  for (const menu of menuList) {
    menuIdSet.add(menu.menuId);
  }

  for (const menu of menuList) {
    const parentId = menu.parentId;
    // 不存在父节点，则为顶级菜单
    if (!menuIdSet.has(parentId)) {
      topMenuList.push(menu);
    }
  }
  return topMenuList;
}

const queryFormState = {
  keywords: "",
  menuType: undefined,
  frameFlag: undefined,
  cacheFlag: undefined,
  visibleFlag: undefined,
  disabledFlag: undefined,
};
const queryForm = reactive({ ...queryFormState });
const moreQueryConditionFlag = ref < Boolean > false;
const tableLoading = ref < Boolean > false;

const tableData = ref([]);

function resetQuery() {
  Object.assign(queryForm, queryFormState);
  query();
}

async function query() {
  try {
    tableLoading.value = true;
    let responseModel = await menuApi.queryMenu();
    // 过滤搜索条件
    console.log("queryForm", queryForm);
    const filtedMenuList = filterQueryForm(responseModel.data, queryForm);
    //找到顶级菜单
    const topMenu = findTopMenuArray(filtedMenuList);
    recursiveMenuTree(filtedMenuList, topMenu);

    tableData.value = topMenu;
  } catch (e) {
    console.log(e);
  } finally {
    tableLoading.value = false;
  }
}

function recursiveMenuTree(menuList, parentArray) {
  for (const parent of parentArray) {
    const children = menuList.filter((e) => e.parentId === parent.menuId);
    if (children.length > 0) {
      parent.children = children;
      recursiveMenuTree(menuList, parent.children);
    }
  }
}

onMounted(query);

const disabledFlagChange = (value) => {
  console.log(1, value);
};

//-------------- 多选操作
const selectedRowKeys = ref([]);
let selectedRows = [];
const hasSelected = computed(() => selectedRowKeys.value.length > 0);
function onSelectChange(keyArray, selectRows) {
  selectedRowKeys.value = keyArray;
  selectedRows = selectRows;
}

function singleDelete(record) {
  confirmBatchDelete([record]);
}

function batchDelete() {
  confirmBatchDelete(selectedRows);
}

function confirmBatchDelete(menuArray) {
  const menuNameArray = menuArray.map((e) => e.menuName);
  Modal.confirm({
    title: "确定要删除如下菜单吗?",
    icon: createVNode(ExclamationCircleOutlined),
    content: _.join(menuNameArray, "、"),
    okText: "删除",
    okType: "danger",
    onOk() {
      console.log("OK");
      const menuIdList = menuArray.map((e) => e.menuId);
      requestDatchDelete(menuIdList);
      selectedRows = [];
    },
    cancelText: "取消",
    onCancel() {},
  });

  async function requestDatchDelete(menuIdList) {
    SmartLoading.show();
    try {
      await menuApi.batchDeleteMenu(menuIdList);
      message.success("删除成功!");
      query();
    } catch (e) {
      console.log(e);
    } finally {
      SmartLoading.hide();
    }
  }
}
function showDrawer(rowData) {
  menuOperateModal.value.showDrawer(rowData);
}
</script>
