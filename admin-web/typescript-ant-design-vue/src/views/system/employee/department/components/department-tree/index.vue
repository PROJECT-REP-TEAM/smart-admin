<!--
 * @Author: zhuoda
 * @Date: 2021-08-12 17:34:00
 * @LastEditTime: 2021-08-30 09:00:45
 * @LastEditors: zhuoda
 * @Description: 
 * @FilePath: /smart-admin/src/views/system/employee/department/components/department-tree/index.vue
-->
<template>
  <a-card class="tree-container">
    <a-input v-model:value="keywords" placeholder="请输入部门名称" />
    <a-tree
      v-if="!_.isEmpty(treeData)"
      v-model:selectedKeys="selectedKeys"
      v-model:checkedKeys="checkedKeys"
      class="tree"
      :treeData="treeData"
      :replaceFields="{ title: 'name', key: 'id', value: 'id' }"
      style="width: 100%; overflow-x: auto"
      :style="[!height ? '' : { height: `${height}px`, overflowY: 'auto' }]"
      :showLine="!props.checkable"
      :checkable="props.checkable"
      :checkStrictly="props.checkStrictly"
      :selectable="!props.checkable"
      defaultExpandAll
      @select="treeSelectChange"
    >
      <template #title="item">
        <a-popover placement="right">
          <template #content>
            <div style="display: flex; flex-direction: column">
              <a-button type="text" @click="addDepartment(item.dataRef)">添加下级</a-button>
              <a-button type="text" @click="updateDepartment(item.dataRef)">修改</a-button>
              <a-button type="text" v-if="item.id != topDeptId" @click="deleteDepartment(item.id)">删除</a-button>
            </div>
          </template>
          {{ item.name }}
        </a-popover>
      </template>
    </a-tree>
    <!-- 添加编辑部门弹窗 -->
    <OperateDepartmentModal ref="operateDepartmentModal" @reload="reload" />
  </a-card>
</template>
<script setup lang="ts">
  import { ref } from '@vue/reactivity';
  import { onUnmounted, watch } from '@vue/runtime-core';
  import _ from 'lodash';
  import { ResponseModel } from '/@/api/base-model/response-model';
  import { departmentApi } from '/@/api/system/department/department-api';
  import { DepartmentTreeVo } from '/@/api/system/department/model/department-tree-vo';
  import { DepartmentVo } from '/@/api/system/department/model/department-vo';
  import emitter from '/@/views/system/employee/department/department-mitt';
  import OperateDepartmentModal from '../operate-department-modal/index.vue';
  import { Modal } from 'ant-design-vue';
  import { createVNode, onMounted } from 'vue';
  import { ExclamationCircleOutlined } from '@ant-design/icons-vue';
  import { useSpinStore } from '/@/store/modules/system/spin';
  // ----------------------- 以下是字段定义 emits props ---------------------
  const operateDepartmentModal = ref();
  const keywords = ref<string>('');
  const selectedKeys = ref<number[]>([]);
  // const expandedKeys = ref<number[]>([]);
  const checkedKeys = ref<number[]>([]);
  const departmentList = ref<DepartmentVo[]>([]);
  const treeData = ref<DepartmentTreeVo[]>([]);
  const idInfoMap = ref<Map<number, DepartmentVo>>(new Map());
  const topDeptId = ref<number>();
  const selectDeptChild = ref<DepartmentTreeVo[] | undefined>([]);
  const breadcrumb = ref<string[]>([]);
  const operateId = ref<number>();
  interface Props {
    // 是否可以选中
    checkable?: boolean;
    // 父子节点选中状态不再关联
    checkStrictly?: boolean;
    // 是否内部初始化数据
    init?: boolean;
    // 树高度 超出出滚动条
    height?: number;
  }
  const props = withDefaults(defineProps<Props>(), {
    checkable: false,
    checkStrictly: false,
    init: true,
  });
  // ----------------------- 以下是计算属性 watch监听 ------------------------
  emitter.on('selectTree', selectTree);
  watch(
    () => keywords.value,
    () => {
      onSearch();
    }
  );
  // ----------------------- 以下是生命周期 ---------------------------------
  onMounted(() => {
    if (props.init) {
      queryDepartmentTree();
    }
  });
  onUnmounted(() => {
    emitter.all.clear();
  });
  // ----------------------- 以下是方法 ------------------------------------
  async function reload() {
    await queryDepartmentTree();
    if (operateId.value) {
      selectTree(operateId.value);
    }
  }
  async function queryDepartmentTree() {
    let res: ResponseModel<DepartmentVo[]> = await departmentApi.queryAllDepartment();
    let data = res.data;
    departmentList.value = data;
    // 构建树形结构
    treeData.value = buildDepartmentTree(data, 0);
    // 构建id-info集合
    data.forEach((e) => {
      idInfoMap.value.set(e.id, e);
    });
    if (!_.isEmpty(treeData.value)) {
      let topData = treeData.value[0];
      // 默认最顶级ID为列表中返回的第一条数据的ID
      topDeptId.value = topData.id;
      // selectedKeys.value = [topData.id];
      // 默认选中的部门下级为列表中返回的第一条数据的children
      // selectDeptChild.value = topData.children;
      // 默认面包屑
      // breadcrumb.value = [topData.name];
      // if (_.isEmpty(expandedKeys.value)) {
      //   expandedKeys.value = [topData.id];
      // }
    }
  }
  // 构建部门树
  function buildDepartmentTree(data: DepartmentVo[], parentId: number) {
    let treeData: DepartmentTreeVo[] = data.filter((e) => e.parentId == parentId) || [];
    treeData.forEach((e) => {
      e.children = buildDepartmentTree(data, e.id);
    });
    return treeData;
  }
  // 树筛选
  function onSearch() {
    if (!keywords.value) {
      // 构建树形结构
      treeData.value = buildDepartmentTree(departmentList.value, 0);
      return;
    }
    let originData = departmentList.value?.concat();
    if (!originData) {
      return;
    }
    // 筛选出名称符合的部门
    let filterDepartmenet = originData.filter((e) => e.name.indexOf(keywords.value) > -1);
    let filterDepartmentList: DepartmentVo[] = [];
    // 循环筛选出的部门 构建部门树
    filterDepartmenet.forEach((e) => {
      recursionFilterDepartment(filterDepartmentList, e.id, false);
    });
    // 构建树形结构
    treeData.value = buildDepartmentTree(filterDepartmentList, 0);
  }
  function selectTree(id: number) {
    selectedKeys.value = [id];
    treeSelectChange(selectedKeys.value);
  }
  // 树选中
  function treeSelectChange(idList: number[]) {
    // 初始化
    breadcrumb.value = [];
    // 初始选中的部门下级
    selectDeptChild.value = [];
    if (_.isEmpty(idList)) {
      return;
    }
    let id = idList[0];
    // 寻找选择的下级
    selectDeptChild.value = departmentList.value.filter((e) => e.parentId == id);
    // 构建面包屑
    let filterDepartmentList: DepartmentVo[] = [];
    recursionFilterDepartment(filterDepartmentList, id, true);
    breadcrumb.value = filterDepartmentList.map((e) => e.name);
    // expandedKeys.value = filterDepartmentList.map((e) => e.id);
  }
  // 根据ID递归筛选部门
  function recursionFilterDepartment(resList: DepartmentVo[], id: number, unshift: boolean) {
    let info = idInfoMap.value.get(id);
    if (!info || resList.some((e) => e.id == id)) {
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
  // 添加编辑部门
  function addDepartment(e: any) {
    let data: DepartmentVo = {
      id: 0,
      name: '',
      parentId: e.id,
    };
    operateId.value = e.id;
    operateDepartmentModal.value.showModal(data);
  }
  function updateDepartment(e: any) {
    operateId.value = e.id;
    operateDepartmentModal.value.showModal(e);
  }
  function deleteDepartment(id: number) {
    Modal.confirm({
      title: '提醒',
      icon: createVNode(ExclamationCircleOutlined),
      content: '确定要删除该部门吗?',
      okText: '删除',
      okType: 'danger',
      async onOk() {
        useSpinStore().show();
        try {
          // 若删除的是当前的部门 先找到上级部门
          let selectedKey = null;
          if (!_.isEmpty(selectedKeys.value)) {
            selectedKey = selectedKeys.value[0];
            if (selectedKey == id) {
              let selectInfo = departmentList.value.find((e) => e.id == id);
              if (selectInfo && selectInfo.parentId) {
                selectedKey = selectInfo.parentId;
              }
            }
          }
          await departmentApi.deleteDepartment(id);
          await queryDepartmentTree();
          // 刷新选中部门
          if (selectedKey) {
            selectTree(selectedKey);
          }
        } catch (error) {
          console.log(error);
        } finally {
          useSpinStore().hide();
        }
      },
      cancelText: '取消',
      onCancel() {},
    });
  }
  // ----------------------- 以下是暴露的方法内容 ----------------------------
  defineExpose({
    queryDepartmentTree,
    selectDeptChild,
    breadcrumb,
    selectedKeys,
    checkedKeys,
  });
</script>
<style scoped lang="less">
  .tree-container {
    height: 100%;
    .tree {
      margin-top: 10px;
    }
  }
</style>
