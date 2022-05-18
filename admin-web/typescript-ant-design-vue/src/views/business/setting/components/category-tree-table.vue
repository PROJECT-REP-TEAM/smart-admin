<template>
  <a-card size="small" :bordered="false" :hoverable="true">
    <a-row class="smart-table-btn-block">
      <div class="smart-table-operate-block">
        <a-button @click="addCategory()" type="primary" size="small">
          <template #icon>
            <PlusOutlined />
          </template>
          新建
        </a-button>
      </div>
      <div class="smart-table-setting-block"></div>
    </a-row>

    <a-table
      :scroll="{ x: 1000 }"
      size="small"
      :dataSource="tableData"
      :columns="columns"
      rowKey="categoryId"
      :pagination="false"
      @expandedRowsChange="changeExand"
      :expanded-row-keys="expandedRowKeys"
    >
      <template #bodyCell="{ text, record, index, column }">
        <template v-if="column.dataIndex === 'action'" >
          <a-button @click="addCategory(record.categoryId)" type="link"
          >增加子分类</a-button
          >
          <a-button @click="addCategory(undefined, record)" type="link">编辑</a-button>
          <a-button @click="confirmDeleteCategory(record.categoryId)" type="link"
          >删除</a-button
          >
        </template>
      </template>

    </a-table>
    <CategoryOperateModal ref="operateModal" @reloadList="reloadList" />
  </a-card>
</template>
<script lang="ts" setup>
import { reactive, ref, onMounted, computed } from "vue";
import { Modal, message } from "ant-design-vue";
import { useSpinStore } from "/@/store/modules/system/spin";
import CategoryOperateModal from "./category-operate-modal.vue";
import type { ResponseModel } from "/@/api/base-model/response-model";
import { categoryApi } from "/@/api/business/category/category-api";
import { CategoryTreeQueryForm } from "/@/api/business/category/model/category-tree-query-form";
import { CategoryTreeVo } from "/@/api/business/category/model/category-tree-vo";
import { CategoryUpdateForm } from "/@/api/business/category/model/category-update-form";
import { CATEGORY_TYPE_ENUM } from "/@/constants/business/category";

interface CustomColumnName {
  categoryType: number; // 分组类型
  columnName: string; // 分类的列名
}
const columnNameList: CustomColumnName[] = [
  {
    categoryType: CATEGORY_TYPE_ENUM.GOODS.value,
    columnName: "商品分类",
  },
  {
    categoryType: CATEGORY_TYPE_ENUM.MAJOR.value,
    columnName: "专业分类",
  }
];
// ----------------------- 以下是计算属性 watch监听 ------------------------
const columName = computed(() => {
  let find = columnNameList.find((e) => e.categoryType == props.categoryType);
  return find ? find.columnName : "";
});

// ----------------------- 定义的props ------------------------
interface CategoryTreeProps {
  categoryType: number; // 分组类型
}
const props = withDefaults(defineProps<CategoryTreeProps>(), {
  categoryType: undefined,
});

const columns = reactive([
  {
    title: columName,
    dataIndex: "categoryName",
  },
  {
    title: "操作",
    dataIndex: "action",
  },
]);
const operateModal = ref();
const tableLoading = ref<Boolean>(false);
const tableData = ref<CategoryTreeVo[]>([]);

const expandedRowKeys = ref<number[]>([]);
// ----------------------- 以下是方法 ------------------------
async function queryList() {
  try {
    tableLoading.value = true;
    let queryForm: CategoryTreeQueryForm = {
      categoryType: props.categoryType,
    };
    let responseModel: ResponseModel<
      CategoryTreeVo[]
    > = await categoryApi.queryCategoryTree(queryForm);
    const list = responseModel.data;
    tableData.value = list;
  } catch (e) {
    console.log(e);
  } finally {
    tableLoading.value = false;
  }
}
function reloadList(parentId?: number) {
  queryList();
  if (parentId) {
    expandedRowKeys.value.push(parentId);
  }
}

function changeExand(val: []) {
  expandedRowKeys.value = val;
}

function addCategory(parentId?: number, rowData?: CategoryUpdateForm) {
  operateModal.value.showModal(props.categoryType, parentId, rowData);
}
function confirmDeleteCategory(categoryId: number) {
  Modal.confirm({
    title: "提示",
    content: "确定要删除当前分类吗?",
    okText: "确定",
    okType: "danger",
    async onOk() {
      deleteCategory(categoryId);
    },
    cancelText: "取消",
    onCancel() {},
  });
}
async function deleteCategory(categoryId: number) {
  try {
    useSpinStore().show();
    await categoryApi.deleteCategoryById(categoryId);
    message.success("撤销成功");
    queryList();
  } catch (e) {
    console.log(e);
  } finally {
    useSpinStore().hide();
  }
}
onMounted(queryList);
// ----------------------- 以下是暴露的方法内容 ------------------------
defineExpose({
  queryList,
});
</script>
