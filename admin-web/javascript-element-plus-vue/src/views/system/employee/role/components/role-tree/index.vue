<!--
 * @Author: zhuoda
 * @Date: 2021-08-28 11:46:46
 * @LastEditTime: 2021-08-30 10:54:12
 * @LastEditors: zhuoda
 * @Description:
 * @FilePath: /smart-admin/src/views/system/employee/role/components/role-tree/index.vue
-->
<template>
  <div>
    <div class='tree-header'>
      <p>设置角色对应的功能操作、后台管理权限</p>
      <el-button v-privilege="'system:role:menu:update'" v-if='selectRoleId' type='primary' @click='saveChange'> 保存
      </el-button>
    </div>
    <!--CheckboxGroup 功能权限勾选部分 start-->
    <RoleTreeCheckbox :tree='tree' />
    <!--CheckboxGroup 功能权限勾选部分 end-->
  </div>
</template>
<script setup>
  import _ from 'lodash';
  import { roleMenuApi } from '/@/api/system/role-menu-api';
  import { useRoleStore } from '/@/store/modules/role';
  import RoleTreeCheckbox from './role-tree-checkbox.vue';
  import { useSpinStore } from '/@/store/modules/spin';
  import { ElMessage } from 'element-plus';
  import { inject, ref, watch } from 'vue';
  // ----------------------- 以下是字段定义 emits props ---------------------
  let roleStore = useRoleStore();
  let tree = ref();
  let selectRoleId = inject('selectRoleId');
  // ----------------------- 以下是计算属性 watch监听 ------------------------
  watch(selectRoleId, () => getRoleSelectedMenu(), {
    immediate: true,
  });
  // ----------------------- 以下是生命周期 ---------------------------------

  // ----------------------- 以下是方法 ------------------------------------
  async function getRoleSelectedMenu() {
    if (!selectRoleId.value) {
      return;
    }
    let res = await roleMenuApi.getRoleSelectedMenu(selectRoleId.value);
    let data = res.data;
    if (_.isEmpty(roleStore.treeMap)) {
      roleStore.initTreeMap(data.menuTreeList || []);
    }
    roleStore.initCheckedData(data.selectedMenuId || []);
    tree.value = data.menuTreeList;
  }

  async function saveChange() {
    let checkedData = roleStore.checkedData;
    if (_.isEmpty(checkedData)) {
      ElMessage.error('还未选择任何权限');
      return;
    }
    let params = {
      roleId: selectRoleId.value,
      menuIdList: checkedData,
    };
    useSpinStore().show();
    try {
      await roleMenuApi.updateRoleMenu(params);
      ElMessage.success('保存成功');
    } catch (error) {
      console.log(error);
    } finally {
      useSpinStore().hide();
    }
  }

  // ----------------------- 以下是暴露的方法内容 ----------------------------
  defineExpose({});
</script>
<style scoped lang='scss'>
  @import './index.scss';
</style>
