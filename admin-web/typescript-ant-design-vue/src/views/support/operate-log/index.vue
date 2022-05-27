<template>
  <a-form class="smart-query-form">
    <a-row class="smart-query-form-row">
      <a-form-item label="用户名称" class="smart-query-form-item">
        <a-input
            style="width: 300px"
            v-model:value="queryForm.userName"
            placeholder="用户名称"
        />
      </a-form-item>

      <a-form-item label="请求时间" class="smart-query-form-item">
        <a-range-picker @change="changeCreateDate"
                        v-model:value="createDateRange"
                        :ranges="defaultChooseTimeRange"
                        style="width: 240px" />
      </a-form-item>

      <a-form-item label="快速筛选" class="smart-query-form-item">
        <a-radio-group v-model:value="queryForm.successFlag" @change="ajaxQuery">
          <a-radio-button :value="undefined">全部</a-radio-button>
          <a-radio-button :value="true">成功</a-radio-button>
          <a-radio-button :value="false">失败</a-radio-button>
        </a-radio-group>
      </a-form-item>

      <a-form-item class="smart-query-form-item smart-margin-left10">
        <a-button type="primary" @click="ajaxQuery">
          <template #icon>
            <ReloadOutlined />
          </template>
          查询
        </a-button>
        <a-button @click="resetQuery">
          <template #icon>
            <SearchOutlined />
          </template>
          重置
        </a-button>
      </a-form-item>
    </a-row>
  </a-form>

  <a-card size="small" :bordered="false" :hoverable="true">
    <a-table
      :scroll="{ x: 1300 }"
      size="small"
      :dataSource="tableData"
      :columns="columns"
      rowKey="operateLogId"
      :pagination="false"
      :row-selection="{ selectedRowKeys: selectedRowKeyList, onChange: onSelectChange }"
    >
      <template #bodyCell="{ text, record, index, column }">
        <template v-if="column.dataIndex === 'successFlag'">
          <span>{{ text ? "成功" : "失败" }}</span>
        </template>

        <template v-else-if="column.dataIndex === 'action'">
          <a-button @click="toDetail(record.operateLogId)" type="link">详情</a-button>
        </template>
      </template>
    </a-table>

    <div class="smart-query-table-page">
      <a-pagination
        showSizeChanger
        showQuickJumper
        show-less-items
        :pageSizeOptions="PAGE_SIZE_OPTIONS"
        :defaultPageSize="queryForm.pageSize"
        v-model:current="queryForm.pageNum"
        v-model:pageSize="queryForm.pageSize"
        :total="total"
        @change="ajaxQuery"
        @showSizeChange="ajaxQuery"
        :show-total="(total) => `共${total}条`"
      />
    </div>
  </a-card>
</template>
<script lang="ts" setup>
import { reactive, ref, onMounted } from "vue";
import {router} from "/@/router";
import { operateLogApi } from "/@/api/support/operate-log/operate-log-api";
import type { ResponseModel } from "/@/api/base-model/response-model";
import { OperateLogVo } from "/@/api/support/operate-log/model/operate-log-vo";
import { OperateLogQueryForm } from "/@/api/support/operate-log/model/operate-log-query-form";
import { PageResultModel } from "/@/api/base-model/page-result-model";
import { PAGE_SIZE_OPTIONS } from "/@/constants/common";
import {defaultTimeRanges} from "/@/lib/default-time-ranges";

const columns = reactive([
  {
    title: "用户id",
    dataIndex: "operateUserId",
  },
  {
    title: "用户名称",
    dataIndex: "operateUserName",
  },
  {
    title: "操作模块",
    dataIndex: "module",
  },
  {
    title: "操作内容",
    dataIndex: "content",
  },
  {
    title: "请求路径",
    dataIndex: "url",
  },
  {
    title: "请求方法",
    dataIndex: "method",
  },
  {
    title: "请求结果",
    dataIndex: "successFlag",
  },
  {
    title: "创建时间",
    dataIndex: "createTime",
  },
  {
    title: "操作",
    dataIndex: "action",
    fixed: "right",
  },
]);

const queryFormState: OperateLogQueryForm = {
  userName: "",
  successFlag: undefined,
  startDate: undefined,
  endDate:undefined,
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

const tableLoading = ref<Boolean>(false);
const selectedRowKeyList = ref<Number[]>([]);
const tableData = ref<OperateLogVo[]>([]);
const total = ref<Number>(0);

function onSelectChange(selectedRowKeys: Number[]) {
  selectedRowKeyList.value = selectedRowKeys;
}

function resetQuery() {
  Object.assign(queryForm, queryFormState);
  createDateRange.value = [];
  ajaxQuery();
}
async function ajaxQuery (){
  try {
    tableLoading.value = true;
    let responseModel: ResponseModel<
      PageResultModel<OperateLogVo>
    > = await operateLogApi.queryList(queryForm);
    const list = responseModel.data.list;
    total.value = responseModel.data.total;
    tableData.value = list;
  } catch (e) {
    console.log(e);
  } finally {
    tableLoading.value = false;
  }
};

function toDetail(operateLogId: number) {
  router.push({ path: '/log/operate-log/detail', query: { operateLogId }});
}

onMounted(ajaxQuery);
</script>
