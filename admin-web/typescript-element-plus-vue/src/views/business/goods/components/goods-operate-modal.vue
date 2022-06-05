<template>
  <el-drawer :title="form.goodsId ? '编辑' : '添加'" :size="720" v-model="visible" @close="onClose">
    <el-form ref="formRef" :model="form" :rules="rules">
      <el-form-item label="商品类型" prop="goodsType">
        <smart-enum-select style="width: 100%" v-model:value="form.goodsType" placeholder="请选择类型" enum-name="GOODS_TYPE_ENUM" />
      </el-form-item>
      <el-form-item label="商品分类" prop="categoryId">
        <category-tree v-model:value="form.categoryId" placeholder="请选择商品分类" :categoryType="CATEGORY_TYPE_ENUM.GOODS.value" />
      </el-form-item>
      <el-form-item label="商品名称" prop="goodsName">
        <el-input v-model="form.goodsName" placeholder="请输入商品名称" />
      </el-form-item>
      <el-form-item label="商品状态" prop="shelvesFlag">
        <el-radio-group v-model="form.shelvesFlag">
          <el-radio :label="true">上架</el-radio>
          <el-radio :label="false">下架</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="商品价格" prop="price">
        <el-input-number style="width: 100%" placeholder="请输入商品价格" v-model="form.price" :min="0" :max="9999" />
      </el-form-item>
      <el-form-item label="备注" prop="remark">
        <el-input style="width: 100%" placeholder="请输入备注" v-model="form.remark" />
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button style="margin-right: 8px" @click="onClose">取消</el-button>
      <el-button type="primary" @click="onSubmit">提交</el-button>
    </template>
  </el-drawer>
</template>
<script setup lang="ts">
  import { ref, nextTick, reactive } from 'vue';
  import SmartEnumSelect from '/@/components/smart-enum-select/index.vue';
  import CategoryTree from '/@/components/category-tree-select/index.vue';
  import { GOODS_TYPE_ENUM } from '/@/constants/business/goods';
  import { CATEGORY_TYPE_ENUM } from '/@/constants/business/category';
  import { useSpinStore } from '/@/store/modules/system/spin';
  import _ from 'lodash';
  import { goodsApi } from '/@/api/business/goods/goods-api';
  import { ElMessage } from 'element-plus';
  import { GoodsAddForm } from '/@/api/business/goods/model/goods-add-form';
  import { GoodsUpdateForm } from '/@/api/business/goods/model/goods-update-form';

  // ----------------------- 以下是字段定义 emits props ------------------------
  // emit
  const emit = defineEmits<{
    (e: 'reloadList'): void;
  }>();

  // 组件ref
  const formRef = ref();

  const formDefault: GoodsAddForm & GoodsUpdateForm = {
    categoryId: undefined,
    coverPic: '',
    goodsIntro: '',
    goodsName: '',
    goodsType: GOODS_TYPE_ENUM.BOOK.value,
    price: undefined,
    shelvesFlag: true,
    goodsId: undefined,
    // remark: "",
  };
  let form = reactive<GoodsAddForm & GoodsUpdateForm>({ ...formDefault });
  const rules = {
    categoryId: [{ required: true, message: '请选择商品分类' }],
    goodsType: [{ required: true, message: '请选择商品分组' }],
    goodsName: [{ required: true, message: '商品名称不能为空' }],
    price: [{ required: true, message: '商品价格不能为空' }],
  };
  // 是否展示抽屉
  const visible = ref(false);

  // ----------------------- 以下是生命周期 ------------------------

  // ----------------------- 以下是方法 ------------------------
  function showDrawer(rowData?: GoodsUpdateForm) {
    Object.assign(form, formDefault);
    if (rowData && !_.isEmpty(rowData)) {
      Object.assign(form, rowData);
    }
    console.log(form);
    visible.value = true;
    nextTick(() => {});
  }

  function onClose() {
    Object.assign(form, formDefault);
    visible.value = false;
  }

  function onSubmit() {
    formRef.value
      .validate()
      .then(async () => {
        useSpinStore().show();
        try {
          if (form.goodsId) {
            await goodsApi.updateGoods(form);
          } else {
            await goodsApi.addGoods(form);
          }
          ElMessage.success(`${form.goodsId ? '修改' : '添加'}成功`);
          onClose();
          emit('reloadList');
        } catch (error) {
          console.log(error);
        } finally {
          useSpinStore().hide();
        }
      })
      .catch((error: any) => {
        console.log('error', error);
        ElMessage.error('参数验证错误，请仔细填写表单数据!');
      });
  }

  // ----------------------- 以下是暴露的方法内容 ------------------------
  defineExpose({
    showDrawer,
  });
</script>
