<!--
 * @Author: zhuoda
 * @Date: 2021-08-28 10:07:48
 * @LastEditTime: 2021-08-28
 * @LastEditors: zhuoda
 * @Description: 
 * @FilePath: /smart-admin/src/views/system/employee/role/components/operate-role-modal/index.vue
-->
<template>
  <a-modal
    :title="form.id ? '编辑角色' : '添加角色'"
    :width="600"
    :visible="modalVisible"
    @cancel="onClose"
    :footer="null"
  >
    <a-form ref="formRef" :model="form" :rules="rules" :labelCol="{ span: 4 }">
      <a-form-item label="角色名称" name="roleName">
        <a-input
          style="width: 100%"
          placeholder="请输入角色名称"
          v-model:value="form.roleName"
        />
      </a-form-item>
      <a-form-item label="角色备注">
        <a-input
          style="width: 100%"
          placeholder="请输入角色备注"
          v-model:value="form.remark"
        />
      </a-form-item>
    </a-form>

    <div class="footer">
      <a-button style="margin-right: 8px" @click="onClose">取消</a-button>
      <a-button type="primary" @click="operateRule">提交</a-button>
    </div>
  </a-modal>
</template>
<script setup lang="ts">
import { message } from "ant-design-vue";
import { reactive, ref } from "vue";
import { RoleAddDto } from "/@/api/system/role/model/role-add-dto";
import { RoleUpdateDto } from "/@/api/system/role/model/role-update-dto";
import { RoleVo } from "/@/api/system/role/model/role-vo";
import { roleApi } from "/@/api/system/role/role-api";
import { useSpinStore } from "/@/store/modules/system/spin";
// ----------------------- 以下是字段定义 emits props ---------------------
let emits = defineEmits<{
  (e: "reloadList"): void;
}>();
const formRef = ref();
const modalVisible = ref<boolean>(false);
type RoleFormType = RoleAddDto & RoleUpdateDto;
const formDefault: RoleFormType = {
  id: undefined,
  remark: undefined,
  roleName: undefined,
};
let form = reactive<RoleFormType>({ ...formDefault });
const rules = {
  roleName: [{ required: true, message: "请输入角色名称" }],
};
// ----------------------- 以下是计算属性 watch监听 ------------------------

// ----------------------- 以下是生命周期 ---------------------------------

// ----------------------- 以下是方法 ------------------------------------
function showModal(role: RoleVo) {
  Object.assign(form, formDefault);
  if (role) {
    Object.assign(form, role);
  }
  modalVisible.value = true;
}
function onClose() {
  Object.assign(form, formDefault);
  modalVisible.value = false;
}
async function operateRule() {
  formRef.value
    .validate()
    .then(async () => {
      useSpinStore().show();
      try {
        if (form.id) {
          await roleApi.updateRole(form);
        } else {
          await roleApi.addRole(form);
        }
        message.info(`${form.id ? "编辑" : "添加"}成功`);
        emits("reloadList");
        onClose();
      } catch (e) {
        console.log(e);
      } finally {
        useSpinStore().hide();
      }
    })
    .catch((error: any) => {
      console.log("error", error);
      message.error("参数验证错误，请仔细填写表单数据!");
    });
}
// ----------------------- 以下是暴露的方法内容 ----------------------------
defineExpose({
  showModal,
});
</script>
<style scoped lang="less">
.footer {
  width: 100%;
  border-top: 1px solid #e9e9e9;
  padding: 10px 16px;
  background: #fff;
  text-align: right;
  z-index: 1;
}
</style>
