<!--
 * @Author: zhuoda
 * @Date: 2021-08-12 17:34:00
 * @LastEditTime: 2022-06-06 00:52:21
 * @LastEditors: LiHaiFan
 * @Description:
 * @FilePath: \typescript-element-plus-vue\src\views\system\employee\department\components\department-tree\index.vue
-->
<template>
  <el-card class="tree-container">
    <el-row>
      <el-input v-model="keywords" placeholder="请输入部门名称" />
    </el-row>
    <el-tree
      ref="tree"
      :current-node-key="selectedKey"
      :default-checked-keys="checkedKeys"
      class="tree"
      :data="departmentTreeData"
      :props="defaultProps"
      style="width: 100%; overflow-x: auto"
      :style="[!height ? '' : { height: `${height}px`, overflowY: 'auto' }]"
      :show-checkbox="props.checkable"
      :check-on-click-node="!props.checkable"
      default-expand-all
      highlight-current
      :expand-on-click-node="false"
      node-key="departmentId"
      @node-click="treeSelectNodeChange"
    >
      <template #default="{ node, data }">
        <el-dropdown placement="right" v-if="props.showMenu" trigger="hover">
          <div>
            {{ data.name }}
            <!--显示排序字段-->
            <template v-if="showSortFlag">
              <span class="sort-span">({{ data.sort }})</span>
            </template>
          </div>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item @click="addDepartment(data)" v-privilege="'department:add'">添加下级</el-dropdown-item>
              <el-dropdown-item @click="updateDepartment(data)" v-privilege="'department:edit'">修改</el-dropdown-item>
              <el-dropdown-item
                v-if="data.departmentId != topDepartmentId"
                @click="deleteDepartment(data.departmentId)"
                v-privilege="'department:delete'"
                >删除</el-dropdown-item
              >
            </el-dropdown-menu>
          </template>
        </el-dropdown>
        <div v-else>{{ data.name }}</div>
      </template>
    </el-tree>
    <!-- 添加编辑部门弹窗 -->
    <OperateDepartmentModal ref="operateDepartmentModal" @reload="refresh" />
  </el-card>
</template>
<script setup lang="ts">
  import { ref, onMounted, onUnmounted, watch, computed } from 'vue';
  import _ from 'lodash';
  import { departmentApi } from '/@/api/system/department/department-api';
  import OperateDepartmentModal from '../operate-department-modal/index.vue';
  import departmentEmitter from '/@/views/system/employee/department/department-mitt';
  import { useSpinStore } from '/@/store/modules/system/spin';
  import { smartSentry } from '/@/lib/smart-sentry';
  import { ElMessageBox } from 'element-plus';
  import { DepartmentVo } from '/@/api/system/department/model/department-vo';
  import { DepartmentTreeVo } from '/@/api/system/department/model/department-tree-vo';

  const DEPARTMENT_PARENT_ID = 0;

  // ----------------------- 组件参数 ---------------------
  interface Props {
    // 是否可以选中
    checkable?: boolean;
    // 父子节点选中状态不再关联
    checkStrictly?: boolean;
    // 是否内部初始化数据
    init?: boolean;
    // 树高度 超出出滚动条
    height?: number;
    // 是否显示操作菜单
    showMenu?: Boolean;
  }
  const props = withDefaults(defineProps<Props>(), {
    checkable: false,
    checkStrictly: false,
    init: true,
    showMenu: false,
  });
  // ----------------------- 部门树的展示 ---------------------
  const topDepartmentId = ref();
  // 所有部门列表
  const departmentList = ref<DepartmentVo[]>([]);
  // 部门树形数据
  const departmentTreeData = ref<DepartmentTreeVo[]>([]);
  // 存放部门id和部门，用于查找
  const idInfoMap = ref(new Map());
  // 是否显示排序字段
  const showSortFlag = ref(false);
  let defaultProps = {
    children: 'children',
    label: 'name',
  };
  onMounted(() => {
    queryDepartmentTree();
  });

  // 刷新
  async function refresh() {
    await queryDepartmentTree();
    if (currentSelectedDepartmentId.value) {
      selectTree(currentSelectedDepartmentId.value);
    }
  }

  // 查询部门列表并构建 部门树
  async function queryDepartmentTree() {
    let res = await departmentApi.queryAllDepartment();
    let data = res.data;
    departmentList.value = data;
    departmentTreeData.value = buildDepartmentTree(data, DEPARTMENT_PARENT_ID);

    data.forEach((e) => {
      idInfoMap.value.set(e.departmentId, e);
    });

    // 默认显示 最顶级ID为列表中返回的第一条数据的ID
    if (!_.isEmpty(departmentTreeData.value) && departmentTreeData.value.length > 0) {
      topDepartmentId.value = departmentTreeData.value[0].departmentId;
    }
  }

  // 构建部门树
  function buildDepartmentTree(data: DepartmentVo[], parentId: number) {
    let children: DepartmentTreeVo[] = data.filter((e) => e.parentId == parentId) || [];
    children.forEach((e) => {
      e.children = buildDepartmentTree(data, e.departmentId);
    });
    updateDepartmentPreIdAndNextId(children);
    return children;
  }

  // 更新树的前置id和后置id
  function updateDepartmentPreIdAndNextId(data: DepartmentTreeVo[]) {
    for (let index = 0; index < data.length; index++) {
      if (index === 0) {
        data[index].nextId = data.length > 1 ? data[1].departmentId : undefined;
        continue;
      }

      if (index === data.length - 1) {
        data[index].preId = data[index - 1].departmentId;
        data[index].nextId = undefined;
        continue;
      }

      data[index].preId = data[index - 1].departmentId;
      data[index].nextId = data[index + 1].departmentId;
    }
  }

  // ----------------------- 树的选中 ---------------------
  const tree = ref();
  const selectedKey = ref<number>();
  const breadcrumb = ref<string[]>([]);
  const currentSelectedDepartmentId = ref<number>();
  const selectedDepartmentChildren = ref<DepartmentTreeVo[]>([]);

  departmentEmitter.on('selectTree', selectTree);

  const checkedKeys = computed(() => {
    if (!tree.value) {
      return;
    }
    return tree.value.getCheckedKeys();
  });

  function selectTree(departmentId: number) {
    selectedKey.value = departmentId;
    treeSelectChange(selectedKey.value);
    if (tree.value) {
      tree.value.setCurrentKey(departmentId);
    }
  }

  // 树节点选中
  function treeSelectNodeChange(row: DepartmentTreeVo) {
    treeSelectChange(row.departmentId);
  }

  // 树选中方法
  function treeSelectChange(departmentId: number) {
    if (!departmentId) {
      breadcrumb.value = [];
      selectedDepartmentChildren.value = [];
      return;
    }
    selectedKey.value = departmentId;
    selectedDepartmentChildren.value = departmentList.value.filter((e) => e.parentId == departmentId);
    let filterDepartmentList: DepartmentTreeVo[] = [];
    recursionFilterDepartment(filterDepartmentList, departmentId, true);
    breadcrumb.value = filterDepartmentList.map((e) => e.name);
  }

  // ----------------------- 部门树的展示 筛选 ---------------------
  const keywords = ref('');
  watch(
    () => keywords.value,
    () => {
      onSearch();
    }
  );

  // 筛选
  function onSearch() {
    if (!keywords.value) {
      departmentTreeData.value = buildDepartmentTree(departmentList.value, DEPARTMENT_PARENT_ID);
      return;
    }
    let originData = departmentList.value?.concat();
    if (!originData) {
      return;
    }
    // 筛选出名称符合的部门
    let filterDepartment = originData.filter((e) => e.name.indexOf(keywords.value) > -1);
    let filterDepartmentList: DepartmentVo[] = [];
    // 循环筛选出的部门 构建部门树
    filterDepartment.forEach((e) => {
      recursionFilterDepartment(filterDepartmentList, e.departmentId, false);
    });

    departmentTreeData.value = buildDepartmentTree(filterDepartmentList, DEPARTMENT_PARENT_ID);
  }

  // 根据ID递归筛选部门
  function recursionFilterDepartment(resList: DepartmentVo[], departmentId: number, unshift: boolean) {
    let info = idInfoMap.value.get(departmentId);
    if (!info || resList.some((e) => e.departmentId == departmentId)) {
      return;
    }
    if (unshift) {
      resList.unshift(info);
    } else {
      resList.push(info);
    }
    if (info.parentId && info.parentId != 0) {
      recursionFilterDepartment(resList, info.parentId, unshift);
    }
  }

  // ----------------------- 表单操作：添加部门/修改部门/删除部门/上下移动 ---------------------
  const operateDepartmentModal = ref();

  // 添加
  function addDepartment(e: DepartmentTreeVo) {
    let data = {
      departmentId: 0,
      name: '',
      parentId: e.departmentId,
    };
    currentSelectedDepartmentId.value = e.departmentId;
    operateDepartmentModal.value.showModal(data);
  }
  // 编辑
  function updateDepartment(e: DepartmentTreeVo) {
    currentSelectedDepartmentId.value = e.departmentId;
    operateDepartmentModal.value.showModal(e);
  }

  // 删除
  function deleteDepartment(departmentId: number) {
    ElMessageBox.confirm('确定要删除该部门吗?', '提醒', {
      confirmButtonText: '删除',
      cancelButtonText: '取消',
      type: 'warning',
    }).then(async () => {
      useSpinStore().show();
      try {
        // 若删除的是当前的部门 先找到上级部门
        if (selectedKey.value && selectedKey.value == departmentId) {
          let selectInfo = departmentList.value.find((e) => e.departmentId == departmentId);
          if (selectInfo && selectInfo.parentId) {
            selectedKey.value = selectInfo.parentId;
          }
        }
        await departmentApi.deleteDepartment(departmentId);
        await queryDepartmentTree();
        // 刷新选中部门
        if (selectedKey.value) {
          selectTree(selectedKey.value);
        }
      } catch (error) {
        smartSentry.captureException(error);
      } finally {
        useSpinStore().hide();
      }
    });
  }

  onUnmounted(() => {
    departmentEmitter.all.clear();
  });

  // ----------------------- 以下是暴露的方法内容 ----------------------------
  defineExpose({
    queryDepartmentTree,
    selectedDepartmentChildren,
    breadcrumb,
    selectedKey,
    checkedKeys,
    keywords,
  });
</script>
<style scoped lang="scss">
  .tree-container {
    height: 100%;
    .tree {
      margin-top: 10px;
    }

    .sort-flag-row {
      margin-top: 10px;
    }

    .sort-span {
      margin-left: 5px;
      color: #67c23a;
    }
    .no-data {
      margin: 10px;
    }
  }
</style>
