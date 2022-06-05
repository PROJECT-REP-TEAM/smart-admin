<!--
 * @Description: 地区级联选择
 * @Author: zhuoda
 * @Date: 2021-08-17
 * @LastEditTime: 2022-06-05 17:57:10
 * @LastEditors: LiHaiFan
-->

<template>
  <div>
    <el-cascader
      :style="`width:${width}`"
      v-model="areaValue"
      :filterable="true"
      :clearable="true"
      :options="areaOptionData"
      :placeholder="placeholder"
      :size="size"
      @change="handleChange"
      :disabled="disabled"
    />
  </div>
</template>

<script setup>
  import { PROVINCE_CITY_DISTRICT } from './province-city-district';
  import { PROVINCE_CITY } from './province-city';
  import { ref, watch } from 'vue';
  import _ from 'lodash';

  // ============ 组件属性 ============

  const TYPE_PROVINCE_CITY_DISTRICT = 'province_city_district';
  const TYPE_PROVINCE_CITY = 'province_city';
  const props = defineProps({
    type: String,
    value: Number,
    width: {
      type: String,
      default: '200px',
    },
    placeholder: {
      type: String,
      default: '请选择地区',
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

  // ============ 组件业务 ============
  const areaOptionData = props.type === TYPE_PROVINCE_CITY_DISTRICT ? PROVINCE_CITY_DISTRICT : PROVINCE_CITY;

  // 绑定地区数据
  const areaValue = ref([]);
  // 监听value变化
  watch(
    () => props.value,
    (newValue) => {
      if (newValue) {
        let array = [];
        for (let index = 0; index < 3; index++) {
          if (newValue[index]) {
            array.push(newValue[index].value);
          }
        }
        areaValue.value = array;
      } else {
        areaValue.value = [];
      }
    }
  );

  function handleChange(value) {
    let resultValue = [];
    if (_.isEmpty(value)) {
      emit('update:value', resultValue);
      emit('change', value, resultValue);
      return;
    }
    // 省
    let province = value[0];
    if (!province || areaOptionData.every((e) => e.value != province)) {
      emit('update:value', resultValue);
      emit('change', value, resultValue);
      return;
    }
    let provinceData = areaOptionData.find((e) => e.value == province);
    resultValue[0] = { value: provinceData.value, label: provinceData.label };
    // 市
    let city = value[1];
    if (!city || provinceData.children.every((e) => e.value != city)) {
      emit('update:value', resultValue);
      emit('change', value, resultValue);
      return;
    }
    let cityData = provinceData.children.find((e) => e.value == city);
    resultValue[1] = { value: cityData.value, label: cityData.label };
    // 区
    let district = value[2];
    if (!district || cityData.children.every((e) => e.value != district)) {
      emit('update:value', resultValue);
      emit('change', value, resultValue);
      return;
    }
    let districtData = cityData.children.find((e) => e.value == district);
    resultValue[2] = { value: districtData.value, label: districtData.label };
    emit('update:value', resultValue);
    emit('change', value, resultValue);
  }
</script>
