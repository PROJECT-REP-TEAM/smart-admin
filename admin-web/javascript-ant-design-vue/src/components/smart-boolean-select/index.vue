<template>
  <a-select
    v-model:value="selectValue"
    :style="`width: ${width}px`"
    :placeholder="placeholder"
    :showSearch="true"
    :allowClear="true"
    :size="size"
    @change="handleChange"
    @deselect="handleChange"
  >
    <a-select-option
      v-for="item in $smartEnumPlugin.getValueDescList('FLAG_NUMBER_ENUM')"
      :key="item.value"
      :value="item.value"
    >
      {{ item.desc }}
    </a-select-option>
  </a-select>
</template>

<script setup lang="ts">
import _ from "lodash";
import { ref, watch } from "vue";

interface SmartBooleanSelectProps {
  value?: boolean;
  width: number;
  size?: string;
  placeholder?: string;
}

const props = withDefaults(defineProps<SmartBooleanSelectProps>(), {
  value: undefined,
  width: 100,
  placeholder: "请选择",
});

const emit = defineEmits<{
  (e: "update:value", value);
  (e: "change", value);
}>();

function convertBoolean2number(value: null | boolean | undefined): null | number {
  let result: null | number = null;
  if (_.isNaN(value) || _.isNull(value) || _.isUndefined(value)) {
    result = null;
  } else {
    result = value ? 1 : 0;
  }
  return result;
}

const selectValue = ref<any>(convertBoolean2number(props.value));

// 箭头value变化
watch(
  () => props.value,
  (newValue) => {
    selectValue.value = convertBoolean2number(newValue);
  }
);

const handleChange = (value) => {
  console.log("boolean enum select", value);
  let booleanResult = null;
  if (!_.isUndefined(value)) {
    booleanResult = value === 1 ? true : false;
  }
  emit("update:value", booleanResult);
  emit("change", booleanResult);
};
</script>
