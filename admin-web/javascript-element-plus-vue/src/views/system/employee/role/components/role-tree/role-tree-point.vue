<!--
 * @Author: zhuoda
 * @Date: 2021-08-28 15:25:29
 * @LastEditTime: 2021-08-28 16:11:20
 * @LastEditors: zhuoda
 * @Description:
 * @FilePath: /smart-admin/src/views/system/employee/role/components/role-tree/role-tree-point.vue
-->
<template>
  <div class="point">
    <div class="point-label">
      <template v-for="module in props.tree" :key="module.menuId">
        <el-checkbox @change="emits('selectCheckbox', module)" :label="module.menuId">{{ module.menuName }} </el-checkbox>
      </template>
    </div>
  </div>
</template>
<script setup>
  // ----------------------- 以下是字段定义 emits props ---------------------
  let props = defineProps({
    tree: Array,
    index: {
      type: Number,
      default: 0,
    },
  });
  let emits = defineEmits(['selectCheckbox']);
  defineExpose({});
</script>
<style scoped lang="scss"></style>
