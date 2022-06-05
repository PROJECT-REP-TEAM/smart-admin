<!--
 * @Author: zhuoda
 * @Date: 2021-08-16 08:52:38
 * @LastEditTime: 2022-06-06 00:14:55
 * @LastEditors: LiHaiFan
 * @Description:
 * @FilePath: \typescript-element-plus-vue\src\views\system\employee\department\components\operate-department-modal\index.vue
-->
<template>
  <el-dialog v-model="visible" :title="form.departmentId ? '编辑部门' : '添加部门'" :width="600" destroy-on-close>
    <el-form ref="formRef" :model="form" :rules="rules">
      <el-form-item label="上级部门" prop="parentId" v-if="form.parentId != 0">
        <DepartmentTreeSelect ref="departmentTreeSelect" v-model="form.parentId" :defaultValueFlag="false" width="100%" />
      </el-form-item>
      <el-form-item label="部门名称" prop="name">
        <el-input v-model="form.name" placeholder="请输入部门名称" />
      </el-form-item>
      <el-form-item label="部门负责人" prop="managerId">
        <employee-select ref="employeeSelect" placeholder="请选择部门负责人" width="100%" v-model="form.managerId" />
      </el-form-item>
      <el-form-item label="部门排序 （值越大越靠前！）" prop="sort">
        <el-input-number style="width: 100%" v-model="form.sort" :min="0" placeholder="请输入部门名称" />
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="visible = false">取消</el-button>
      <el-button type="primary" @click="handleOk">确定</el-button>
    </template>
  </el-dialog>
</template>
<script setup lang="ts">
  import { nextTick, reactive, ref } from 'vue';
  import { departmentApi } from '/@/api/system/department/department-api';
  import DepartmentTreeSelect from '/@/components/department-tree-select/index.vue';
  import EmployeeSelect from '/@/components/employee-select/index.vue';
  import { employeeApi } from '/@/api/system/employee/employee-api';
  import { useSpinStore } from '/@/store/modules/system/spin';
  import { ElMessage } from 'element-plus';
  import { DepartmentCreateDto } from '/@/api/system/department/model/department-create-dto';
  import { DepartmentUpdateDto } from '/@/api/system/department/model/department-update-dto';
  import { DepartmentVo } from '/@/api/system/department/model/department-vo';
  import { EmployeeVo } from '/@/api/system/employee/model/employee-vo';
  // ----------------------- 以下是字段定义 emits props ---------------------
  const emits = defineEmits<{
    (e: 'reload'): void;
  }>();
  const visible = ref(false);
  const formRef = ref();
  const departmentTreeSelect = ref();
  const defaultOperateParams: DepartmentCreateDto & DepartmentUpdateDto = {
    departmentId: undefined,
    managerId: undefined,
    name: undefined,
    parentId: undefined,
    sort: 0,
  };
  let form = reactive<DepartmentCreateDto & DepartmentUpdateDto>({ ...defaultOperateParams });
  const rules = {
    parentId: [{ required: true, message: '上级部门不能为空' }],
    name: [
      { required: true, message: '部门名称不能为空' },
      { max: 50, message: '部门名称不能大于20个字符', trigger: 'blur' },
    ],
    managerId: [{ required: true, message: '部门负责人不能为空' }],
  };
  let employeeList = ref<EmployeeVo[]>([]);
  // ----------------------- 以下是计算属性 watch监听 ------------------------

  // ----------------------- 以下是生命周期 ---------------------------------

  // ----------------------- 以下是方法 ------------------------------------
  async function showModal(data?: DepartmentVo) {
    await selectEmployee();
    Object.assign(form, defaultOperateParams);
    if (data) {
      Object.assign(form, data);
    }
    visible.value = true;
    await nextTick(() => {
      departmentTreeSelect.value.queryDepartmentTree();
    });
  }

  function closeModal() {
    Object.assign(form, defaultOperateParams);
    visible.value = false;
  }

  async function selectEmployee() {
    try {
      let res = await employeeApi.queryAll();
      employeeList.value = res.data;
    } catch (error) {
      console.log(error);
    }
  }

  async function addDepartment() {
    useSpinStore().show();
    try {
      await departmentApi.addDepartment(form);
      emits('reload');
      closeModal();
    } catch (error) {
      console.log(error);
    } finally {
      useSpinStore().hide();
    }
  }

  async function updateDepartment() {
    useSpinStore().show();
    try {
      if (form.parentId === form.departmentId) {
        ElMessage.warning('上级菜单不能为自己');
        return;
      }
      await departmentApi.updateDepartment(form);
      emits('reload');
      closeModal();
    } catch (error) {
      console.log(error);
    } finally {
      useSpinStore().hide();
    }
  }

  function validateForm(formRef: { validate: () => Promise<any> }) {
    return new Promise((resolve) => {
      formRef
        .validate()
        .then(() => {
          resolve(true);
        })
        .catch(() => {
          resolve(false);
        });
    });
  }

  async function handleOk() {
    let validateFormRes = await validateForm(formRef.value);
    if (!validateFormRes) {
      ElMessage.error('参数验证错误，请仔细填写表单数据!');
      return;
    }
    if (form.departmentId) {
      await updateDepartment();
    } else {
      await addDepartment();
    }
  }

  // ----------------------- 以下是暴露的方法内容 ----------------------------
  defineExpose({
    showModal,
  });
</script>
<style scoped lang="scss"></style>
