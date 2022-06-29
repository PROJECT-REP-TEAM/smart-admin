<template>
  <default-home-card extra="更多" icon="AlertTwoTone" title="常见问题">
    <a-empty v-if="$lodash.isEmpty(data)"/>
    <ul v-else>
      <template v-for="(item,index) in data" :key="index">
        <li>
          <a class="content">
            <a-badge status="gold"/>
            <span class="title">{{ item.title }}</span>
          </a>
          <span class="desc">
            发布于{{ item.createTime }} 　 更新于{{ item.updateTime }}
          </span>
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
    title: '如何设置菜单权限？如何设置菜单权限？如何设置菜单权限？',
    createTime: '2022-06-28',
    updateTime: '2022-06-28'
  },
  {
    title: '如何进行页面缓存？如何进行页面缓存？如何进行页面缓存？',
    createTime: '2022-06-14',
    updateTime: '2022-06-28'
  },
  {
    title: 'v1版本能平滑升级到v2么？v1版本能平滑升级到v2么？v1版本能平滑升级到v2么？',
    createTime: '2022-05-22',
    updateTime: '2022-06-28'
  },
  {
    title: '代码生成器？代码生成器？代码生成器？',
    createTime: '2022-04-04',
    updateTime: '2022-06-28'
  },
  {
    title: '心跳机制？心跳机制？心跳机制？',
    createTime: '2022-06-28',
    updateTime: '2022-06-28'
  },
  {
    title: '如何设置菜单权限？如何设置菜单权限？如何设置菜单权限？',
    createTime: '2022-06-28',
    updateTime: '2022-06-28'
  },
  {
    title: '如何进行页面缓存？如何进行页面缓存？如何进行页面缓存？',
    createTime: '2022-06-14',
    updateTime: '2022-06-28'
  },
  {
    title: 'v1版本能平滑升级到v2么？v1版本能平滑升级到v2么？v1版本能平滑升级到v2么？',
    createTime: '2022-05-22',
    updateTime: '2022-06-28'
  },
  {
    title: '代码生成器？代码生成器？代码生成器？',
    createTime: '2022-04-04',
    updateTime: '2022-06-28'
  },
  {
    title: '心跳机制？心跳机制？心跳机制？',
    createTime: '2022-06-28',
    updateTime: '2022-06-28'
  },
  {
    title: '如何设置菜单权限？如何设置菜单权限？如何设置菜单权限？',
    createTime: '2022-06-28',
    updateTime: '2022-06-28'
  },
])
</script>
<style lang='less' scoped>
ul li {
  display: flex;
  flex-direction: column;
  margin-bottom: 8px;

  .title {
    font-size: 14px;
    font-weight: 500;
    color: @text-color;
  }

  .content {
    white-space: nowrap;
    text-overflow: ellipsis;
    overflow: hidden;
    word-break: break-all;
  }

  .desc {
    flex-shrink: 0;
    color: @text-color-secondary;
    min-width: 75px;
    padding-left: 14px;
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
