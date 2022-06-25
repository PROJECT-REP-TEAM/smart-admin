<!--
 * @Description: 
 * @Author: zhuoda
 * @Date: 2022-06-25
 * @LastEditTime: 2022-06-25
 * @LastEditors: zhuoda
-->
<template>
  <div class="notice-div">
    <a-list size="small" bordered :data-source="data">
      <template #renderItem="{ item }">
        <a-list-item>{{ item }}</a-list-item>
      </template>
      <template #header>
        <div class="title">
          <sound-two-tone :style="{ fontSize: '18px' }" />
          通知公告
        </div>
      </template>
      <template #footer>
        <div>Footer</div>
      </template>
    </a-list>
  </div>
</template>
<script>
  import { defineComponent } from 'vue';
  const data = [
    'Racing car sprays burning fuel into crowd.',
    'Japanese princess to wed commoner.',
    'Australian walks 100km after outback crash.',
    'Man charged over missing wedding girl.',
    'Los Angeles battles huge wildfires.',
  ];
  export default defineComponent({
    setup() {
      return {
        data,
      };
    },
  });
</script>
<style scoped lang="less">
  .notice-div {
    background-color: #fff;
  }
  .title {
    &::before {
      content: '';
      position: absolute;
      top: 3px;
      left: 0;
      width: 6px;
      height: 30px;
      background-color: @primary-color;
    }
  }
</style>
