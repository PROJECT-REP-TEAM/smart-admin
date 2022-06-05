<!--
 * @Description:
 * @Author: zhuoda
 * @Date: 2021-08-12 18:23:56
 * @LastEditTime: 2022-06-05 17:29:13

 * @LastEditors: LiHaiFan
-->
<template>
  <el-select
    v-model="selectValue"
    :style="`width: ${width}`"
    :placeholder="props.placeholder"
    :showSearch="true"
    :allowClear="true"
    :size="size"
    @change="handleChange"
    @deselect="handleChange"
  >
    <el-option v-for="item in employeeList" :key="item.employeeId" :value="item.employeeId" :label="getEmployeeName(item)">
      {{ getEmployeeName(item) }}
    </el-option>
  </el-select>
</template>

<script lang="ts" setup>
  import { ref, watch, onMounted } from 'vue';
  import { ResponseModel } from '/@/api/base-model/response-model';
  import { employeeApi } from '/@/api/system/employee/employee-api';
  import { EmployeeVo } from '/@/api/system/employee/model/employee-vo';

  // =========== 属性定义 和 事件方法暴露 =============

  interface EmployeeSelectProps {
    placeholder?: string;
    value?: number;
    width: string;
    size?: string;
  }

  const props = withDefaults(defineProps<EmployeeSelectProps>(), {
    value: undefined,
    placeholder: '请选择',
    width: '100%',
    size: 'default',
  });

  const emit = defineEmits<{
    (e: 'update:value', value: any): void;
    (e: 'change', value: any): void;
  }>();

  // =========== 业务逻辑 =============

  //员工列表数据
  const employeeList = ref<EmployeeVo[]>([]);

  async function query() {
    try {
      let resp: ResponseModel<EmployeeVo[]> = await employeeApi.queryAll();
      employeeList.value = resp.data;
    } catch (e) {
      console.log(e);
    } finally {
      console.log(1);
    }
  }

  // 员工姓名
  function getEmployeeName(item: EmployeeVo) {
    let result = item.actualName;
    if (item.departmentName) {
      result += `（${item.departmentName}）`;
    }
    return result;
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
    emit('update:value', value);
    emit('change', value);
  }
</script>
