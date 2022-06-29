<!--
 * @Description:
 * @Author: zhuoda
 * @Date: 2022-06-25
 * @LastEditTime: 2022-06-25
 * @LastEditors: zhuoda
-->
<template>
  <default-home-card
      extra="更多"
      icon="SoundTwoTone"
      title="通知公告"
  >
    <a-empty v-if="$lodash.isEmpty(data)"/>
    <ul v-else>
      <li v-for="(item,index) in data" :key="index" :class="[item.readFlag ? 'read' : 'un-read']">
        <a-tooltip placement="top">
          <template #title>
            <span>{{ item.title }}</span>
          </template>
          <a class="content">
            <a-badge :status="item.readFlag ? 'default' : 'error'"/>
            {{ item.title }}
          </a>
        </a-tooltip>
        <span class="time"> {{ item.time }}</span>
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
    readFlag: false,
    time: '2022-06-28'
  },
  {
    title: '忙完六一，忙毕业季，累到不行的毕业典礼真的是孩子们想要的吗？',
    readFlag: false,
    time: '2022-06-28'
  },
  {
    title: '忙完六一，忙毕业季，累到不行的毕业典礼真的是孩子们想要的吗？',
    readFlag: true,
    time: '2022-06-28'
  },
  {
    title: '忙完六一，忙毕业季，累到不行的毕业典礼真的是孩子们想要的吗？',
    readFlag: true,
    time: '2022-06-28'
  },
  {
    title: '忙完六一，忙毕业季，累到不行的毕业典礼真的是孩子们想要的吗？',
    readFlag: false,
    time: '2022-06-28'
  }
])
</script>
<style lang="less" scoped>
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
</style>
