<!--
 * @Description: 
 * @Author: zhuoda
 * @Date: 2021-08-03
 * @LastEditTime: 2021-08-28 15:31:50
 * @LastEditors: zhuoda
-->
<template>
  <a-select
    v-model:value="selectValue"
    :style="`width: ${width}px`"
    :placeholder="props.placeholder"
    :showSearch="true"
    :allowClear="true"
    :size="size"
    @change="handleChange"
    @deselect="handleChange"
    :disabled="disabled"
  >
    <a-select-option
      v-for="item in $smartEnumPlugin.getValueDescList(props.enumName)"
      :key="item.value"
      :value="item.value"
    >
      {{ item.desc }}
    </a-select-option>
  </a-select>
</template>

<script lang="ts" setup>
import { ref, watch } from "vue";

// ========================
interface SmartEnumSelectProps {
  enumName: string;
  value?: string | number | string[] | number[];
  width?: number;
  size?: string;
  placeholder?: string;
  disabled?: boolean;
}

const props = withDefaults(defineProps<SmartEnumSelectProps>(), {
  enumName: undefined,
  value: undefined,
  width: 100,
  placeholder: "请选择",
});

const emit = defineEmits<{
  (e: "update:value", value: any): void;
  (e: "change", value: any): void;
}>();

// ======================== 逻辑

const selectValue = ref(props.value);

// 箭头value变化
watch(
  () => props.value,
  (newValue) => {
    selectValue.value = newValue;
  }
);

function handleChange(value: any): void {
  emit("update:value", value);
  emit("change", value);
}
</script>
