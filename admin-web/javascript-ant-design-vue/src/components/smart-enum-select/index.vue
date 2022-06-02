<!--
 * @Description:
 * @Author: zhuoda
 * @Date: 2021-08-03
 * @LastEditTime: 2022-06-02
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

<script setup>
import { ref, watch } from "vue";

// ========================
const props = defineProps({
  enumName: String,
  value: Number,
  width: {
    type: Number,
    default: 100,
  },
  placeholder: {
    type: String,
    default: "请选择",
  },
  size: {
    type: String,
    default: "default",
  },
  disabled: {
    type: Boolean,
    default: false,
  },
});
const emit = defineEmits(["update:value", "change"]);

// ======================== 逻辑

const selectValue = ref(props.value);

// 箭头value变化
watch(
  () => props.value,
  (newValue) => {
    selectValue.value = newValue;
  }
);

function handleChange(value) {
  emit("update:value", value);
  emit("change", value);
}
</script>
