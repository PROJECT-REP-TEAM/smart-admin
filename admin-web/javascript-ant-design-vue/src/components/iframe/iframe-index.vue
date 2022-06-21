<template>
  <iframe :id="name" :src="url" frameborder="0" height="800" scrolling="yes" width="100%"></iframe>
</template>
<script setup>
// ----------------------- 以下是公用变量 emits props ----------------
let props = defineProps({
  name: String,
  url:String
});
// ----------------------- 展开自定义组合式函数 -----------------------

// ----------------------- 以下是暴露的方法内容 -----------------------
defineExpose({});
// ----------------------- 以下是生命周期 ----------------------------

// ----------------------- 以下是业务内容 ----------------------------
</script>
<style lang='less' scoped></style>
