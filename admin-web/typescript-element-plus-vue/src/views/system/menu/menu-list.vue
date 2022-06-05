<template>
  <el-form class="smart-query-form">
    <el-row class="smart-query-form-row">
      <el-form-item label="关键字" class="smart-query-form-item">
        <el-input style="width: 300px" v-model="queryForm.keywords" placeholder="菜单名称/路由地址/组件路径/权限字符串" />
      </el-form-item>

      <el-form-item label="类型" class="smart-query-form-item">
        <smart-enum-select
          :width="100"
          v-model:value="queryForm.menuType"
          placeholder="请选择类型"
          enum-name="MENU_TYPE_ENUM"
          @change="disabledFlagChange"
        />
      </el-form-item>

      <el-form-item label="启用" class="smart-query-form-item">
        <smart-enum-select :width="100" enum-name="FLAG_NUMBER_ENUM" v-model:value="queryForm.disabledFlag" @change="disabledFlagChange" />
      </el-form-item>

      <el-form-item class="smart-query-form-item smart-margin-left10">
        <el-button v-privilege="'system:menu:query'" type="primary" @click="query" icon="Search"> 查询 </el-button>

        <el-button @click="resetQuery" icon="RefreshLeft"> 重置 </el-button>
        <el-button class="smart-margin-left20" @click="moreQueryConditionFlag = !moreQueryConditionFlag" icon="MoreFilled">
          {{ moreQueryConditionFlag ? '收起' : '展开' }}
        </el-button>
      </el-form-item>
    </el-row>

    <el-row class="smart-query-form-row" v-show="moreQueryConditionFlag">
      <el-form-item label="外链" class="smart-query-form-item">
        <smart-enum-select :width="100" enum-name="FLAG_NUMBER_ENUM" v-model:value="queryForm.frameFlag" @change="disabledFlagChange" />
      </el-form-item>

      <el-form-item label="缓存" class="smart-query-form-item">
        <smart-enum-select :width="100" enum-name="FLAG_NUMBER_ENUM" v-model:value="queryForm.cacheFlag" @change="disabledFlagChange" />
      </el-form-item>

      <el-form-item label="显示" class="smart-query-form-item">
        <smart-enum-select :width="100" enum-name="FLAG_NUMBER_ENUM" v-model:value="queryForm.visibleFlag" @change="disabledFlagChange" />
      </el-form-item>
    </el-row>
  </el-form>

  <el-card shadow="hover">
    <el-row class="smart-table-btn-block">
      <div class="smart-table-operate-block">
        <el-button v-privilege="'system:menu:add'" type="primary" size="small" @click="showDrawer" icon="Plus"> 添加菜单 </el-button>

        <el-button v-privilege="'system:menu:delete'" type="danger" size="small" @click="batchDelete" :disabled="!hasSelected" icon="Delete">
          批量删除
        </el-button>
      </div>
      <div class="smart-table-setting-block"></div>
    </el-row>

    <el-table
      size="small"
      :data="tableData"
      border
      :rowKey="rowKey"
      v-loading="tableLoading"
      @select="onSelect"
      @select-all="onSelect"
      :default-expand-all="true"
    >
      <el-table-column type="selection" width="55" />
      <el-table-column prop="menuName" label="名称" width="200"></el-table-column>
      <el-table-column prop="menuType" label="类型">
        <template #default="scope">
          <span>{{ $smartEnumPlugin.getDescByValue('MENU_TYPE_ENUM', scope.row.menuType) }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="icon" label="图标" width="50">
        <template #default="scope">
          <component :is="$icons[scope.row.icon]" />
        </template>
      </el-table-column>
      <el-table-column prop="webPerms" label="权限"></el-table-column>
      <el-table-column prop="frameFlag" label="外链">
        <template #default="scope">
          <span>{{ $smartEnumPlugin.getDescByValue('FLAG_NUMBER_ENUM', scope.row.frameFlag) }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="cacheFlag" label="缓存">
        <template #default="scope">
          <span>{{ $smartEnumPlugin.getDescByValue('FLAG_NUMBER_ENUM', scope.row.cacheFlag) }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="disabledFlag" label="禁用">
        <template #default="scope">
          <span>{{ $smartEnumPlugin.getDescByValue('FLAG_NUMBER_ENUM', scope.row.disabledFlag) }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="sort" label="顺序"></el-table-column>
      <el-table-column prop="createTime" label="创建时间"></el-table-column>
      <el-table-column prop="operate" label="操作" width="150">
        <template #default="scope">
          <el-button v-privilege="'system:menu:update'" link type="primary" size="small" @click="showDrawer(scope.row)"> 编辑 </el-button>
          <el-button v-privilege="'system:menu:delete'" link type="danger" size="small" @click="singleDelete(scope.row)">删除 </el-button>
        </template>
      </el-table-column>
    </el-table>
  </el-card>

  <MenuOperateModal ref="menuOperateModal" @reloadList="query" />
</template>
<script lang="ts" setup>
  import { computed, onMounted, reactive, ref } from 'vue';
  import SmartEnumSelect from '/@/components/smart-enum-select/index.vue';
  import { menuApi } from '/@/api/system/menu/menu-api';
  import MenuOperateModal from './components/menu-operate-modal.vue';
  import _ from 'lodash';
  import lodash from 'lodash';
  import { ElMessage, ElMessageBox } from 'element-plus';
  import { useSpinStore } from '/@/store/modules/system/spin';
  import { MenuVo } from '/@/api/system/menu/model/menu-vo';
  import { MenuUpdateForm } from '/@/api/system/menu/model/menu-update-form';

  const menuOperateModal = ref();

  interface MenuQueryForm {
    // 关键字
    keywords?: string;
    // 类型
    menuType?: number;
    // 是否外链
    frameFlag?: number;
    // 是否缓存
    cacheFlag?: number;
    // 是否显示
    visibleFlag?: number;
    // 是否禁用
    disabledFlag?: number;
  }

  function filterQueryForm(menuList: MenuVo[], queryForm: MenuQueryForm): MenuVo[] {
    if (!menuList || menuList.length === 0) {
      return [];
    }

    let filterResult = [];

    for (const menu of menuList) {
      if (isMenuExistKeywords(menu, queryForm.keywords) && isMenuExistMenuType(menu, queryForm.menuType) && isMenuExistMenuFlag(menu, queryForm)) {
        filterResult.push(menu);
      }
    }

    return filterResult;
  }

  function isMenuExistMenuFlag(menu: MenuVo, queryForm: MenuQueryForm) {
    let frameFlagCondition = false;
    if (!_.isNil(queryForm.frameFlag)) {
      frameFlagCondition = !_.isNil(menu.frameFlag) && menu.frameFlag === (queryForm.frameFlag === 1);
    } else {
      frameFlagCondition = true;
    }

    let cacheFlagCondition = false;
    if (!_.isNil(queryForm.cacheFlag)) {
      cacheFlagCondition = !_.isNil(menu.cacheFlag) && menu.cacheFlag === (queryForm.cacheFlag === 1);
    } else {
      cacheFlagCondition = true;
    }

    let visibleFlagCondition = false;
    if (!_.isNil(queryForm.visibleFlag)) {
      visibleFlagCondition = !_.isNil(menu.visibleFlag) && menu.visibleFlag === (queryForm.visibleFlag === 1);
    } else {
      visibleFlagCondition = true;
    }

    let disabledFlagCondition = false;
    if (!_.isNil(queryForm.disabledFlag)) {
      disabledFlagCondition = !_.isNil(menu.disabledFlag) && menu.disabledFlag === (queryForm.disabledFlag === 1);
    } else {
      disabledFlagCondition = true;
    }

    return frameFlagCondition && cacheFlagCondition && visibleFlagCondition && disabledFlagCondition;
  }

  function isMenuExistMenuType(menu: MenuVo, menuType?: number) {
    if (!menuType) {
      return true;
    }

    if (menu.menuType && menu.menuType === menuType) {
      return true;
    }
    return false;
  }

  function isMenuExistKeywords(menu: MenuVo, keywords?: string) {
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

  function findTopMenuArray(menuList: MenuVo[]): MenuVo[] {
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

  const queryFormState: MenuQueryForm = {
    keywords: '',
    menuType: undefined,
    frameFlag: undefined,
    cacheFlag: undefined,
    visibleFlag: undefined,
    disabledFlag: undefined,
  };
  const queryForm = reactive<MenuQueryForm>({ ...queryFormState });
  const moreQueryConditionFlag = ref(false);
  const tableLoading = ref(false);
  const rowKey = ref('menuId');

  const tableData = ref<MenuVo[]>([]);

  function resetQuery() {
    Object.assign(queryForm, queryFormState);
    query();
  }

  async function query() {
    try {
      tableLoading.value = true;
      let responseModel = await menuApi.queryMenu();
      // 过滤搜索条件
      console.log('queryForm', queryForm);
      const filterMenuList = filterQueryForm(responseModel.data, queryForm);
      //找到顶级菜单
      const topMenu = findTopMenuArray(filterMenuList);
      recursiveMenuTree(filterMenuList, topMenu);

      tableData.value = topMenu;
    } catch (e) {
      console.log(e);
    } finally {
      tableLoading.value = false;
    }
  }

  function recursiveMenuTree(menuList: MenuVo[], parentArray: MenuVo[]) {
    for (const parent of parentArray) {
      const children = menuList.filter((e) => e.parentId === parent.menuId);
      if (children.length > 0) {
        parent.children = children;
        recursiveMenuTree(menuList, parent.children);
      }
    }
  }

  onMounted(query);

  const disabledFlagChange = (value: string | string[] | number | number[]) => {
    console.log(1, value);
  };

  //-------------- 多选操作
  const selectedRowKeys = ref<number[]>([]);
  const selectionRows = ref<MenuVo[]>([]);
  const hasSelected = computed(() => selectedRowKeys.value.length > 0);

  // 选择行
  async function onSelect(selection: MenuVo[]) {
    // 找到当页未被勾选项
    let unchecked = lodash.xorBy(tableData.value, selection, rowKey.value);
    // 合并当前与选中项
    selectedRowKeys.value = lodash.unionBy(
      selectedRowKeys.value,
      selection.map((e: any) => e[rowKey.value])
    );
    selectionRows.value = lodash.unionBy(selectionRows.value, selection, rowKey.value);
    // 移除未被勾选项
    selectedRowKeys.value = lodash.differenceBy(
      selectedRowKeys.value,
      unchecked.map((e: any) => e[rowKey.value])
    );
    selectionRows.value = lodash.differenceBy(selectionRows.value, unchecked, rowKey.value);
  }

  function singleDelete(record: MenuVo) {
    confirmBatchDelete([record]);
  }

  function batchDelete() {
    confirmBatchDelete(selectionRows.value);
  }

  function confirmBatchDelete(menuArray: MenuVo[]) {
    const menuNameArray = menuArray.map((e) => e.menuName);
    ElMessageBox.confirm(_.join(menuNameArray, '、'), '确定要删除如下菜单吗', {
      confirmButtonText: '删除',
      cancelButtonText: '取消',
      type: 'error',
    })
      .then(() => {
        console.log('OK');
        const menuIdList = menuArray.map((e) => e.menuId);
        requestBatchDelete(menuIdList);
        selectionRows.value = [];
        selectedRowKeys.value = [];
      })
      .catch(() => {});
  }

  async function requestBatchDelete(menuIdList?: number[]) {
    if (!menuIdList) {
      return;
    }
    useSpinStore().show();
    try {
      await menuApi.batchDeleteMenu(menuIdList);
      ElMessage.success('删除成功!');
      await query();
    } catch (e) {
      console.log(e);
    } finally {
      useSpinStore().hide();
    }
  }

  function showDrawer(rowData?: MenuUpdateForm) {
    menuOperateModal.value.showDrawer(rowData);
  }
</script>
