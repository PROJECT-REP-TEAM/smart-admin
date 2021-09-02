<!--
 * @Author: zhuoda
 * @Date: 2021-08-10 16:53:06
 * @LastEditTime: 2021-08-16 10:31:42
 * @LastEditors: zhuoda
 * @Description: 菜单树下拉选择
 * @FilePath: /smart-admin/src/views/system/menu/components/menu-tree-select.vue
-->
<template>
  <a-tree-select
    :value="props.value"
    :treeData="treeData"
    :replaceFields="{ title: 'menuName', key: 'menuId', value: 'menuId' }"
    show-search
    style="width: 100%"
    :dropdown-style="{ maxHeight: '400px', overflow: 'auto' }"
    placeholder="请选择菜单"
    allow-clear
    tree-default-expand-all
    @change="treeSelectChange"
  />
</template>
<script setup lang="ts">
  import { ref, watch } from 'vue';
  import { menuApi } from '/@/api/system/menu/menu-api';
  import type { MenuTreeVo } from '/@/api/system/menu/model/menu-tree-vo';
  import type { ResponseModel } from '/@/api/base-model/response-model';
  import _ from 'lodash';
  // ----------------------- 以下是字段定义 emits props ------------------------
  const props = defineProps<{
    value?: number;
    // 若传入data则不需要调用queryMenuTree接口了
    data?: Array<MenuTreeVo>;
  }>();

  const emit = defineEmits<{
    (e: 'update:value', value: number): void;
  }>();

  let treeData = ref<Array<MenuTreeVo>>([]);
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
    let res: ResponseModel<MenuTreeVo[]> = await menuApi.queryMenuTree(true);
    treeData.value = res.data;
    // if (!props.value && !_.isEmpty(treeData.value)) {
    //   // 默认取第一条
    //   treeSelectChange(treeData.value[0].menuId);
    // }
  }

  function treeSelectChange(e: number) {
    emit('update:value', e);
  }

  // ----------------------- 以下是暴露的方法内容 ------------------------
  defineExpose({
    queryMenuTree,
  });
</script>
