<!--
 * @Author: zhuoda
 * @Date: 2021-08-30 09:14:55
 * @LastEditTime: 2022-06-05 22:55:31
 * @LastEditors: LiHaiFan
 * @Description:
 * @FilePath: \typescript-element-plus-vue\src\views\system\employee\role\components\role-data-scope\index.vue
-->
<template>
  <div>
    <div class="btn-group">
      <el-button v-privilege="'system:role:dataScope:update'" class="button-style" type="primary" @click="updateDataScope"> 保存 </el-button>
      <el-button class="button-style" @click="getDataScope"> 刷新</el-button>
    </div>
    <el-row class="header">
      <el-col class="tab-margin" :span="4">业务单据</el-col>
      <el-col class="tab-data" :span="8">查看数据范围</el-col>
      <el-col class="tab-margin" :span="12" />
    </el-row>
    <div class="detail-container">
      <el-row class="data" align="middle" justify="center" v-for="item in dataScopeList" :key="item.dataScopeType">
        <el-col class="tab-margin" :span="4">
          {{ item.dataScopeTypeName }}
        </el-col>
        <el-col class="tab-data" :span="8">
          <el-radio-group v-model="item.viewType">
            <el-radio v-for="scope in item.viewTypeList" :key="`${item.dataScopeType}-${scope.viewType}`" class="radio-style" :label="scope.viewType"
              >{{ scope.viewTypeName }}
            </el-radio>
          </el-radio-group>
        </el-col>
        <el-col class="tab-margin tab-desc" :span="12">
          <p>{{ item.dataScopeTypeDesc }}</p>
        </el-col>
      </el-row>
    </div>
  </div>
</template>
<script setup lang="ts">
  import { inject, Ref, ref, watch } from 'vue';
  import { roleApi } from '/@/api/system/role/role-api';
  import { ElMessage } from 'element-plus';
  import { DataScopeAndViewTypeVo } from '/@/api/system/role/model/data-scope-and-view-type-vo';
  import { DataScopeBatchSetDto } from '/@/api/system/role/model/data-scope-batch-set-dto';

  // ----------------------- 以下是字段定义 emits props ---------------------
  let selectRoleId = inject('selectRoleId') as Ref<number>;
  type DataScopeSelectViewTypeVo = DataScopeAndViewTypeVo & DataScopeBatchSetDto;
  let dataScopeList = ref<DataScopeSelectViewTypeVo[]>([]);
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
      await getRoleDataScope();
    } catch (e) {
      console.error(e);
    }
  }

  // 获取数据范围根据角色id
  async function getRoleDataScope() {
    if (!selectRoleId.value) {
      return;
    }
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
      ElMessage.success('保存成功');
      await getDataScope();
    } catch (e) {
      console.error(e);
    }
  }

  // ----------------------- 以下是暴露的方法内容 ----------------------------
  defineExpose({});
</script>
<style scoped lang="scss">
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

  .detail-container {
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
    width: 100%;
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
