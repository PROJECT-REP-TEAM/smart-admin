<!--
 * @Author: zhuoda
 * @Date: 2021-08-13 15:47:44
 * @LastEditTime: 2021-08-17 17:39:17
 * @LastEditors: zhuoda
 * @Description: 当前所选部门的子部门 人员管理右上半部分
 * @FilePath: /smart-admin/src/views/system/employee/department/components/child-department-list/index.vue
-->
<template>
  <a-card class="child-dept-container">
    <a-breadcrumb>
      <a-breadcrumb-item v-for="(item, index) in props.breadcrumb" :key="index">{{ item }}</a-breadcrumb-item>
    </a-breadcrumb>
    <a-typography-title class="department-list-box" :level="4">下级部门</a-typography-title>
    <a-list class="department-list" :data-source="props.selectDeptChild">
      <template #renderItem="{ item }">
        <a-list-item>
          <div class="department-item" @click="selectTree(item.id)">
            {{ item.name }}
          <RightOutlined />
          </div>
        </a-list-item>
      </template>
    </a-list>
  </a-card>
</template>
<script setup lang="ts">
  import emitter from '/@/views/system/employee/department/department-mitt';
  // ----------------------- 以下是字段定义 emits props ---------------------
  const props = defineProps<{
    breadcrumb?: string[];
    selectDeptChild?: [];
  }>();
  // ----------------------- 以下是计算属性 watch监听 ------------------------

  // ----------------------- 以下是生命周期 ---------------------------------

  // ----------------------- 以下是方法 ------------------------------------
  function selectTree(id: number) {
    emitter.emit('selectTree', id);
  }
  // ----------------------- 以下是暴露的方法内容 ----------------------------
  defineExpose({});
</script>
<style scoped lang="less">
  .child-dept-container {
    .department-list-box {
      margin-top: 20px;
    }
    .department-list {
        height: 170px;
        overflow-y: auto;
      }
    .department-item {
      cursor: pointer;
    }
  }
</style>
