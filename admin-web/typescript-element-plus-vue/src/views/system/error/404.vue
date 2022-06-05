<!--
 * @Author: LiHaiFan
 * @Date: 2022-06-05 15:26:04
 * @LastEditTime: 2022-06-06 00:19:21
 * @LastEditors: LiHaiFan
 * @Description: 
 * @FilePath: \typescript-element-plus-vue\src\views\system\error\404.vue
-->
<template>
  <div class="main-container">
    <img src="/images/404.png" />
    <p class="desc">抱歉…您访问的页面失联啦~{{ countdown }}s后</p>
    <a-button type="primary" @click="gotoHome">返回首页</a-button>
  </div>
</template>
<script setup lang="ts">
  import { onMounted, ref } from 'vue';
  import { PAGE_NAME_HOME } from '/@/constants/common';
  import { router } from '/@/router';

  // ----------------------- 以下是字段定义 emits props ---------------------
  let countdown = ref(3);
  // ----------------------- 以下是计算属性 watch监听 ------------------------

  // ----------------------- 以下是生命周期 ---------------------------------
  onMounted(() => {
    let interval = setInterval(() => {
      countdown.value--;
      if (countdown.value == 0) {
        clearInterval(interval);
        gotoHome();
      }
    }, 1000);
  });
  // ----------------------- 以下是方法 ------------------------------------
  function gotoHome() {
    router.push({ name: PAGE_NAME_HOME });
  }
  // ----------------------- 以下是暴露的方法内容 ----------------------------
  defineExpose({});
</script>
<style scoped lang="scss">
  .main-container {
    width: 100%;
    height: 100%;
    display: flex;
    align-items: center;
    justify-content: center;
    flex-direction: column;

    > img {
      width: 30vw;
    }
    .desc {
      font-size: 15px;
      font-weight: 400;
      color: #70829a;
    }
  }
</style>
