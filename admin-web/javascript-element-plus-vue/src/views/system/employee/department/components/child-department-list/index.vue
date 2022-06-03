<!--
 * @Author: zhuoda
 * @Date: 2021-08-13 15:47:44
 * @LastEditTime: 2021-08-17 17:39:17
 * @LastEditors: zhuoda
 * @Description: 当前所选部门的子部门 人员管理右上半部分
 * @FilePath: /smart-admin/src/views/system/employee/department/components/child-department-list/index.vue
-->
<template>
  <el-card class="child-dept-container">
    <el-breadcrumb>
      <el-breadcrumb-item v-for="(item, index) in props.breadcrumb" :key="index">{{ item }}</el-breadcrumb-item>
    </el-breadcrumb>
    <div class="department-list-box">下级部门</div>
    <div class="department-list">
      <el-empty v-if="$lodash.isEmpty(props.selectedDepartmentChildren)" description="暂无数据" :image-size="40"></el-empty>
      <div v-else class="department-item" v-for="item in props.selectedDepartmentChildren" :key='item.departmentId' @click="selectTree(item.departmentId)">
        {{ item.name }}
        <el-icon><arrow-right /></el-icon>
      </div>
    </div>
  </el-card>
</template>
<script setup>
  import emitter from '/@/views/system/employee/department/department-mitt';
  // ----------------------- 以下是字段定义 emits props ---------------------
  const props = defineProps({
    // 部门面包屑
    breadcrumb: Array,
    // 选中部门的孩子
    selectedDepartmentChildren: Array,
  });
  // ----------------------- 以下是计算属性 watch监听 ------------------------

  // ----------------------- 以下是生命周期 ---------------------------------

  // ----------------------- 以下是方法 ------------------------------------
  function selectTree(id) {
    emitter.emit('selectTree', id);
  }
  // ----------------------- 以下是暴露的方法内容 ----------------------------
  defineExpose({});
</script>
<style scoped lang="scss">
  .child-dept-container {
    .department-list-box {
      margin-top: 10px;
      margin-bottom: 0.5em;
      color: #000000d9;
      font-weight: 600;
      font-size: 16px;
      line-height: 1.5;
    }
    .department-list {
      height: 250px;
      overflow-y: auto;
    }
    .department-item {
      cursor: pointer;
      display: flex;
      align-items: center;
      padding: 12px 0;
      color: #000000d9;
      border-bottom: 1px solid #f0f0f0;
    }

    :last-child {
      border-bottom: none;
    }
  }
</style>
