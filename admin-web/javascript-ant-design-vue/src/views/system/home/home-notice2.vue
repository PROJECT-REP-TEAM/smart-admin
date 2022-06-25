<!--
 * @Description: 
 * @Author: zhuoda
 * @Date: 2022-06-25
 * @LastEditTime: 2022-06-25
 * @LastEditors: zhuoda
-->
<template>
  <div class="notice-div">
    <a-card size="small">
      <template #title>
        <div class="title">
          <sound-two-tone :style="{ fontSize: '18px' }" />
          通知公告
        </div>
      </template>
      <template #extra><a href="#">更多</a></template>

      <ul>
        <li class="un-read">
          <a>
            <a-badge status="error" />
            们可以将同样的函数定义为一个方法，而不是</a
          >
          <span>2021-12-12</span>
        </li>
        <li class="un-read">
          <a>
            <a-badge status="error" />
            们可以将同样的函数定义为一个方法，而不是</a
          >
          <span>2021-12-12</span>
        </li>
        <li class="un-read">
          <a>
            <a-badge status="error" />
            们可以将同样的函数定义为一个方法，而不是</a
          >
          <span>2021-12-12</span>
        </li>
        <li class="read">
          <a>
            <a-badge status="default" />
            们可以将同样的函数定义为一个方法，而不是</a
          >
          <span>2021-12-12</span>
        </li>
        <li class="read">
          <a>
            <a-badge status="default" />
            们可以将同样的函数定义为一个方法，而不是</a
          >
          <span>2021-12-12</span>
        </li>
        <li class="un-read">
          <a>
            <a-badge status="error" />
            们可以将同样的函数定义为一个方法，而不是</a
          >
          <span>2021-12-12</span>
        </li>
        <li class="read">
          <a>
            <a-badge status="default" />
            们可以将同样的函数定义为一个方法，而不是</a
          >
          <span>2021-12-12</span>
        </li>
        <li class="un-read">
          <a>
            <a-badge status="error" />
            们可以将同样的函数定义为一个方法，而不是</a
          >
          <span>2021-12-12</span>
        </li>
        <li class="read">
          <a>
            <a-badge status="default" />
            们可以将同样的函数定义为一个方法，而不是</a
          >
          <span>2021-12-12</span>
        </li>
      </ul>
    </a-card>
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

    ul li {
      display: flex;
      justify-content: space-between;
      margin-bottom: 8px;
      span {
        color: @text-color-secondary;
      }
    }
    ul li :hover {
      color: @primary-color;
    }
    .un-read a {
      color: @text-color;
    }

    .read a {
      color: @text-color-secondary;
    }
  }
  .title {
    &::before {
      content: '';
      position: absolute;
      top: 3px;
      left: 0;
      width: 3px;
      height: 30px;
      background-color: @primary-color;
    }
  }
</style>
