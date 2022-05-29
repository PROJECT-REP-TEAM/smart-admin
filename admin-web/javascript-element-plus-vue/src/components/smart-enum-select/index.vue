<!--
 * @Description:
 * @Author: zhuoda
 * @Date: 2021-08-03
 * @LastEditTime: 2021-08-28 15:31:50
 * @LastEditors: zhuoda
-->
<template>
  <el-select
    v-model='selectValue'
    :style='`width: ${width}px`'
    :placeholder='placeholder'
    :fit-input-width='true'
    :filterable='true'
    :clearable='true'
    :size='size'
    @change='handleChange'
    :disabled='disabled'
    no-data-text="暂无数据"
  >
    <el-option
      v-for='item in $smartEnumPlugin.getValueDescList(props.enumName)'
      :key='item.value'
      :value='item.value'
      :label="item.desc"
    >
      {{ item.desc }}
    </el-option>
  </el-select>
</template>

<script setup>
  import { ref, watch } from 'vue';

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

  // ======================== 逻辑

  const selectValue = ref(props.value);

  // 箭头value变化
  watch(
    () => props.value,
    (newValue) => {
      selectValue.value = newValue;
    },
  );

  function handleChange(value) {
    emit('update:value', value);
    emit('change', value);
  }
</script>
