<!--
 * @Author: LiHaiFan
 * @Date: 2022-06-05 15:26:04
 * @LastEditTime: 2022-06-05 21:57:05
 * @LastEditors: LiHaiFan
 * @Description: 
 * @FilePath: \typescript-element-plus-vue\src\views\business\goods\components\goods-data-tracer-modal.vue
-->
<template>
  <el-drawer :size="720" v-model="visible" @close="onClose">
    <DataTracerList :business-id="sourceId" :business-type="DATA_TRACER_BUSINESS_TYPE_ENUM.GOODS.value" />
  </el-drawer>
</template>
<script lang="ts" setup>
  import { ref } from 'vue';
  import DataTracerList from '/@/components/data-tracer/index.vue';
  import { DATA_TRACER_BUSINESS_TYPE_ENUM } from '/@/constants/business/data-tracer';
  // ----------------------- 以下是字段定义 emits props ------------------------

  // 是否展示抽屉
  const visible = ref(false);
  const sourceId = ref<number>();
  // ----------------------- 以下是生命周期 ------------------------

  // ----------------------- 以下是方法 ------------------------
  function showDrawer(goodsId: number) {
    sourceId.value = goodsId;
    visible.value = true;
  }

  function onClose() {
    visible.value = false;
  }

  // ----------------------- 以下是暴露的方法内容 ------------------------
  defineExpose({
    showDrawer,
  });
</script>
