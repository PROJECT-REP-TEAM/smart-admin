<!--
 * @Description: 校区部门选择
 * @Author: zhuoda
 * @Date: 2021-08-12 18:23:56
 * @LastEditTime: 2021-08-25 11:48:26
 * @LastEditors: Please set LastEditors
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
    <a-select-option v-for="item in list" :key="item.id" :value="item.id">
      {{ item.name }}
    </a-select-option>
  </a-select>
</template>

<script lang="ts" setup>
import { ref, watch, onMounted } from "vue";
import { ResponseModel } from "/@/api/base-model/response-model";
import { departmentApi } from "/@/api/system/department/department-api";
import { DepartmentVo } from "/@/api/system/department/model/department-vo";

// =========== 属性定义 和 事件方法暴露 =============
interface ShoolDepartmentSelectProps {
  placeholder?: string;
  value?: number;
  width: string;
  size?: string;
  showFirst?:boolean;
}

const props = withDefaults(defineProps<ShoolDepartmentSelectProps>(), {
  value: undefined,
  placeholder: "请选择",
  width: "100%",
  size: "default",
  showFirst:false,
});

const emit = defineEmits<{
  (e: "update:value", value: any): void;
  (e: "change", value: any): void;
}>();

// =========== 业务逻辑 =============

//列表数据
const list = ref<DepartmentVo[]>([]);
async function query() {
  try {
    let resp: ResponseModel<
      DepartmentVo[]
    > = await departmentApi.querySchoolDepartmentList();
    list.value = resp.data;
    if(props.showFirst){
      showFirst()
    }
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

//展示第一个
function showFirst(){
  if(list.value && list.value.length > 0){
    selectValue.value = list.value[0].id;
    handleChange(selectValue.value )
  }
}
</script>
