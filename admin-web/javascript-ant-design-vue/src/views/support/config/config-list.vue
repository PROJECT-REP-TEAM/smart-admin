<template>
  <div>
    <a-form class="smart-query-form">
      <a-row class="smart-query-form-row">
        <a-form-item label="参数Key" class="smart-query-form-item">
          <a-input style="width: 300px" v-model:value="queryForm.configKey" placeholder="请输入key" />
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

          <a-button @click="toEditOrAdd()" type="primary" class="smart-margin-left20">
            <template #icon>
              <PlusOutlined />
            </template>
            新建
          </a-button>
        </a-form-item>
      </a-row>
    </a-form>

    <a-card size="small" :bordered="false" :hoverable="true">
      <a-table :scroll="{ x: 1300 }" size="small" bordered :dataSource="tableData" :columns="columns" rowKey="configId" :pagination="false">
        <template #bodyCell="{ text, record, index, column }">
          <template v-if="column.dataIndex === 'action'">
            <a-button @click="toEditOrAdd(record)" type="link">编辑</a-button>
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
    <ConfigFormModal ref="configFormModal" @reloadList="resetQuery" />
  </div>
</template>
<script setup>
  import { onMounted, reactive, ref } from 'vue';
  import { configApi } from '../../../api/support/config/config-api';
  import ConfigFormModal from './config-form-modal.vue';
  import { PAGE_SIZE_OPTIONS } from '/@/constants/common-const';

  const columns = reactive([
    {
      title: 'id',
      width: 80,
      dataIndex: 'configId',
    },
    {
      title: '参数key',
      dataIndex: 'configKey',
    },
    {
      title: '参数名称',
      dataIndex: 'configName',
    },
    {
      title: '参数值',
      dataIndex: 'configValue',
    },
    {
      title: '备注',
      dataIndex: 'remark',
    },
    {
      title: '创建时间',
      dataIndex: 'createTime',
    },
    {
      title: '修改时间',
      dataIndex: 'updateTime',
    },

    {
      title: '操作',
      dataIndex: 'action',
      fixed: 'right',
    },
  ]);

  const queryFormState = {
    configKey: '',
    pageNum: 1,
    pageSize: 10,
  };
  const queryForm = reactive({ ...queryFormState });

  const tableLoading = ref(false);
  const tableData = ref([]);
  const total = ref(0);

  function resetQuery() {
    Object.assign(queryForm, queryFormState);
    ajaxQuery();
  }
  async function ajaxQuery() {
    try {
      tableLoading.value = true;
      let responseModel = await configApi.queryList(queryForm);
      const list = responseModel.data.list;
      total.value = responseModel.data.total;
      tableData.value = list;
    } catch (e) {
      console.log(e);
    } finally {
      tableLoading.value = false;
    }
  }

  const configFormModal = ref();
  function toEditOrAdd(rowData) {
    configFormModal.value.showModal(rowData);
  }

  onMounted(ajaxQuery);
</script>
