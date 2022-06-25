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
          <profile-two-tone :style="{ fontSize: '18px' }" />
          <a-tabs size="small" style="margin-top: -10px">
            <a-tab-pane key="1" tab="运营政策" />
            <a-tab-pane key="2" tab="优惠活动" />
            <a-tab-pane key="3" tab="业务资料" />
          </a-tabs>
        </div>
      </template>
      <template #extra><a href="#">更多</a></template>
      <ul>
        <li class="un-read">
          <a>
            <a-tag class="tag" color="#f5222d">NEW</a-tag>
            们可以将同样的函数定义为一个方法，而不是</a
          >
          <span class="time">2021-12-12</span>
        </li>
        <li class="un-read">
          <a>
            <a-tag class="tag" color="#f5222d">NEW</a-tag>
            我们为什么需要缓存？假设我们有一个性能开销比较大的计</a
          >
          <span class="time">2021-12-12</span>
        </li>
        <li class="un-read">
          <a>
            <a-tag class="tag" color="#f5222d">NEW</a-tag>
            我们为什么需要缓存？假设我们有一个性能开销比较大的计</a
          >
          <span class="time">2021-12-12</span>
        </li>
        <li class="un-read">
          <a>
            <a-tag class="tag" color="#f5222d">NEW</a-tag>
            我们为什么需要缓存？假设我们有一个性能开销比较大的计</a
          >
          <span class="time">2021-12-12</span>
        </li>
        <li class="un-read">
          <a> 我们为什么需要缓存？假设我们有一个性能开销比较大的计</a>
          <span class="time">2021-12-12</span>
        </li>
        <li class="un-read">
          <a> 我们为什么需要缓存？假设我们有一个性能开销比较大的计</a>
          <span class="time">2021-12-12</span>
        </li>
        <li class="un-read">
          <a> 我们为什么需要缓存？假设我们有一个性能开销比较大的计</a>
          <span class="time">2021-12-12</span>
        </li>
        <li class="un-read">
          <a> 我们为什么需要缓存？假设我们有一个性能开销比较大的计</a>
          <span class="time">2021-12-12</span>
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
      width: calc(100%-75px);
      .time {
        color: @text-color-secondary;
        min-width: 75px;
      }
      .tag {
        line-height: 14px;
      }
    }
    ul li :hover {
      color: @primary-color;
    }
    .un-read a {
      color: @text-color;
      .tag {
        color: #fff;
      }
    }

    .read a {
      color: @text-color-secondary;
      .tag {
        color: #fff;
      }
    }
  }
</style>
