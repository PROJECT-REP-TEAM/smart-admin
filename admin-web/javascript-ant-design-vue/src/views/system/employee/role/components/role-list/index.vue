<!--
 * @Author: zhuoda
 * @Date: 2021-08-26 10:36:54
 * @LastEditTime: 2022-06-02
 * @LastEditors: zhuoda
 * @Description:
 * @FilePath: /smart-admin/src/views/system/employee/role/components/role-list/index.vue
-->
<template>
  <a-card title="角色列表" class="role-container" style="padding: 0">
    <template #extra>
      <a-button type="primary" size="small" @click="showOperateRoleModal">添加</a-button>
    </template>
    <a-menu mode="vertical" v-model:selectedKeys="selectedKeys">
      <a-menu-item v-for="item in roleList" :key="item.id">
        <a-popover placement="right">
          <template #content>
            <div style="display: flex; flex-direction: column">
              <a-button type="text" @click="deleteRole(item.id)">删除</a-button>
              <a-button type="text" @click="showOperateRoleModal(item)">编辑</a-button>
            </div>
          </template>
          {{ item.roleName }}
        </a-popover>
      </a-menu-item>
    </a-menu>
  </a-card>
  <OperateRoleModal ref="operateRoleModal" @reloadList="queryAllRole" />
</template>
<script setup>
import { computed, ref } from "vue";
import { roleApi } from "/@/api/system/role/role-api";
import OperateRoleModal from "../operate-role-modal/index.vue";
import { message, Modal } from "ant-design-vue";
import { useSpinStore } from "/@/store/modules/system/spin";
import _ from "lodash";

// ----------------------- 以下是字段定义 emits props ---------------------
const roleList = ref([]);
const operateRoleModal = ref();
let selectedKeys = ref([]);
// ----------------------- 以下是计算属性 watch监听 ------------------------
const selectRoleId = computed(() => {
  if (!selectedKeys.value && _.isEmpty(selectedKeys.value)) {
    return null;
  }
  return selectedKeys.value[0];
});
// ----------------------- 以下是生命周期 ---------------------------------
queryAllRole();
// ----------------------- 以下是方法 ------------------------------------
async function queryAllRole() {
  let res = await roleApi.queryAll();
  roleList.value = res.data;
  if (!_.isEmpty(res.data) && res.data[0].id) {
    selectedKeys.value = [res.data[0].id];
  }
}
function showOperateRoleModal(role) {
  operateRoleModal.value.showModal(role);
}
function deleteRole(roleId) {
  if (!roleId) {
    return;
  }
  Modal.confirm({
    title: "提示",
    content: "确定要删除该角色么？",
    okText: "确定",
    okType: "danger",
    async onOk() {
      useSpinStore().show();
      try {
        await roleApi.deleteRole(roleId);
        message.info("删除成功");
        queryAllRole();
      } catch (e) {
        console.log(e);
      } finally {
        useSpinStore().hide();
      }
    },
    cancelText: "取消",
    onCancel() {},
  });
}
// ----------------------- 以下是暴露的方法内容 ----------------------------
defineExpose({
  selectRoleId,
});
</script>
<style scoped lang="less">
.role-container {
  height: 100%;
  overflow-y: auto;

  :deep(.ant-card-body) {
    padding: 5px;
  }
}
.ant-menu-inline,
.ant-menu-vertical,
.ant-menu-vertical-left {
  border-right: none;
}
</style>
