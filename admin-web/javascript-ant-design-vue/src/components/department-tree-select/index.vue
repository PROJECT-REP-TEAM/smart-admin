<!--
 * @Author: zhuoda
 * @Date: 2021-08-10 16:53:06
 * @LastEditTime: 2022-06-02
 * @LastEditors: zhuoda
 * @Description: 部门树下拉选择
 * @FilePath: /smart-admin/src/views/system/employee/department/components/department-tree-select/index.vue
-->
<template>
  <a-tree-select
    :value="props.value"
    :treeData="treeData"
    :replaceFields="{ title: 'name', key: 'id', value: 'id' }"
    show-search
    style="width: 100%"
    :dropdown-style="{ maxHeight: '400px', overflow: 'auto' }"
    placeholder="请选择部门"
    allow-clear
    tree-default-expand-all
    :multiple="props.multiple"
    @change="treeSelectChange"
  />
</template>
<script setup>
import { onMounted, ref } from "vue";
import _ from "lodash";
import { departmentApi } from "/@/api/system/department/department-api";
// ----------------------- 以下是字段定义 emits props ------------------------

const props = defineProps({
  // 绑定值
  value: Number,
  // 单选多选
  multiple: {
    type: Boolean,
    default: false,
  },
  // 是否内部初始化数据
  init: {
    type: Boolean,
    default: true,
  },
});

const emit = defineEmits(["update:value"]);

let treeData = ref([]);
// ----------------------- 以下是计算属性 watch监听 ------------------------
// ----------------------- 以下是生命周期 ------------------------
onMounted(() => {
  if (props.init) {
    queryDepartmentTree();
  }
});
// ----------------------- 以下是方法 ------------------------
// 外部调用初始化
async function queryDepartmentTree() {
  let res = await departmentApi.departmentTree();
  treeData.value = res.data;
}

function treeSelectChange(e) {
  emit("update:value", e);
}

// ----------------------- 以下是暴露的方法内容 ------------------------
defineExpose({
  queryDepartmentTree,
});
</script>
