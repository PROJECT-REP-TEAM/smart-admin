<!--
 * @Author: zhuoda
 * @Date: 2021-08-30 09:14:55
 * @LastEditTime: 2021-08-30 10:50:53
 * @LastEditors: zhuoda
 * @Description:
 * @FilePath: /smart-admin/src/views/system/employee/role/components/role-data-scope/index.vue
-->
<template>
  <div>
    <div class="btn-group">
      <a-button class="button-style" type="primary" @click="updateDataScope"> 保存 </a-button>
      <a-button class="button-style" @click="getDataScope"> 刷新 </a-button>
    </div>
    <a-row class="header">
      <a-col class="tab-margin" :span="4">业务单据</a-col>
      <a-col class="tab-data" :span="8">查看数据范围</a-col>
      <a-col class="tab-margin" :span="12" />
    </a-row>
    <div class="data-container">
      <a-row class="data" align="middle" justify="center" v-for="item in dataScopeList" :key="item.dataScopeType">
        <a-col class="tab-margin" :span="4">
          {{ item.dataScopeTypeName }}
        </a-col>
        <a-col class="tab-data" :span="8">
          <a-radio-group v-model:value="item.viewType">
            <a-radio
              v-for="scope in item.viewTypeList"
              :key="`${item.dataScopeType}-${scope.viewType}`"
              class="radio-style"
              :value="scope.viewType"
              >{{ scope.viewTypeName }}</a-radio
            >
          </a-radio-group>
        </a-col>
        <a-col class="tab-margin tab-desc" :span="12">
          <p>{{ item.dataScopeTypeDesc }}</p>
        </a-col>
      </a-row>
    </div>
  </div>
</template>
<script setup lang="ts">
  import { message } from 'ant-design-vue';
  import { inject, ref, Ref, watch } from 'vue';
  import { roleApi } from '/@/api/system/role/role-api';

  // ----------------------- 以下是字段定义 emits props ---------------------
  defineProps<{
    value;
  }>();
  defineEmits<{
    (e: 'update:value');
  }>();
  let selectRoleId = inject('selectRoleId') as Ref<number>;
  let dataScopeList = ref([]);
  // ----------------------- 以下是计算属性 watch监听 ------------------------
  watch(
    () => selectRoleId.value,
    () => getRoleDataScope()
  );
  // ----------------------- 以下是生命周期 ---------------------------------
  getDataScope();
  // ----------------------- 以下是方法 ------------------------------------
  // 获取数据
  async function getDataScope() {
    try {
      let result = await roleApi.getDataScopeList();
      dataScopeList.value = result.data;
      getRoleDataScope();
    } catch (e) {
      console.error(e);
    }
  }
  // 获取数据范围根据角色id
  async function getRoleDataScope() {
    try {
      let result = await roleApi.getDataScopeByRoleId(selectRoleId.value);
      let data = result.data;
      dataScopeList.value.forEach((item) => {
        let find = data.find((e) => e.dataScopeType == item.dataScopeType);
        item.viewType = undefined;
        if (find) {
          item.viewType = find.viewType;
        }
      });
    } catch (e) {
      console.error(e);
    }
  }
  // 更新
  async function updateDataScope() {
    try {
      let data = {
        roleId: selectRoleId.value,
        batchSetList: dataScopeList.value,
      };
      await roleApi.updateDataScope(data);
      message.success('保存成功');
      getDataScope();
    } catch (e) {
      console.error(e);
    }
  }
  // ----------------------- 以下是暴露的方法内容 ----------------------------
  defineExpose({});
</script>
<style scoped lang="less">
  .btn-group {
    text-align: right;
  }

  .button-style {
    margin: 0 10px;
  }
  .header {
    border-bottom: 1px solid #f2f2f2;
    font-weight: 600;
    margin: 10px 0px;
  }
  .tab-data {
    margin: 10px 0px;
  }

  .data-container {
    height: 680px;
    overflow-y: scroll;
  }
  .data {
    border-bottom: 1px solid #f2f2f2;
    margin: 10px 0px;
  }
  .radio-style {
    display: block;
    height: 30px;
    line-height: 30px;
  }
  .tab-margin {
    text-align: center;
    margin: 10px 0px;
  }
  .tab-desc {
    line-height: 30px;
    font-size: 16px;
    text-align: left;
  }
</style>
