<template>
  <el-card shadow='hover'>
    <el-row class="smart-table-btn-block">
      <div class="smart-table-operate-block">
        <el-button @click="addCategory()" type="primary" size="small" icon='Plus'>新建</el-button>
      </div>
      <div class="smart-table-setting-block"></div>
    </el-row>

    <el-table
      size="small"
      :data="tableData"
      :rowKey="rowKey"
      @expand-change="changeExpand"
      :expand-row-keys="expandedRowKeys"
    >
      <el-table-column prop='categoryName' :label='columnName'></el-table-column>
      <el-table-column prop='action' label='操作'>
        <template #default='scope'>
          <el-button @click="addCategory(scope.row.categoryId)" link type='primary'>增加子分类</el-button>
          <el-button @click="addCategory(undefined, scope.row)" link type='primary'>编辑</el-button>
          <el-button @click="confirmDeleteCategory(scope.row.categoryId)" link type='primary'>删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <CategoryOperateModal ref="operateModal" @reloadList="reloadList" />
  </el-card>
</template>
<script setup>
import { reactive, ref, onMounted, computed } from "vue";
import { useSpinStore } from "/@/store/modules/spin";
import CategoryOperateModal from "./category-operate-modal.vue";
import { categoryApi } from "/@/api/business/category-api";
import { CATEGORY_TYPE_ENUM } from "/@/constants/business/category";
import { ElMessage, ElMessageBox } from 'element-plus';

const columnNameList = [
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
const columnName = computed(() => {
  let find = columnNameList.find((e) => e.categoryType == props.categoryType);
  return find ? find.columnName : "";
});

// ----------------------- 定义的props ------------------------
const props = defineProps({
  categoryType:Number
});

const columns = reactive([
  {
    title: columnName,
    dataIndex: "categoryName",
  },
  {
    title: "操作",
    dataIndex: "action",
  },
]);
const rowKey = ref('categoryId');
const operateModal = ref();
const tableLoading = ref(false);
const tableData = ref([]);

const expandedRowKeys = ref([]);
// ----------------------- 以下是方法 ------------------------
async function queryList() {
  try {
    tableLoading.value = true;
    let queryForm = {
      categoryType: props.categoryType,
    };
    let responseModel = await categoryApi.queryCategoryTree(queryForm);
    const list = responseModel.data;
    tableData.value = list;
  } catch (e) {
    console.log(e);
  } finally {
    tableLoading.value = false;
  }
}
function reloadList(parentId) {
  queryList();
  if (parentId) {
    expandedRowKeys.value.push(parentId);
  }
}

function changeExpand(val) {
  expandedRowKeys.value = val.map(e=>e[rowKey.value]);
}

function addCategory(parentId, rowData) {
  operateModal.value.showModal(props.categoryType, parentId, rowData);
}
function confirmDeleteCategory(categoryId) {
  ElMessageBox.confirm('确定要删除当前分类吗?', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'error',
  }).then(() => {
    deleteCategory(categoryId);
  }).catch(() => {
  });
}
async function deleteCategory(categoryId) {
  try {
    useSpinStore().show();
    await categoryApi.deleteCategoryById(categoryId);
    ElMessage.success("撤销成功");
    await queryList();
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
