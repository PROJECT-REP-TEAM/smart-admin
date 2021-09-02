<!--
 * @Author: zhuoda
 * @Date: 2021-08-10 16:53:06
 * @LastEditTime: 2021-08-16 15:59:21
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
<script setup lang="ts">
  import { onMounted, ref } from 'vue';
  import type { ResponseModel } from '/@/api/base-model/response-model';
  import _ from 'lodash';
  import { departmentApi } from '/@/api/system/department/department-api';
  import { DepartmentTreeVo } from '/@/api/system/department/model/department-tree-vo';
  // ----------------------- 以下是字段定义 emits props ------------------------
  interface Props {
    // 绑定值
    value?: number | number[];
    // 单选多选
    multiple?: boolean;
    // 是否内部初始化数据
    init?: boolean;
  }
  const props = withDefaults(defineProps<Props>(), {
    value: undefined,
    multiple: false,
    init: true,
  });

  const emit = defineEmits<{
    (e: 'update:value', value: number | number[]): void;
  }>();

  let treeData = ref<Array<DepartmentTreeVo>>([]);
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
    let res: ResponseModel<DepartmentTreeVo[]> = await departmentApi.departmentTree();
    treeData.value = res.data;
  }

  function treeSelectChange(e: number | number[]) {
    emit('update:value', e);
  }

  // ----------------------- 以下是暴露的方法内容 ------------------------
  defineExpose({
    queryDepartmentTree,
  });
</script>
