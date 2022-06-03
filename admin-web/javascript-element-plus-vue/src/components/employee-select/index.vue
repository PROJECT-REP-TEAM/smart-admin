<!--
 * @Description:
 * @Author: zhuoda
 * @Date: 2021-08-12 18:23:56
 * @LastEditTime: 2021-08-18

 * @LastEditors: zhuoda
-->
<template>
  <el-select
    v-model='selectValue'
    :style='`width: ${width}`'
    :placeholder='props.placeholder'
    :showSearch='true'
    :allowClear='true'
    :size='size'
    @change='handleChange'
    @deselect='handleChange'
  >
    <el-option v-for='item in employeeList' :key='item.employeeId' :value='item.employeeId'
               :label='getEmployeeName(item)'>
      {{ getEmployeeName(item)}}
    </el-option>
  </el-select>
</template>

<script setup>
  import { onMounted, ref, watch } from 'vue';
  import { employeeApi } from '/@/api/system/employee-api';

  // =========== 属性定义 和 事件方法暴露 =============

  const props = defineProps({
    value: Number,
    placeholder: {
      type: String,
      default: '请选择',
    },
    width: {
      type: String,
      default: '100%',
    },
    size: {
      type: String,
      default: 'default',
    },
  });


  const emit = defineEmits(['update:value', 'change']);

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

  // 员工姓名
  function getEmployeeName(item){
    let result = item.actualName;
    if(item.departmentName){
      result+= `（${item.departmentName}）`;
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
    },
  );

  function handleChange(value) {
    emit('update:value', value);
    emit('change', value);
  }
</script>
