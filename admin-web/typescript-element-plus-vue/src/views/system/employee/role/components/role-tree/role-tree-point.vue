<!--
 * @Author: zhuoda
 * @Date: 2021-08-28 15:25:29
 * @LastEditTime: 2022-06-05 23:01:44
 * @LastEditors: LiHaiFan
 * @Description:
 * @FilePath: \typescript-element-plus-vue\src\views\system\employee\role\components\role-tree\role-tree-point.vue
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
<script setup lang="ts">
  import { MenuSimpleTreeVo } from '/@/api/system/menu/model/menu-simple-tree-vo';
  // ----------------------- 以下是字段定义 emits props ---------------------
  let props = withDefaults(
    defineProps<{
      tree?: MenuSimpleTreeVo[];
      index?: number;
    }>(),
    {
      index: 0,
    }
  );
  let emits = defineEmits<{
    (e: 'selectCheckbox', module: MenuSimpleTreeVo): void;
  }>();
  defineExpose({});
</script>
<style scoped lang="scss"></style>
