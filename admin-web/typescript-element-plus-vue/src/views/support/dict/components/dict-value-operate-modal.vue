<template>
  <el-drawer v-model="visible" :title="form.dictValueId ? '编辑' : '添加'" @close="onClose">
    <el-form ref="formRef" :model="form" :rules="rules">
      <el-form-item label="编码" prop="valueCode">
        <el-input v-model="form.valueCode" placeholder="请输入编码" />
      </el-form-item>
      <el-form-item label="名称" prop="valueName">
        <el-input v-model="form.valueName" placeholder="请输入名称" />
      </el-form-item>
      <el-form-item label="排序" prop="sort">
        <el-input-number v-model="form.sort" :min="0" :max="1000" />
      </el-form-item>
      <el-form-item label="备注" prop="remark">
        <textarea v-model="form.remark" style="width: 100%; height: 100px; outline: none"></textarea>
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button style="margin-right: 8px" @click="onClose">取消</el-button>
      <el-button type="primary" @click="onSubmit">提交</el-button>
    </template>
  </el-drawer>
</template>
<script setup lang="ts">
  import { ref, reactive } from 'vue';
  import { useSpinStore } from '/@/store/modules/system/spin';
  import { dictApi } from '/@/api/support/dict/dict-api';
  import { ElMessage } from 'element-plus';
  import { DictValueAddForm } from '/@/api/support/dict/model/dict-value-add-form';
  import { DictValueUpdateForm } from '/@/api/support/dict/model/dict-value-update-form';

  // ----------------------- 以下是字段定义 emits props ------------------------
  // emit
  const emit = defineEmits<{
    (e: 'reloadList', parentId?: number): void;
  }>();

  //  组件
  const formRef = ref();

  const formDefault: DictValueAddForm & DictValueUpdateForm = {
    dictValueId: undefined,
    dictKeyId: undefined,
    sort: 1,
    valueCode: '',
    valueName: '',
    remark: '',
  };
  let form = reactive<DictValueAddForm & DictValueUpdateForm>({ ...formDefault });
  const rules = {
    valueCode: [{ required: true, message: '请输入编码' }],
    valueName: [{ required: true, message: '请输入名称' }],
    sort: [{ required: true, message: '请输入排序' }],
  };
  // 是否展示
  const visible = ref(false);

  // ----------------------- 以下是生命周期 ------------------------

  // ----------------------- 以下是方法 ------------------------
  function showModal(rowData?: DictValueUpdateForm, dictKeyId?: number) {
    Object.assign(form, formDefault);
    if (rowData) {
      Object.assign(form, rowData);
    }
    form.dictKeyId = dictKeyId;
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
          if (form.dictValueId) {
            await dictApi.valueEdit(form);
          } else {
            await dictApi.valueAdd(form);
          }
          ElMessage.success(`${form.dictKeyId ? '修改' : '添加'}成功`);
          emit('reloadList');
          onClose();
        } catch (error) {
          console.log(error);
        } finally {
          useSpinStore().hide();
        }
      })
      .catch((error: any) => {
        console.log('error', error);
        ElMessage.error('参数验证错误，请仔细填写表单数据!');
      });
  }

  // ----------------------- 以下是暴露的方法内容 ------------------------
  defineExpose({
    showModal,
  });
</script>
