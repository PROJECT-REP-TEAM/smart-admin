<!--
 * @Author: zhuoda
 * @Date: 2021-08-28 11:59:20
 * @LastEditTime: 2021-08-28 16:20:53
 * @LastEditors: zhuoda
 * @Description:
 * @FilePath: /smart-admin/src/views/system/employee/role/components/role-tree/role-tree-checkbox.vue
-->
<template>
  <div style="height: 542px; overflow: auto">
    <a-checkbox-group v-model:value="checkedData">
      <div class="checked-box">
        <ul>
          <!--li 菜单模块 start-->
          <RoleTreeMenu :tree="props.tree" :index="0" />
          <!--li 菜单模块 end-->
        </ul>
      </div>
    </a-checkbox-group>
  </div>
</template>
<script setup lang="ts">
  import { ref, watch } from 'vue';
  import { useRoleStore } from '/@/store/modules/system/role';
  import RoleTreeMenu from './role-tree-menu.vue';

  // ----------------------- 以下是字段定义 emits props ---------------------
  let props = defineProps<{
    tree?: [];
  }>();
  defineEmits<{
    (e: 'update:value'): void;
  }>();
  let roleStore = useRoleStore();
  let checkedData = ref();
  // ----------------------- 以下是计算属性 watch监听 ------------------------
  watch(
    () => roleStore.checkedData,
    (e) => (checkedData.value = e),
    {
      deep: true,
    }
  );
  // ----------------------- 以下是生命周期 ---------------------------------

  // ----------------------- 以下是方法 ------------------------------------

  // ----------------------- 以下是暴露的方法内容 ----------------------------
  defineExpose({});
</script>
<style scoped lang="less">
  @import './index.less';
</style>
