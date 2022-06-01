<!--
 * @Author: zhuoda
 * @Date: 2021-08-10 16:53:06
 * @LastEditTime: 2021-08-16 15:59:21
 * @LastEditors: zhuoda
 * @Description: 部门树下拉选择
 * @FilePath: /smart-admin/src/views/system/employee/department/components/department-tree-select/index.vue
-->
<template>
  <el-tree-select
    :value='props.value'
    style='width: 100%'
    node-key='departmentId'
    :props="{ label: 'name' }"
    :fit-input-width='true'
    :filterable='true'
    :clearable='true'
    :default-expand-all='true'
    :multiple='multiple'
    :data='treeData'
    placeholder='请选择部门'
    @change='treeSelectChange'
  />
</template>
<script setup>
  import { onMounted, ref } from 'vue';
  import { departmentApi } from '/@/api/system/department-api';
  // ----------------------- 以下是字段定义 emits props ------------------------
  const props = defineProps({
    value: Number | Array,
    multiple: {
      type: Boolean,
      default: false,
    },
    init: {
      type: Boolean,
      default: true,
    },
  });
  const emit = defineEmits(['update:value']);

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
    emit('update:value', e);
  }

  // ----------------------- 以下是暴露的方法内容 ------------------------
  defineExpose({
    queryDepartmentTree,
  });
</script>
