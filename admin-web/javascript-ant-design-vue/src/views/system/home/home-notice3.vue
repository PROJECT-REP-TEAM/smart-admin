<!--
 * @Description:
 * @Author: zhuoda
 * @Date: 2022-06-25
 * @LastEditTime: 2022-06-25
 * @LastEditors: zhuoda
-->
<template>
  <default-home-card extra="更多" icon="ProfileTwoTone">
    <template #title>
      <a-tabs class="title-tabs" size="small">
        <a-tab-pane key="1" tab="运营政策"/>
        <a-tab-pane key="2" tab="优惠活动"/>
        <a-tab-pane key="3" tab="业务资料"/>
      </a-tabs>
    </template>
    <a-empty v-if="$lodash.isEmpty(data)"/>
    <ul v-else>
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
  </default-home-card>
</template>
<script setup>
import {ref} from "vue";
import DefaultHomeCard from "/@/views/system/home/components/default-home-card.vue";

let data = ref([
  {
    title: '忙完六一，忙毕业季，累到不行的毕业典礼真的是孩子们想要的吗？',
    newFlag: true,
    readFlag: false,
    time: '2022-06-28'
  },
  {
    title: '忙完六一，忙毕业季，累到不行的毕业典礼真的是孩子们想要的吗？',
    newFlag: true,
    readFlag: false,
    time: '2022-06-28'
  },
  {
    title: '忙完六一，忙毕业季，累到不行的毕业典礼真的是孩子们想要的吗？',
    newFlag: false,
    readFlag: true,
    time: '2022-06-28'
  },
  {
    title: '忙完六一，忙毕业季，累到不行的毕业典礼真的是孩子们想要的吗？',
    newFlag: false,
    readFlag: true,
    time: '2022-06-28'
  },
  {
    title: '忙完六一，忙毕业季，累到不行的毕业典礼真的是孩子们想要的吗？',
    newFlag: false,
    readFlag: false,
    time: '2022-06-28'
  }
])
</script>
<style lang="less" scoped>
.title-tabs {
  ::v-deep(.ant-tabs-nav) {
    margin: 0 0 9px 10px;
    &::before {
      border-bottom-width: 0 !important;
    }
  }
  ::v-deep(.ant-tabs-tab) {
    padding: 0 0 6px 0 !important;
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
</style>
