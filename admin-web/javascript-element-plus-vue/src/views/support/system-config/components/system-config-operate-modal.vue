<!--
 * @Description:
 * @version:
 * @Author: zhuoda
 * @Date: 2021-09-01 20:58:51
 * @LastEditors: zhuoda
 * @LastEditTime: 2021-09-01 20:58:52
-->
<template>
  <el-drawer
    v-model='visible'
    :title="form.systemConfigId ? '编辑' : '添加'"
    @close='onClose'
  >
    <el-form
      ref='formRef'
      :model='form'
      :rules='rules'
    >
      <el-form-item label='参数Key' prop='configKey'>
        <el-input v-model='form.configKey' placeholder='请输入参数Key' />
      </el-form-item>
      <el-form-item label='参数名称' prop='configName'>
        <el-input v-model='form.configName' placeholder='请输入参数名称' />
      </el-form-item>
      <el-form-item label='参数值' prop='configValue'>
        <el-input v-model='form.configValue' placeholder='请输入参数值' />
      </el-form-item>
      <el-form-item label='备注' name='remark'>
         <textarea
           v-model='form.remark'
           style='width: 100%; height: 100px; outline: none'
         ></textarea>
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button style='margin-right: 8px' @click='onClose'>取消</el-button>
      <el-button type='primary' @click='onSubmit'>提交</el-button>
    </template>
  </el-drawer>
</template>
<script setup>
  import { ref, reactive } from 'vue';
  import { useSpinStore } from '/@/store/modules/spin';
  import { systemConfigApi } from '/@/api/support/system-config-api';
  import { ElMessage } from 'element-plus';

  // ----------------------- 以下是字段定义 emits props ------------------------
  // emit
  const emit = defineEmits(['reloadList']);

  //  组件
  const formRef = ref();

  const formDefault = {
    systemConfigId: undefined,
    configKey: '',
    configName: '',
    configValue: '',
    remark: '',
  };
  let form = reactive({ ...formDefault });
  const rules = {
    configKey: [{ required: true, message: '请输入参数key' }],
    configName: [{ required: true, message: '请输入参数名称' }],
    configValue: [{ required: true, message: '请输入参数值' }],
  };
  // 是否展示
  const visible = ref(false);

  // ----------------------- 以下是生命周期 ------------------------

  // ----------------------- 以下是方法 ------------------------
  function showModal(rowData) {
    Object.assign(form, formDefault);
    if (rowData) {
      Object.assign(form, rowData);
    }
    visible.value = true;
  }

  function onClose() {
    Object.assign(form, formDefault);
    visible.value = false;
  }

  function onSubmit() {
    formRef.value
      .validate()
      .then(async () => {
        useSpinStore().show();
        try {
          if (form.systemConfigId) {
            await systemConfigApi.updateSystemConfig(form);
          } else {
            await systemConfigApi.addSystemConfig(form);
          }
          ElMessage.success(`${form.systemConfigId ? '修改' : '添加'}成功`);
          emit('reloadList');
          onClose();
        } catch (error) {
          console.log(error);
        } finally {
          useSpinStore().hide();
        }
      })
      .catch((error) => {
        console.log('error', error);
        ElMessage.error('参数验证错误，请仔细填写表单数据!');
      });
  }

  // ----------------------- 以下是暴露的方法内容 ------------------------
  defineExpose({
    showModal,
  });
</script>
