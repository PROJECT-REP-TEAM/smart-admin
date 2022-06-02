<!--
 * @Author: zhuoda
 * @Date: 2021-08-28 15:25:29
 * @LastEditTime: 2022-06-02
 * @LastEditors: zhuoda
 * @Description:
 * @FilePath: /smart-admin/src/views/system/employee/role/components/role-tree/role-tree-point.vue
-->
<template>
  <div class="point">
    <div class="point-label">
      <template v-for="module in props.tree" :key="module.menuId">
        <a-checkbox @change="emits('selectCheckbox', module)" :value="module.menuId"
          >{{ module.menuName }}
        </a-checkbox>
      </template>
    </div>
  </div>
</template>
<script setup>
// ----------------------- 以下是字段定义 emits props ---------------------
const props = defineProps({
  tree: {
    type: Array,
    default: [],
  },
  index: {
    type: Number,
    default: 0,
  },
});
let emits = defineEmits("selectCheckbox");
// ----------------------- 以下是计算属性 watch监听 ------------------------

// ----------------------- 以下是生命周期 ---------------------------------

// ----------------------- 以下是方法 ------------------------------------

// ----------------------- 以下是暴露的方法内容 ----------------------------
defineExpose({});
</script>
<style scoped lang="less"></style>
