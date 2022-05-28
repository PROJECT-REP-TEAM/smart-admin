<!--
 * @Description:
 * @Author: zhuoda
 * @Date: 2021-08-12
 * @LastEditTime: 2021-08-18
 * @LastEditors: zhuoda
-->
<template>
  <el-tree-select
    v-model='selectValue'
    :style='`width:${width}`'
    :fit-input-width='true'
    :data='categoryTree'
    :placeholder='placeholder'
    default-expand-all
    @change='handleChange'
  />
</template>

<script setup>
  import { onMounted, ref, watch } from 'vue';
  import { categoryApi } from '/@/api/business/category-api';

  const props = defineProps({
    value: Number,
    placeholder: {
      type: String,
      default: '请选择',
    },
    categoryType: Number,
    width: {
      type: String,
      default: '100%',
    },
  });

  const emit = defineEmits(['update:value', 'change']);
  const categoryTree = ref([]);

  // ======================== 逻辑

  async function queryCategoryTree() {
    if (!props.categoryType) {
      categoryTree.value = [];
      return;
    }
    try {
      let param = {
        categoryType: props.categoryType,
      };
      let resp = await categoryApi.queryCategoryTree(param);
      categoryTree.value = resp.data;
    } catch (e) {
      console.log(e);
    } finally {
      console.log(1);
    }
  }

  const selectValue = ref(props.value);

  // 箭头value变化
  watch(
    () => props.value,
    (newValue) => {
      selectValue.value = newValue;
    },
  );

  // 监听类型变化
  watch(
    () => props.categoryType,
    () => {
      queryCategoryTree();
    },
  );

  function handleChange(value) {
    emit('update:value', value);
    emit('change', value);
  }

  onMounted(queryCategoryTree);
</script>
