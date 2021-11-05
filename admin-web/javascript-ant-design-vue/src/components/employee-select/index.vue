<!--
 * @Description:
 * @Author: zhuoda
 * @Date: 2021-08-12 18:23:56
 * @LastEditTime: 2021-08-18
 * @LastEditors: zhuoda
-->
<template>
  <a-select
    v-model:value="selectValue"
    :style="`width: ${width}`"
    :placeholder="props.placeholder"
    :showSearch="true"
    :allowClear="true"
    :size="size"
    @change="handleChange"
    @deselect="handleChange"
  >
    <a-select-option v-for="item in employeeList" :key="item.id" :value="item.id">
      {{ item.actualName }}（{{ item.departmentName }}）
    </a-select-option>
  </a-select>
</template>

<script lang="ts" setup>
import { ref, watch, onMounted } from "vue";
import { employeeApi } from "/@/api/system/employee/employee-api";

// =========== 属性定义 和 事件方法暴露 =============
interface EmployeeSelectProps {
  placeholder?: string;
  value?: number;
  width: string;
  size?: string;
}

const props = withDefaults(defineProps<EmployeeSelectProps>(), {
  value: undefined,
  placeholder: "请选择",
  width: "100%",
  size: "default",
});

const emit = defineEmits<{
  (e: "update:value", value: any): void;
  (e: "change", value: any): void;
}>();

// =========== 业务逻辑 =============

//员工列表数据
const employeeList = ref([]);
async function query() {
  try {
    let resp = await employeeApi.queryAll();
    employeeList.value = resp.data;
  } catch (e) {
    console.log(e);
  } finally {
    console.log(1);
  }
}
onMounted(query);

// 监听value变化
const selectValue = ref(props.value);
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
