<template>
  <a-drawer
      :width="720"
      :visible="visible"
      :body-style="{ paddingBottom: '80px' }"
      @close="onClose"
  >
    <DataTracerList
        :business-id="sourceId"
        :business-type="DATA_TRACER_BUSINESS_TYPE_ENUM.GOODS.value"/>
  </a-drawer>
</template>
<script setup lang="ts">
import {ref} from "vue";
import DataTracerList from '/@/components/data-tracer/index.vue';
import {DATA_TRACER_BUSINESS_TYPE_ENUM} from '/@/constants/business/data-tracer';
// ----------------------- 以下是字段定义 emits props ------------------------

// 是否展示抽屉
const visible = ref(false);
const sourceId = ref<number>(undefined);
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
