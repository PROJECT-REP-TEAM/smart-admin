<!--
 * @Author: zhuoda
 * @Date: 2021-08-16 15:06:33
 * @LastEditTime: 2022-06-06 00:36:42
 * @LastEditors: LiHaiFan
 * @Description:
 * @FilePath: \typescript-element-plus-vue\src\views\system\employee\department\components\operate-employee-modal\index.vue
-->
<template>
  <el-dialog :title="form.employeeId ? '编辑' : '添加'" :width="600" v-model="visible" @close="onClose" destroy-on-close>
    <el-form ref="formRef" :model="form" :rules="rules" layout="vertical" label-width="80px">
      <el-form-item label="姓名" prop="actualName">
        <el-input v-model="form.actualName" placeholder="请输入姓名" />
      </el-form-item>
      <el-form-item label="手机号" prop="phone">
        <el-input v-model="form.phone" placeholder="请输入手机号" />
      </el-form-item>
      <el-form-item label="部门" prop="departmentId">
        <DepartmentTreeSelect ref="departmentTreeSelect" :init="false" v-model="form.departmentId" />
      </el-form-item>
      <el-form-item label="登录名" prop="loginName">
        <el-input v-model="form.loginName" placeholder="请输入登录名" />
        <p class="hint">初始密码默认为：123456</p>
      </el-form-item>
      <el-form-item label="性别" prop="gender">
        <smart-enum-select style="width: 100%" v-model:value="form.gender" placeholder="请选择性别" enum-name="GENDER_ENUM" />
      </el-form-item>
      <el-form-item label="状态" prop="disabledFlag">
        <el-select v-model="form.disabledFlag" placeholder="请选择状态" style="width: 100%">
          <el-option :value="0" label="启用">启用</el-option>
          <el-option :value="1" label="禁用">禁用</el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="角色" prop="roleIdList">
        <el-select multiple v-model="form.roleIdList" placeholder="请选择角色" style="width: 100%">
          <el-option v-for="item in roleList" :key="item.roleId" :value="item.roleId" :label="item.roleName">{{ item.roleName }} </el-option>
        </el-select>
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button style="margin-right: 8px" @click="onClose">取消</el-button>
      <el-button v-if="!form.employeeId" type="primary" style="margin-right: 8px" @click="onSubmit(true)">保存并继续添加 </el-button>
      <el-button type="primary" @click="onSubmit(false)">提交</el-button>
    </template>
  </el-dialog>
</template>
<script setup lang="ts">
  import _ from 'lodash';
  import { nextTick, reactive, ref } from 'vue';
  import { employeeApi } from '/@/api/system/employee/employee-api';
  import { useSpinStore } from '/@/store/modules/system/spin';
  import { regular } from '/@/constants/regular';
  import DepartmentTreeSelect from '/@/components/department-tree-select/index.vue';
  import { roleApi } from '/@/api/system/role/role-api';
  import SmartEnumSelect from '/@/components/smart-enum-select/index.vue';
  import { GENDER_ENUM } from '/@/constants/common';
  import { ElMessage } from 'element-plus';
  import { EmployeeAddDto } from '/@/api/system/employee/model/employee-add-dto';
  import { EmployeeUpdateDto } from '/@/api/system/employee/model/employee-update-dto';
  import { RoleVo } from '/@/api/system/role/model/role-vo';
  // ----------------------- 以下是字段定义 emits props ---------------------
  const departmentTreeSelect = ref();
  // emit
  const emit = defineEmits<{
    (e: 'reloadList'): void;
  }>();

  // 组件ref
  const formRef = ref();
  const roleList = ref<RoleVo[]>([]);
  const formDefault: EmployeeUpdateDto & EmployeeAddDto = {
    employeeId: undefined,
    actualName: undefined,
    departmentId: undefined,
    disabledFlag: 0,
    gender: GENDER_ENUM.MAN.value,
    loginName: undefined,
    phone: undefined,
    roleIdList: undefined,
  };
  let form = reactive<EmployeeUpdateDto & EmployeeAddDto>(_.cloneDeep(formDefault));
  const rules = {
    actualName: [
      { required: true, message: '姓名不能为空' },
      { max: 30, message: '姓名不能大于30个字符', trigger: 'blur' },
    ],
    phone: [
      { required: true, message: '手机号不能为空' },
      { pattern: regular.phone, message: '请输入正确的手机号码', trigger: 'blur' },
    ],
    loginName: [
      { required: true, message: '登录账号不能为空' },
      { max: 30, message: '登录账号不能大于30个字符', trigger: 'blur' },
    ],
    gender: [{ required: true, message: '性别不能为空' }],
    departmentId: [{ required: true, message: '部门不能为空' }],
    disabledFlag: [{ required: true, message: '状态不能为空' }],
  };
  // 是否展示抽屉
  const visible = ref(false);
  // ----------------------- 以下是计算属性 watch监听 ------------------------

  // ----------------------- 以下是生命周期 ---------------------------------

  // ----------------------- 以下是方法 ------------------------------------

  async function queryAllRole() {
    let res = await roleApi.queryAll();
    roleList.value = res.data;
  }

  async function showDrawer(rowData?: EmployeeUpdateDto) {
    Object.assign(form, formDefault);
    if (rowData && !_.isEmpty(rowData)) {
      Object.assign(form, rowData);
    }
    visible.value = true;
    await nextTick(() => {
      queryAllRole();
      departmentTreeSelect.value.queryDepartmentTree();
    });
  }

  function reset() {
    Object.assign(form, formDefault);
    formRef.value.resetFields();
  }

  function onClose() {
    reset();
    visible.value = false;
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

  async function onSubmit(keepAdding: any) {
    let validateFormRes = await validateForm(formRef.value);
    if (!validateFormRes) {
      ElMessage.error('参数验证错误，请仔细填写表单数据!');
      return;
    }
    useSpinStore().show();
    try {
      if (form.employeeId) {
        await employeeApi.updateEmployee(form);
      } else {
        await employeeApi.addEmployee(form);
      }
      ElMessage.success(`${form.employeeId ? '修改' : '添加'}成功`);
      if (keepAdding) {
        reset();
      } else {
        onClose();
      }
      emit('reloadList');
    } catch (error) {
      console.log(error);
    } finally {
      useSpinStore().hide();
    }
  }

  // ----------------------- 以下是暴露的方法内容 ----------------------------
  defineExpose({
    showDrawer,
  });
</script>
