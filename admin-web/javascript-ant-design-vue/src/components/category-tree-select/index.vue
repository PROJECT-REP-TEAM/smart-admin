<!--
 * @Description:
 * @Author: zhuoda
 * @Date: 2021-08-12
 * @LastEditTime: 2021-08-18
 * @LastEditors: zhuoda
-->
<template>
  <a-tree-select
    v-model:value="selectValue"
    :style="`width:${width}`"
    :dropdown-style="{ maxHeight: '400px', overflow: 'auto' }"
    :tree-data="categoryTree"
    :placeholder="placeholder"
    tree-default-expand-all
    @change="handleChange"
  />
</template>

<script lang="ts" setup>
import { ref, watch, onMounted } from "vue";
import { categoryApi } from "@/api/business/category/category-api";

// ========================
interface CategoryTreeSelectProps {
  placeholder?: string;
  value?: string | number;
  categoryType?: number; // 分组类型 CATEGORY_TYPE_ENUM
  width: string;
}

const props = withDefaults(defineProps<CategoryTreeSelectProps>(), {
  value: undefined,
  placeholder: "请选择",
  categoryType: undefined,
  width: "100%",
});

const emit = defineEmits<{
  (e: "update:value", value);
  (e: "change", value);
}>();
const categoryTree = ref<[]>([]);
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
    let resp = await categoryApi.queryCategoryTree(
      param
    );
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
  emit("update:value", value);
  emit("change", value);
}

onMounted(queryCategoryTree);
</script>
