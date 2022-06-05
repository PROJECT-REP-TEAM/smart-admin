<!--
 * @Author: zhuoda
 * @Date: 2021-08-26 10:36:54
 * @LastEditTime: 2022-06-05 22:59:13
 * @LastEditors: LiHaiFan
 * @Description:
 * @FilePath: \typescript-element-plus-vue\src\views\system\employee\role\components\role-list\index.vue
-->
<template>
  <el-card title="角色列表" class="role-container" style="padding: 0">
    <template #header>
      <el-button v-privilege="'system:role:add'" type="primary" size="small" @click="showOperateRoleModal">添加 </el-button>
    </template>
    <el-menu :default-active="selectedKey" @select="selectRole">
      <template v-for="item in roleList" :key="item.roleId">
        <el-menu-item :index="`${item.roleId}`">
          <el-popover placement="right" trigger="hover">
            <div class="btn-group">
              <div class="btn" v-privilege="'system:role:delete'">
                <el-button link type="primary" @click="deleteRole(item.roleId)">删除 </el-button>
              </div>
              <div class="btn" v-privilege="'system:role:update'">
                <el-button link type="primary" @click="showOperateRoleModal(item)">编辑 </el-button>
              </div>
            </div>
            <template #reference>
              <p>{{ item.roleName }}</p>
            </template>
          </el-popover>
        </el-menu-item>
      </template>
    </el-menu>
  </el-card>
  <OperateRoleModal ref="operateRoleModal" @reloadList="queryAllRole" />
</template>
<script setup lang="ts">
  import { computed, nextTick, ref } from 'vue';
  import { roleApi } from '/@/api/system/role/role-api';
  import OperateRoleModal from '../operate-role-modal/index.vue';
  import { useSpinStore } from '/@/store/modules/system/spin';
  import _ from 'lodash';
  import { ElMessage, ElMessageBox } from 'element-plus';
  import { RoleVo } from '/@/api/system/role/model/role-vo';

  // ----------------------- 以下是字段定义 emits props ---------------------
  const roleList = ref<RoleVo[]>([]);
  const operateRoleModal = ref();
  let selectedKey = ref();
  // ----------------------- 以下是计算属性 watch监听 ------------------------
  const selectRoleId = computed(() => {
    if (!selectedKey.value) {
      return null;
    }
    return selectedKey.value;
  });
  // ----------------------- 以下是生命周期 ---------------------------------
  queryAllRole();

  // ----------------------- 以下是方法 ------------------------------------
  function selectRole(e: number) {
    selectedKey.value = e;
  }

  async function queryAllRole() {
    let res = await roleApi.queryAll();
    roleList.value = res.data;
    await nextTick(() => {
      if (!_.isEmpty(res.data) && res.data[0].roleId) {
        selectedKey.value = res.data[0].roleId.toString();
      }
    });
  }

  function showOperateRoleModal(role: RoleVo) {
    operateRoleModal.value.showModal(role);
  }

  function deleteRole(roleId?: number) {
    if (!roleId) {
      return;
    }
    ElMessageBox.confirm('确定要删除该角色么？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
    }).then(async () => {
      useSpinStore().show();
      try {
        await roleApi.deleteRole(roleId);
        ElMessage.info('删除成功');
        await queryAllRole();
      } catch (e) {
        console.log(e);
      } finally {
        useSpinStore().hide();
      }
    });
  }

  // ----------------------- 以下是暴露的方法内容 ----------------------------
  defineExpose({
    selectRoleId,
  });
</script>
<style scoped lang="scss">
  .role-container {
    height: 100%;
    overflow-y: auto;

    :deep(.ant-card-body) {
      padding: 5px;
    }
  }

  .btn-group {
    display: flex;
    flex-direction: column;
    width: 100%;

    .btn {
      width: 100%;
      display: flex;
      align-items: center;
      justify-content: center;
    }
  }
</style>
