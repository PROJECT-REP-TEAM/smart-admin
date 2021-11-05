<!--
 * @Description: 地区级联选择
 * @Author: zhuoda
 * @Date: 2021-08-17
 * @LastEditTime: 2021-08-18
 * @LastEditors: zhuoda
-->

<template>
  <a-cascader
    :style="`width:${width}`"
    v-model:value="areaValue"
    :show-search="{ filter }"
    :options="areaOptionData"
    :placeholder="placeholder"
    :size="size"
    @change="handleChange"
  />
</template>

<script lang="ts" setup>
import { PROVINCE_CITY_DISTRICT } from "./province-city-district";
import { PROVINCE_CITY } from "./province-city";
import { ref, toRaw, watch } from "vue";

// ============ 组件属性 ============

const TYPE_PROVINCE_CITY_DISTRICT = "province_city_district";
const TYPE_PROVINCE_CITY = "province_city";
import { SmartAreaOption } from "./smart-area-option";

interface SmartAreaCascaderProps {
  // 类型
  type: string;
  value: SmartAreaOption[];
  width: string;
  size?: string;
  placeholder?: string;
}

const props = withDefaults(defineProps<SmartAreaCascaderProps>(), {
  size: "default",
  value: undefined,
  width: "200px",
  placeholder: "请选择地区",
});

const emit = defineEmits<{
  (e: "update:value", value);
  (e: "change", value, selectedOptions);
}>();

// ============ 组件业务 ============
const areaOptionData =
  props.type === TYPE_PROVINCE_CITY_DISTRICT ? PROVINCE_CITY_DISTRICT : PROVINCE_CITY;

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

function handleChange(value: number[], selectedOptions: SmartAreaOption[]): void {
  emit("update:value", toRaw(selectedOptions));
  emit("change", value, toRaw(selectedOptions));
}

const filter = (inputValue: string, path: SmartAreaOption[]) => {
  return path.some(
    (option) => option.label.toLowerCase().indexOf(inputValue.toLowerCase()) > -1
  );
};
</script>
