<template>
  <default-home-card extra="更多" icon="FireTwoTone" title="更新日志">
    <a-empty v-if="$lodash.isEmpty(data)"/>
    <ul v-else>
      <template v-for="(item,index) in data" :key="index">
        <li :class="[item.readFlag ? 'read' : 'un-read']">
          <a class="content">
            <a-badge :status="item.readFlag ? 'default' : 'geekblue'"/>
            {{ item.title }}
          </a>
          <span class="time"> {{ item.time }}</span>
        </li>
      </template>
    </ul>
  </default-home-card>
</template>
<script setup>
import {ref} from "vue";
import DefaultHomeCard from "/@/views/system/home/components/default-home-card.vue";

let data = ref([
  {
    title: '【v1.2.0】新增View UI Plus JS版本',
    readFlag: false,
    time: '2022-06-28'
  },
  {
    title: '【v1.1.9】新增Element Plus JS版本',
    readFlag: false,
    time: '2022-06-14'
  },
  {
    title: '【v1.1.8】新增Element Plus TS版本',
    readFlag: true,
    time: '2022-05-22'
  },
  {
    title: '【v1.1.7】新增Ant Design Vue JS版本',
    readFlag: true,
    time: '2022-04-04'
  },
  {
    title: '【v1.1.6】新增Ant Design Vue TS版本',
    readFlag: true,
    time: '2022-06-28'
  }
])
</script>
<style lang='less' scoped>
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
