<!--
 * @Author: LiHaiFan
 * @Date: 2022-06-05 15:26:04
 * @LastEditTime: 2022-06-05 17:59:21
 * @LastEditors: LiHaiFan
 * @Description: 
 * @FilePath: \typescript-element-plus-vue\src\components\smart-boolean-select\index.vue
-->
<template>
  <el-select
    v-model="selectValue"
    :style="`width: ${width}px`"
    :placeholder="placeholder"
    :filterable="true"
    :clearable="true"
    :size="size"
    @change="handleChange"
    :disabled="disabled"
    no-data-text="暂无数据"
  >
    <el-option v-for="item in $smartEnumPlugin.getValueDescList('FLAG_NUMBER_ENUM')" :key="item.value" :value="item.value" :label="item.desc">
      {{ item.desc }}
    </el-option>
  </el-select>
</template>

<script setup lang="ts">
  import _ from 'lodash';
  import { ref, watch } from 'vue';

  interface SmartBooleanSelectProps {
    value?: boolean;
    width: number;
    size?: string;
    placeholder?: string;
    disabled?: boolean;
  }

  const props = withDefaults(defineProps<SmartBooleanSelectProps>(), {
    value: undefined,
    width: 100,
    placeholder: '请选择',
    disabled: false,
  });

  const emit = defineEmits<{
    (e: 'update:value', value: any): void;
    (e: 'change', value: any): void;
  }>();

  function convertBoolean2number(value: null | boolean | undefined): null | number {
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

  const handleChange = (value: any) => {
    console.log('boolean enum select', value);
    let booleanResult = null;
    if (!_.isUndefined(value)) {
      booleanResult = value === 1 ? true : false;
    }
    emit('update:value', booleanResult);
    emit('change', booleanResult);
  };
</script>
