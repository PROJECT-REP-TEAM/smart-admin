<!--
 * @Author: zhuoda
 * @Date: 2021-08-16 08:52:38
 * @LastEditTime: 2022-06-02
 * @LastEditors: zhuoda
 * @Description:
 * @FilePath: /smart-admin/src/views/system/employee/department/components/operate-department-modal/index.vue
-->
<template>
  <a-modal
    v-model:visible="visible"
    :title="form.id ? '编辑部门' : '添加部门'"
    @ok="handleOk"
  >
    <a-form ref="formRef" :model="form" :rules="rules" layout="vertical">
      <a-form-item label="上级部门" name="parentId" v-if="form.parentId != 0">
        <DepartmentTreeSelect
          ref="departmentTreeSelect"
          v-model:value="form.parentId"
          :init="false"
        />
      </a-form-item>
      <a-form-item label="部门名称" name="name">
        <a-input v-model:value="form.name" placeholder="请输入部门名称" />
      </a-form-item>
      <a-form-item label="部门负责人" name="managerId">
        <a-select
          v-model:value="form.managerId"
          show-search
          placeholder="请选择部门负责人"
          optionFilterProp="title"
        >
          <a-select-option
            v-for="item in employeeList"
            :key="item.id"
            :title="item.actualName"
            >{{ item.actualName }}</a-select-option
          >
        </a-select>
      </a-form-item>
    </a-form>
  </a-modal>
</template>
<script setup>
import { nextTick, reactive, ref } from "vue";
import { departmentApi } from "/@/api/system/department/department-api";
import DepartmentTreeSelect from "/@/components/department-tree-select/index.vue";
import { employeeApi } from "/@/api/system/employee/employee-api";
import message from "ant-design-vue/lib/message";
import { useSpinStore } from "/@/store/modules/system/spin";
// ----------------------- 以下是字段定义 emits props ---------------------
const emits = defineEmits("reload");
const visible = ref(false);
const formRef = ref();
const departmentTreeSelect = ref();
const defaultOperateParams = {
  id: undefined,
  managerId: undefined,
  name: undefined,
  parentId: undefined,
  shortName: undefined,
};
let form = reactive({ ...defaultOperateParams });
const rules = {
  parentId: [{ required: true, message: "上级部门不能为空" }],
  name: [
    { required: true, message: "部门名称不能为空" },
    { max: 50, message: "部门名称不能大于20个字符", trigger: "blur" },
  ],
  managerId: [{ required: true, message: "部门负责人不能为空" }],
};
let employeeList = ref([]);
// ----------------------- 以下是计算属性 watch监听 ------------------------

// ----------------------- 以下是生命周期 ---------------------------------

// ----------------------- 以下是方法 ------------------------------------
async function showModal(data) {
  await selectEmployee();
  Object.assign(form, defaultOperateParams);
  if (data) {
    Object.assign(form, data);
  }
  visible.value = true;
  nextTick(() => {
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
    emits("reload");
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
    if (form.parentId == form.id) {
      message.warning("上级菜单不能为自己");
      return;
    }
    await departmentApi.updateDepartment(form);
    emits("reload");
    closeModal();
  } catch (error) {
    console.log(error);
  } finally {
    useSpinStore().hide();
  }
}
function validateForm(formRef) {
  return (
    new Promise() <
    boolean >
    ((resolve) => {
      formRef
        .validate()
        .then(() => {
          resolve(true);
        })
        .catch(() => {
          resolve(false);
        });
    })
  );
}
async function handleOk() {
  let validateFormRes = await validateForm(formRef.value);
  if (!validateFormRes) {
    message.error("参数验证错误，请仔细填写表单数据!");
    return;
  }
  if (form.id) {
    updateDepartment();
  } else {
    addDepartment();
  }
}
// ----------------------- 以下是暴露的方法内容 ----------------------------
defineExpose({
  showModal,
});
</script>
<style scoped lang="less"></style>
