<!--
 * @Author: zhuoda
 * @Date: 2021-08-28 10:07:48
 * @LastEditTime: 2022-06-06 00:43:12
 * @LastEditors: LiHaiFan
 * @Description:
 * @FilePath: \typescript-element-plus-vue\src\views\system\employee\role\components\operate-role-modal\index.vue
-->
<template>
  <el-dialog :title="form.roleId ? '编辑角色' : '添加角色'" :width="600" v-model="modalVisible" destroy-on-close>
    <el-form ref="formRef" :model="form" :rules="rules">
      <el-form-item label="角色名称" prop="roleName">
        <el-input style="width: 100%" placeholder="请输入角色名称" v-model="form.roleName" />
      </el-form-item>
      <el-form-item label="角色备注">
        <el-input style="width: 100%" placeholder="请输入角色备注" v-model="form.remark" />
      </el-form-item>
    </el-form>

    <template #footer>
      <el-button style="margin-right: 8px" @click="onClose">取消</el-button>
      <el-button type="primary" @click="operateRule">提交</el-button>
    </template>
  </el-dialog>
</template>
<script setup lang="ts">
  import { reactive, ref } from 'vue';
  import { roleApi } from '/@/api/system/role/role-api';
  import { useSpinStore } from '/@/store/modules/system/spin';
  import { ElMessage } from 'element-plus';
  import { RoleAddDto } from '/@/api/system/role/model/role-add-dto';
  import { RoleUpdateDto } from '/@/api/system/role/model/role-update-dto';
  import { RoleVo } from '/@/api/system/role/model/role-vo';
  // ----------------------- 以下是字段定义 emits props ---------------------
  let emits = defineEmits<{
    (e: 'reloadList'): void;
  }>();
  const formRef = ref();
  const modalVisible = ref(false);
  type RoleFormType = RoleAddDto & RoleUpdateDto;
  const formDefault: RoleFormType = {
    roleId: undefined,
    remark: undefined,
    roleName: undefined,
  };
  let form = reactive<RoleFormType>({ ...formDefault });
  const rules = {
    roleName: [{ required: true, message: '请输入角色名称' }],
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
          if (form.roleId) {
            await roleApi.updateRole(form);
          } else {
            await roleApi.addRole(form);
          }
          ElMessage.info(`${form.roleId ? '编辑' : '添加'}成功`);
          emits('reloadList');
          onClose();
        } catch (e) {
          console.log(e);
        } finally {
          useSpinStore().hide();
        }
      })
      .catch((error:any) => {
        console.log('error', error);
        ElMessage.error('参数验证错误，请仔细填写表单数据!');
      });
  }

  // ----------------------- 以下是暴露的方法内容 ----------------------------
  defineExpose({
    showModal,
  });
</script>
<style scoped lang="scss">
  .footer {
    width: 100%;
    border-top: 1px solid #e9e9e9;
    padding: 10px 16px;
    background: #fff;
    text-align: right;
    z-index: 1;
  }
</style>
