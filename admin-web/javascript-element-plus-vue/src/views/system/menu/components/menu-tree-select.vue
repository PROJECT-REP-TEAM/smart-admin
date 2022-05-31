<!--
 * @Author: zhuoda
 * @Date: 2021-08-10 16:53:06
 * @LastEditTime: 2021-08-16 10:31:42
 * @LastEditors: zhuoda
 * @Description: 菜单树下拉选择
 * @FilePath: /smart-admin/src/views/system/menu/components/menu-tree-select.vue
-->
<template>
  <el-tree-select
    :value="props.value"
    style="width: 100%"
    node-key='menuId'
    :props="{ label: 'menuName' }"
    :fit-input-width='true'
    :filterable='true'
    :clearable='true'
    :default-expand-all='true'
    :data='treeData'
    placeholder="请选择菜单"
    @change='treeSelectChange'
  />
</template>
<script setup>
  import { ref, watch } from 'vue';
  import { menuApi } from '/@/api/system/menu-api';
  import _ from 'lodash';
  // ----------------------- 以下是字段定义 emits props ------------------------
  const props = defineProps({
    value:Number,
    data:Array
  });

  const emit = defineEmits(["update:value"]);

  let treeData = ref([]);
  // ----------------------- 以下是计算属性 watch监听 ------------------------
  watch(
    () => props.data,
    (e) => {
      treeData.value = [];
      if (e) {
        treeData.value = _.cloneDeep(e);
      }
    },
    { immediate: true, deep: true }
  );
  // ----------------------- 以下是生命周期 ------------------------
  // ----------------------- 以下是方法 ------------------------

  // 由父组件调用初始化menuTree或者传入props data
  async function queryMenuTree() {
    let res = await menuApi.queryMenuTree(true);
    treeData.value = res.data;
    // if (!props.value && !_.isEmpty(treeData.value)) {
    //   // 默认取第一条
    //   treeSelectChange(treeData.value[0].menuId);
    // }
  }

  function treeSelectChange(e) {
    emit('update:value', e);
  }

  // ----------------------- 以下是暴露的方法内容 ------------------------
  defineExpose({
    queryMenuTree,
  });
</script>
