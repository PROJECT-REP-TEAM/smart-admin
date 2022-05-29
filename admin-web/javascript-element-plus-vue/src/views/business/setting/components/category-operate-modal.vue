<!--
 * @Description:
 * @version:
 * @Author: zhuoda
 * @Date: 2021-09-01 20:58:51
 * @LastEditors: zhuoda
 * @LastEditTime: 2021-09-01 20:58:52
-->
<template>
  <el-dialog
    v-model='visible'
    :title="form.categoryId ? '编辑' : '添加'"
  >
    <el-form
      ref='formRef'
      :model='form'
      :rules='rules'
    >
      <el-form-item label='分类名称' prop='categoryName'>
        <el-input v-model='form.categoryName' placeholder='请输入分类名称' />
      </el-form-item>
    </el-form>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="onClose">取消</el-button>
        <el-button type="primary" @click="onSubmit">确定</el-button>
      </span>
    </template>
  </el-dialog>
</template>
<script setup>
  import { reactive, ref } from 'vue';
  import { useSpinStore } from '/@/store/modules/spin';
  import _ from 'lodash';
  import { categoryApi } from '/@/api/business/category-api';
  import { ElMessage } from 'element-plus';

  // ----------------------- 以下是字段定义 emits props ------------------------
  // emit
  const emit = defineEmits(['reloadList']);

  //  组件
  const formRef = ref();

  const formDefault = {
    categoryId: undefined,
    categoryName: '',
    categoryType: 1,
    parentId: undefined,
    disabledFlag: false,
  };
  let form = reactive({ ...formDefault });
  const rules = {
    categoryName: [{ required: true, message: '请输入分类名称' }],
  };
  // 是否展示抽屉
  const visible = ref(false);

  // ----------------------- 以下是生命周期 ------------------------

  // ----------------------- 以下是方法 ------------------------
  function showModal(categoryType, parentId, rowData) {
    Object.assign(form, formDefault);
    form.categoryType = categoryType;
    form.parentId = parentId;
    if (rowData && !_.isEmpty(rowData)) {
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
          if (form.categoryId) {
            await categoryApi.updateCategory(form);
          } else {
            await categoryApi.addCategory(form);
          }
          ElMessage.success(`${form.categoryId ? '修改' : '添加'}成功`);
          emit('reloadList', form.parentId);
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
