<template>
  <el-form class="smart-query-form">
    <el-row class="smart-query-form-row">
      <el-form-item label="用户名称" class="smart-query-form-item">
        <el-input style="width: 300px" v-model="queryForm.userName" placeholder="用户名称" />
      </el-form-item>

      <el-form-item label="请求时间" class="smart-query-form-item">
        <el-date-picker @change="changeCreateDate" v-model="createDateRange" :shortcuts="defaultChooseTimeRange" style="width: 240px" />
      </el-form-item>

      <el-form-item label="快速筛选" class="smart-query-form-item">
        <el-radio-group v-model="queryForm.successFlag" @change="ajaxQuery">
          <el-radio-button :label="undefined">全部</el-radio-button>
          <el-radio-button :label="true">成功</el-radio-button>
          <el-radio-button :label="false">失败</el-radio-button>
        </el-radio-group>
      </el-form-item>

      <el-form-item class="smart-query-form-item smart-margin-left10">
        <el-button type="primary" @click="ajaxQuery" icon="Search"> 查询 </el-button>
        <el-button @click="resetQuery" icon="RefreshLeft"> 重置 </el-button>
      </el-form-item>
    </el-row>
  </el-form>

  <el-card shadow="hover">
    <el-table size="small" :data="tableData" border :rowKey="rowKey" v-loading="tableLoading">
      <el-table-column prop="operateUserId" label="用户id"></el-table-column>
      <el-table-column prop="operateUserName" label="用户名称"></el-table-column>
      <el-table-column prop="module" label="操作模块"></el-table-column>
      <el-table-column prop="content" label="操作内容"></el-table-column>
      <el-table-column prop="url" label="请求路径"></el-table-column>
      <el-table-column prop="method" label="请求方法"></el-table-column>
      <el-table-column prop="successFlag" label="请求结果">
        <template #default="scope">
          <span>{{ scope.row.successFlag ? '成功' : '失败' }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="创建时间"></el-table-column>
      <el-table-column fixed="right" label="操作">
        <template #default="scope">
          <el-button @click="toDetail(scope.row.operateLogId)" link type="primary">详情</el-button>
        </template>
      </el-table-column>
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
  import { router } from '/@/router';
  import { operateLogApi } from '/@/api/support/operate-log/operate-log-api';
  import { PAGE_SIZE_OPTIONS } from '/@/constants/common';
  import { defaultTimeRanges } from '/@/lib/default-time-ranges';
  import { OperateLogQueryForm } from '/@/api/support/operate-log/model/operate-log-query-form';
  import { OperateLogVo } from '/@/api/support/operate-log/model/operate-log-vo';

  const queryFormState: OperateLogQueryForm = {
    userName: '',
    successFlag: undefined,
    startDate: undefined,
    endDate: undefined,
    pageNum: 1,
    pageSize: 10,
  };
  const queryForm = reactive<OperateLogQueryForm>({ ...queryFormState });
  const createDateRange = ref([]);
  const defaultChooseTimeRange = defaultTimeRanges;
  // 时间变动
  function changeCreateDate(dates: [moment, moment] | [string, string], dateStrings: [string, string]): void {
    queryForm.startDate = dateStrings[0];
    queryForm.endDate = dateStrings[1];
  }

  const tableLoading = ref(false);
  const rowKey = ref('operateLogId');
  const tableData = ref<OperateLogVo[]>([]);
  const total = ref(0);

  function resetQuery() {
    Object.assign(queryForm, queryFormState);
    createDateRange.value = [];
    ajaxQuery();
  }
  async function ajaxQuery() {
    try {
      tableLoading.value = true;
      let responseModel = await operateLogApi.queryList(queryForm);
      const list = responseModel.data.list;
      total.value = responseModel.data.total;
      tableData.value = list;
    } catch (e) {
      console.log(e);
    } finally {
      tableLoading.value = false;
    }
  }

  function toDetail(operateLogId: number) {
    router.push({ path: '/log/operate-log/detail', query: { operateLogId } });
  }

  onMounted(ajaxQuery);
</script>
