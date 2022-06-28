<!--
 * @Description:
 * @Author: zhuoda
 * @Date: 2022-06-25
 * @LastEditTime: 2022-06-25
 * @LastEditors: zhuoda
-->
<template>
  <div class="card-container">
    <a-card size="small">
      <template #title>
        <div class="title">
          <profile-two-tone :style="{ fontSize: '18px' }"/>
          <a-tabs class="title-tabs" size="small">
            <a-tab-pane key="1" tab="运营政策"/>
            <a-tab-pane key="2" tab="优惠活动"/>
            <a-tab-pane key="3" tab="业务资料"/>
          </a-tabs>
        </div>
      </template>
      <template #extra><a href="#">更多</a></template>
      <ul>
        <li v-for="(item,index) in data" :key="index" :class="[item.readFlag ? 'read' : 'un-read']">
          <a-tooltip placement="top">
            <template #title>
              <span>{{ item.title }}</span>
            </template>
            <a class="content">
              <a-tag v-if="item.newFlag" class="tag" color="#f5222d">NEW</a-tag>
              {{ item.title }}
            </a>
          </a-tooltip>
          <span class="time">{{ item.time }}</span>
        </li>
      </ul>
    </a-card>
  </div>
</template>
<script setup>
import {ref} from "vue";

let data = ref([
  {
    title:'忙完六一，忙毕业季，累到不行的毕业典礼真的是孩子们想要的吗？',
    newFlag:true,
    readFlag:false,
    time:'2022-06-28'
  },
  {
    title:'忙完六一，忙毕业季，累到不行的毕业典礼真的是孩子们想要的吗？',
    newFlag:true,
    readFlag:false,
    time:'2022-06-28'
  },
  {
    title:'忙完六一，忙毕业季，累到不行的毕业典礼真的是孩子们想要的吗？',
    newFlag:false,
    readFlag:true,
    time:'2022-06-28'
  },
  {
    title:'忙完六一，忙毕业季，累到不行的毕业典礼真的是孩子们想要的吗？',
    newFlag:false,
    readFlag:true,
    time:'2022-06-28'
  },
  {
    title:'忙完六一，忙毕业季，累到不行的毕业典礼真的是孩子们想要的吗？',
    newFlag:false,
    readFlag:false,
    time:'2022-06-28'
  }
])
</script>
<style lang="less" scoped>
.card-container {
  background-color: #fff;
  .title {
    display: flex;
    align-items: center;
    ::v-deep(.ant-tabs-nav) {
      margin: 0 0 8px 10px;
    }
    ::v-deep(.ant-tabs-tab) {
      padding: 0 0 8px 0;
    }
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

  ul li {
    display: flex;
    justify-content: space-between;
    margin-bottom: 8px;

    .content {
      white-space: nowrap;
      text-overflow: ellipsis;
      overflow: hidden;
      word-break: break-all;
    }

    .time {
      flex-shrink: 0;
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
