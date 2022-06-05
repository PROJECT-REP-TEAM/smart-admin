<!--
 * @Description: 地区级联选择
 * @Author: zhuoda
 * @Date: 2021-08-17
 * @LastEditTime: 2022-06-05 17:56:10
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

<script lang="ts" setup>
  import { PROVINCE_CITY_DISTRICT } from './province-city-district';
  import { PROVINCE_CITY } from './province-city';
  import { ref, watch } from 'vue';
  import _ from 'lodash';
  import { SmartAreaOption } from './smart-area-option';

  // ============ 组件属性 ============

  const TYPE_PROVINCE_CITY_DISTRICT: string = 'province_city_district';
  const TYPE_PROVINCE_CITY: string = 'province_city';
  interface SmartAreaCascaderProps {
    // 类型
    type: string;
    value: SmartAreaOption[];
    width: string;
    size?: string;
    placeholder?: string;
  }

  const props = withDefaults(defineProps<SmartAreaCascaderProps>(), {
    size: 'default',
    value: undefined,
    width: '200px',
    placeholder: '请选择地区',
  });

  const emit = defineEmits<{
    (e: 'update:value', value: SmartAreaOption[]): void;
    (e: 'change', value: number[], selectedOptions: SmartAreaOption[]): void;
  }>();

  // ============ 组件业务 ============
  const areaOptionData: SmartAreaOption[] = props.type === TYPE_PROVINCE_CITY_DISTRICT ? PROVINCE_CITY_DISTRICT : PROVINCE_CITY;

  // 绑定地区数据
  const areaValue = ref<number[]>([]);
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

  function handleChange(value: number[]): void {
    let resultValue: SmartAreaOption[] = [];
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
    let provinceData: SmartAreaOption | undefined = areaOptionData.find((e) => e.value == province);
    if (!provinceData) {
      emit('update:value', resultValue);
      emit('change', value, resultValue);
      return;
    }
    resultValue[0] = { value: provinceData.value, label: provinceData.label };
    // 市
    let city = value[1];
    provinceData.children = provinceData.children || [];
    if (!city || provinceData.children.every((e) => e.value != city)) {
      emit('update:value', resultValue);
      emit('change', value, resultValue);
      return;
    }
    let cityData: SmartAreaOption | undefined = provinceData.children.find((e) => e.value == city);
    if (!cityData) {
      emit('update:value', resultValue);
      emit('change', value, resultValue);
      return;
    }
    resultValue[1] = { value: cityData.value, label: cityData.label };
    // 区
    let district = value[2];
    cityData.children = cityData.children || [];
    if (!district || cityData.children.every((e) => e.value != district)) {
      emit('update:value', resultValue);
      emit('change', value, resultValue);
      return;
    }
    let districtData: SmartAreaOption | undefined = cityData.children.find((e) => e.value == district);
    if (!districtData) {
      emit('update:value', resultValue);
      emit('change', value, resultValue);
      return;
    }
    resultValue[2] = { value: districtData.value, label: districtData.label };
    emit('update:value', resultValue);
    emit('change', value, resultValue);
  }
</script>
