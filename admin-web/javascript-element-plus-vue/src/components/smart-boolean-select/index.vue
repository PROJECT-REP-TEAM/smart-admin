<template>
  <el-select
    v-model="selectValue"
    :style="`width: ${width}px`"
    :placeholder="placeholder"
    :filterable='true'
    :clearable='true'
    :size="size"
    @change="handleChange"
    :disabled='disabled'
    no-data-text="暂无数据"
  >
    <el-option
      v-for="item in $smartEnumPlugin.getValueDescList('FLAG_NUMBER_ENUM')"
      :key="item.value"
      :value="item.value"
      :label="item.desc"
    >
      {{ item.desc }}
    </el-option>
  </el-select>
</template>

<script setup>
import _ from "lodash";
import { ref, watch } from "vue";

const props = defineProps({
  value: Number,
  width: {
    type: Number,
    default: 100,
  },
  placeholder: {
    type: String,
    default: '请选择',
  },
  size: {
    type: String,
    default: 'default',
  },
  disabled: {
    type: Boolean,
    default: false,
  },
});

const emit = defineEmits(['update:value', 'change']);

function convertBoolean2number(value)  {
  let result = null;
  if (_.isNaN(value) || _.isNull(value) || _.isUndefined(value)) {
    result = null;
  } else {
    result = value ? 1 : 0;
  }
  return result;
}

const selectValue = ref(convertBoolean2number(props.value));

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
