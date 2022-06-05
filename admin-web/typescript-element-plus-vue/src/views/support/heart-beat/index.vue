<!--
 * @Author: LiHaiFan
 * @Date: 2022-06-05 15:26:04
 * @LastEditTime: 2022-06-05 22:46:18
 * @LastEditors: LiHaiFan
 * @Description: 
 * @FilePath: \typescript-element-plus-vue\src\views\support\heart-beat\index.vue
-->
<template>
  <el-card shadow="hover">
    <el-table size="small" :data="tableData" border v-loading="tableLoading">
      <el-table-column prop="projectPath" label="项目路径"></el-table-column>
      <el-table-column prop="serverIp" label="服务器ip"></el-table-column>
      <el-table-column prop="processNo" label="进程号"></el-table-column>
      <el-table-column prop="processStartTime" label="进程开启时间"></el-table-column>
      <el-table-column prop="heartBeatTime" label="心跳当前时间"></el-table-column>
    </el-table>

    <div class="smart-query-table-page">
      <el-pagination
        background
        v-model:currentPage="queryForm.pageNum"
        v-model:page-size="queryForm.pageSize"
        :page-sizes="PAGE_SIZE_OPTIONS"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
        @size-change="ajaxQuery"
        @current-change="ajaxQuery"
      >
      </el-pagination>
    </div>
  </el-card>
</template>
<script lang="ts" setup>
  import { reactive, ref, onMounted } from 'vue';
  import { heartBeatApi } from '/@/api/support/heart-beat/heart-beat-api';
  import { HeartBeatQueryForm } from '/@/api/support/heart-beat/model/heart-beat-query-form';
  import { HeartBeatVo } from '/@/api/support/heart-beat/model/heart-beat-vo';
  import { PAGE_SIZE_OPTIONS } from '/@/constants/common';

  const queryFormState: HeartBeatQueryForm = {
    pageNum: 1,
    pageSize: 10,
  };
  const queryForm = reactive<HeartBeatQueryForm>({ ...queryFormState });
  const tableLoading = ref(false);
  const tableData = ref<HeartBeatVo[]>([]);
  const total = ref(0);

  async function ajaxQuery() {
    try {
      tableLoading.value = true;
      let responseModel = await heartBeatApi.queryList(queryForm);
      const list = responseModel.data.list;
      total.value = responseModel.data.total;
      tableData.value = list;
    } catch (e) {
      console.log(e);
    } finally {
      tableLoading.value = false;
    }
  }

  onMounted(ajaxQuery);
</script>
