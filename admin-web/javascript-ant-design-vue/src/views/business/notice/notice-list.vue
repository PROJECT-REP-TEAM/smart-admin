<template>
  <a-form class="smart-query-form">
    <a-row class="smart-query-form-row">
      <a-form-item class="smart-query-form-item" label="关键字">
        <a-input
            v-model:value="queryForm.searchWord"
            placeholder="请输入文章标题/创建人姓名"
            style="width: 300px"
        />
      </a-form-item>

      <a-form-item class="smart-query-form-item" label="通知类型">
        <smart-enum-select v-model:value="queryForm.noticeType" enum-name="NOTICE_TYPE_ENUM" placeholder="请选择通知类型"
                           style="width: 100%"/>
      </a-form-item>

      <a-form-item class="smart-query-form-item" label="所属栏目">
        <smart-enum-select v-model:value="queryForm.noticeBelongType" enum-name="NOTICE_BELONG_TYPE_ENUM"
                           placeholder="请选择所属栏目"
                           style="width: 100%"/>
      </a-form-item>

      <a-form-item class="smart-query-form-item" label="发布时间">
        <a-range-picker :ranges="defaultTimeRanges"
                        style="width: 100%"
                        @change="changeDate"/>
      </a-form-item>

      <a-form-item class="smart-query-form-item" label="状态">
        <a-select
            v-model:value="queryForm.disabledFlag"
            :allowClear="true"
            placeholder="请选择状态"
            style="width: 100%"
        >
          <a-select-option :value="false">启用</a-select-option>
          <a-select-option :value="true">禁用</a-select-option>
        </a-select>
      </a-form-item>

      <a-form-item class="smart-query-form-item smart-margin-left10">
        <a-button type="primary" @click="ajaxQuery">
          <template #icon>
            <ReloadOutlined/>
          </template>
          查询
        </a-button>
        <a-button @click="resetQuery">
          <template #icon>
            <SearchOutlined/>
          </template>
          重置
        </a-button>
      </a-form-item>
    </a-row>
  </a-form>
  <a-card :bordered="false" :hoverable="true" size="small">
    <a-row class="smart-table-btn-block">
      <div class="smart-table-operate-block">
        <a-button size="small" type="primary" @click="addNotice">
          <template #icon>
            <PlusOutlined/>
          </template>
          新建
        </a-button>

        <a-button
            :disabled="selectedRowKeyList.length == 0"
            size="small"
            type="danger"
            @click="confirmBatchDelete"
        >
          <template #icon>
            <DeleteOutlined/>
          </template>
          批量删除
        </a-button>
      </div>
      <div class="smart-table-setting-block"></div>
    </a-row>

    <a-table
        :columns="columns"
        :dataSource="tableData"
        :pagination="false"
        :row-selection="{ selectedRowKeys: selectedRowKeyList, onChange: onSelectChange }"
        :scroll="{ x: 1300 }"
        rowKey="noticeId"
        size="small"
    >
      <template #noticeTitle="{ record }">
        <a @click="goDetail(record.noticeId)">{{ record.noticeTitle }}</a>
      </template>

      <template #noticeType="{ text }">
        <span>{{ $smartEnumPlugin.getDescByValue("NOTICE_TYPE_ENUM", text) }}</span>
      </template>

      <template #noticeBelongType="{ text }">
        <span>{{ $smartEnumPlugin.getDescByValue("NOTICE_BELONG_TYPE_ENUM", text) }}</span>
      </template>

      <template #coverFileKey="{ text }">
        <template v-if="!$lodash.isEmpty(text)">
          <img :src="text[0].fileUrl" class="cover-img"/>
        </template>
      </template>

      <template #topFlag="{ text }">
        {{ text ? '是' : '否' }}
      </template>

      <template #disabledFlag="{ text }">
        {{ text ? '禁用' : '启用' }}
      </template>

      <template #action="{ record }">
        <a-button type="link" @click="updateNotice(record.noticeId)">编辑</a-button>
      </template>
    </a-table>

    <div class="smart-query-table-page">
      <a-pagination
          v-model:current="queryForm.pageNum"
          v-model:pageSize="queryForm.pageSize"
          :defaultPageSize="queryForm.pageSize"
          :pageSizeOptions="PAGE_SIZE_OPTIONS"
          :show-total="(total) => `共${total}条`"
          :total="total"
          show-less-items
          showQuickJumper
          showSizeChanger
          @change="ajaxQuery"
          @showSizeChange="ajaxQuery"
      />
    </div>
  </a-card>
</template>
<script setup>
import {onMounted, reactive, ref} from "vue";
import {PAGE_SIZE, PAGE_SIZE_OPTIONS} from "/@/constants/common-const";
import {defaultTimeRanges} from "/@/lib/default-time-ranges"
import SmartEnumSelect from "/@/components/smart-enum-select/index.vue";
import {noticeApi} from "/@/api/business/notice/notice-api";
import {message, Modal} from "ant-design-vue";
import {useSpinStore} from "/@/store/modules/system/spin";
import {useRouter} from "vue-router";
// ----------------------- 数据查询 ----------------------------
const columns = reactive([
  {
    title: "公告标题",
    dataIndex: "noticeTitle",
    slots: {customRender: "noticeTitle"},
  },
  {
    title: "通知类型",
    dataIndex: "noticeType",
    slots: {customRender: "noticeType"},
  },
  {
    title: "所属栏目",
    dataIndex: "noticeBelongType",
    slots: {customRender: "noticeBelongType"},
  },
  {
    title: "封面",
    dataIndex: "coverFileKey",
    slots: {customRender: "coverFileKey"},
  },
  {
    title: "是否置顶",
    dataIndex: "topFlag",
    slots: {customRender: "topFlag"},
  },
  {
    title: "观看量",
    dataIndex: "watchAmount",
  },
  {
    title: "发布时间",
    dataIndex: "publishTime",
    width: 200
  },
  {
    title: "禁用状态",
    dataIndex: "disabledFlag",
    slots: {customRender: "disabledFlag"},
  },
  {
    title: "创建人",
    dataIndex: "createUserName",
  },
  {
    title: "操作",
    dataIndex: "action",
    fixed: "right",
    slots: {customRender: "action"},
  },
]);

const queryFormState = {
  keywords: "",
  noticeType: undefined,
  noticeBelongType: undefined,
  startTime: undefined,
  endTime: undefined,
  disabledFlag: undefined,
  pageNum: 1,
  pageSize: PAGE_SIZE,
};
const queryForm = reactive({...queryFormState});
const tableLoading = ref(false);
const selectedRowKeyList = ref([]);
const tableData = ref([]);
const total = ref(0);

function changeDate(dates, dateStrings) {
  queryForm.startTime = dateStrings[0];
  queryForm.endTime = dateStrings[1];
}

function onSelectChange(selectedRowKeys) {
  selectedRowKeyList.value = selectedRowKeys;
}

function resetQuery() {
  Object.assign(queryForm, queryFormState);
  ajaxQuery();
}

async function ajaxQuery() {
  try {
    tableLoading.value = true;
    let responseModel = await noticeApi.queryNotice(queryForm);
    const list = responseModel.data.list;
    total.value = responseModel.data.total;
    tableData.value = list;
  } catch (e) {
    console.log(e);
  } finally {
    tableLoading.value = false;
  }
}

onMounted(() => {
  ajaxQuery();
})

// ----------------------- 数据删除 ----------------------------
function confirmBatchDelete() {
  Modal.confirm({
    title: "提示",
    content: "确定要删除选中公告吗?",
    okText: "删除",
    okType: "danger",
    onOk() {
      batchDelete();
    },
    cancelText: "取消",
    onCancel() {
    },
  });
}

const batchDelete = async () => {
  try {
    useSpinStore().show();
    let deleteForm = {
      noticeIdList: selectedRowKeyList.value,
    };
    await noticeApi.deleteNotice(deleteForm);
    message.success("删除成功");
    ajaxQuery();
    selectedRowKeyList.value = [];
  } catch (e) {
    console.log(e);
  } finally {
    useSpinStore().hide();
  }
};
let router = useRouter();

// ----------------------- 数据添加编辑 ----------------------------
function addNotice() {
  router.push({path: '/business/notice/operate'});
}

function updateNotice(noticeId) {
  router.push({path: '/business/notice/operate', query: {noticeId: noticeId}});
}

//----------------------- 详情 ----------------------------
function goDetail(noticeId) {
  router.push({path: '/business/notice/detail', query: {noticeId: noticeId}});
}
</script>
<style lang='less' scoped>
.cover-img {
  width: 45px;
  height: 45px;
}
</style>
