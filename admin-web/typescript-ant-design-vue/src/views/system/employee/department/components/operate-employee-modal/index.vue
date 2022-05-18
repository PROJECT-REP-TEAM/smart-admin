<!--
 * @Author: zhuoda
 * @Date: 2021-08-16 15:06:33
 * @LastEditTime: 2021-09-01
 * @LastEditors: zhuoda
 * @Description:
 * @FilePath: /smart-admin/src/views/system/employee/department/components/operate-employee-modal/index.vue
-->
<template>
  <a-drawer
    :maskClosable="false"
    :title="form.id ? '编辑' : '添加'"
    :width="600"
    :visible="visible"
    :body-style="{ paddingBottom: '80px' }"
    @close="onClose"
  >
    <a-form v-if="visible" ref="formRef" :model="form" :rules="rules" layout="vertical">
      <a-form-item label="姓名" name="actualName">
        <a-input v-model:value="form.actualName" placeholder="请输入姓名" />
      </a-form-item>
      <a-form-item label="手机号" name="phone">
        <a-input v-model:value="form.phone" placeholder="请输入手机号" />
      </a-form-item>
      <a-form-item label="部门" name="departmentId">
        <DepartmentTreeSelect
          ref="departmentTreeSelect"
          :init="false"
          v-model:value="form.departmentId"
        />
      </a-form-item>
      <a-form-item label="登录名" name="loginName">
        <a-input v-model:value="form.loginName" placeholder="请输入登录名" />
        <p class="hint">初始密码默认为：123456</p>
      </a-form-item>
      <a-form-item label="性别" name="gender">
        <smart-enum-select
          style="width: 100%"
          v-model:value="form.gender"
          placeholder="请选择性别"
          enum-name="GENDER_ENUM"
        />
      </a-form-item>
      <a-form-item label="状态" name="disabledFlag">
        <a-select v-model:value="form.disabledFlag" placeholder="请选择状态">
          <a-select-option :value="0">启用</a-select-option>
          <a-select-option :value="1">禁用</a-select-option>
        </a-select>
      </a-form-item>
      <a-form-item label="角色" name="roleIdList">
        <a-select
          mode="multiple"
          v-model:value="form.roleIdList"
          optionFilterProp="title"
          placeholder="请选择角色"
        >
          <a-select-option
            v-for="item in roleList"
            :key="item.id"
            :title="item.roleName"
            >{{ item.roleName }}</a-select-option
          >
        </a-select>
      </a-form-item>
    </a-form>
    <div class="footer">
      <a-button style="margin-right: 8px" @click="onClose">取消</a-button>
      <a-button
        v-if="!form.id"
        type="primary"
        style="margin-right: 8px"
        @click="onSubmit(true)"
        >保存并继续添加</a-button
      >
      <a-button type="primary" @click="onSubmit(false)">提交</a-button>
    </div>
  </a-drawer>
</template>
<script setup lang="ts">
import { message } from "ant-design-vue";
import _ from "lodash";
import { nextTick, reactive, ref } from "vue";
import { employeeApi } from "/@/api/system/employee/employee-api";
import { EmployeeAddDto } from "/@/api/system/employee/model/employee-add-dto";
import { EmployeeUpdateDto } from "/@/api/system/employee/model/employee-update-dto";
import { useSpinStore } from "/@/store/modules/system/spin";
import { regular } from "/@/constants/regular";
import DepartmentTreeSelect from "/@/components/department-tree-select/index.vue";
import { roleApi } from "/@/api/system/role/role-api";
import { ResponseModel } from "/@/api/base-model/response-model";
import { RoleVo } from "/@/api/system/role/model/role-vo";
import SmartEnumSelect from "/@/components/smart-enum-select/index.vue";
import { GENDER_ENUM } from "/@/constants/common";
// ----------------------- 以下是字段定义 emits props ---------------------
const departmentTreeSelect = ref();
// emit
const emit = defineEmits<{
  (e: "reloadList"): void;
}>();

// 组件ref
const formRef = ref();
const roleList = ref<RoleVo[]>([]);
const formDefault: EmployeeUpdateDto & EmployeeAddDto = {
  id: undefined,
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
    { required: true, message: "姓名不能为空" },
    { max: 30, message: "姓名不能大于30个字符", trigger: "blur" },
  ],
  phone: [
    { required: true, message: "手机号不能为空" },
    { pattern: regular.phone, message: "请输入正确的手机号码", trigger: "blur" },
  ],
  loginName: [
    { required: true, message: "登录账号不能为空" },
    { max: 30, message: "登录账号不能大于30个字符", trigger: "blur" },
  ],
  gender: [{ required: true, message: "性别不能为空" }],
  departmentId: [{ required: true, message: "部门不能为空" }],
  disabledFlag: [{ required: true, message: "状态不能为空" }],
};
// 是否展示抽屉
const visible = ref(false);
// ----------------------- 以下是计算属性 watch监听 ------------------------

// ----------------------- 以下是生命周期 ---------------------------------

// ----------------------- 以下是方法 ------------------------------------

async function queryAllRole() {
  let res: ResponseModel<RoleVo[]> = await roleApi.queryAll();
  roleList.value = res.data;
}

async function showDrawer(rowData?: EmployeeUpdateDto) {
  Object.assign(form, formDefault);
  if (rowData && !_.isEmpty(rowData)) {
    Object.assign(form, rowData);
  }
  visible.value = true;
  nextTick(() => {
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
  return new Promise<boolean>((resolve) => {
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

async function onSubmit(keepAdding: boolean) {
  let validateFormRes = await validateForm(formRef.value);
  if (!validateFormRes) {
    message.error("参数验证错误，请仔细填写表单数据!");
    return;
  }
  useSpinStore().show();
  try {
    if (form.id) {
      await employeeApi.updateEmployee(form);
    } else {
      await employeeApi.addEmployee(form);
    }
    message.success(`${form.id ? "修改" : "添加"}成功`);
    if (keepAdding) {
      reset();
    } else {
      onClose();
    }
    emit("reloadList");
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
<style scoped lang="less">
.footer {
  position: absolute;
  right: 0;
  bottom: 0;
  width: 100%;
  border-top: 1px solid #e9e9e9;
  padding: 10px 16px;
  background: #fff;
  text-align: right;
  z-index: 1;
}
.hint {
  margin-top: 5px;
  color: #bfbfbf;
}
</style>
