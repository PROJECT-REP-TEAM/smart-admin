<!--
 * @Description:
 * @Author: zhuoda
 * @Date: 2021-08-12
 * @LastEditTime: 2022-06-05 17:25:41
 * @LastEditors: LiHaiFan
-->
<template>
  <el-tree-select
    v-model="selectValue"
    :style="`width:${width}`"
    :fit-input-width="true"
    :data="categoryTree"
    :placeholder="placeholder"
    default-expand-all
    @change="handleChange"
  />
</template>

<script lang="ts" setup>
  import { onMounted, ref, watch } from 'vue';
  import { ResponseModel } from '/@/api/base-model/response-model';
  import { categoryApi } from '/@/api/business/category/category-api';
  import { CategoryTreeQueryForm } from '/@/api/business/category/model/category-tree-query-form';
  import { CategoryTreeVo } from '/@/api/business/category/model/category-tree-vo';

  interface CategoryTreeSelectProps {
    placeholder?: string;
    value?: string | number;
    categoryType?: number; // 分组类型 CATEGORY_TYPE_ENUM
    width: string;
  }

  const props = withDefaults(defineProps<CategoryTreeSelectProps>(), {
    value: undefined,
    placeholder: '请选择',
    categoryType: undefined,
    width: '100%',
  });

  const emit = defineEmits<{
    (e: 'update:value', value: any): void;
    (e: 'change', value: any): void;
  }>();
  const categoryTree = ref<CategoryTreeVo[]>([]);

  // ======================== 逻辑

  async function queryCategoryTree() {
    if (!props.categoryType) {
      categoryTree.value = [];
      return;
    }
    try {
      let param: CategoryTreeQueryForm = {
        categoryType: props.categoryType,
      };
      let resp: ResponseModel<CategoryTreeVo[]> = await categoryApi.queryCategoryTree(param);
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
    }
  );

  // 监听类型变化
  watch(
    () => props.categoryType,
    () => {
      queryCategoryTree();
    }
  );

  function handleChange(value: any): void {
    emit('update:value', value);
    emit('change', value);
  }

  onMounted(queryCategoryTree);
</script>
