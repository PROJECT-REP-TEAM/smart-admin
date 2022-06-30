<template>
  <default-home-card extra="更多" icon="AlertTwoTone" title="常见问题" @extraClick="more">
    <a-empty v-if="$lodash.isEmpty(data)"/>
    <ul v-else>
      <template v-for="(item,index) in data" :key="index">
        <li>
          <a class="content" @click="goDetail(item,true)">
            <a-badge status="gold"/>
            <span class="title">{{ item.noticeTitle }}</span>
          </a>
          <span class="desc">
            发布于{{ item.publishTime }} 　 更新于{{ item.updateTime }}
          </span>
        </li>
      </template>
    </ul>
  </default-home-card>
</template>
<script setup>
import DefaultHomeCard from "/@/views/system/home/components/default-home-card.vue";
import {noticeSetup} from "/@/views/business/notice/notice-setup";
import {NOTICE_BELONG_TYPE_ENUM} from "/@/constants/business/notice-const";
import {useRouter} from "vue-router";
import {computed, onMounted} from "vue";

const router = useRouter();
let data = computed(() => {
  return tableData.value;
})
let {queryForm, tableData, ajaxQuery, goDetail} = noticeSetup();

onMounted(() => {
  queryForm.noticeBelongType = NOTICE_BELONG_TYPE_ENUM.COMMON_PROBLEM.value;
  queryForm.pageSize = 15;
  ajaxQuery();
})

function more() {
  router.push({
    path: '/business/notice/notice-list',
    query: {noticeBelongType: NOTICE_BELONG_TYPE_ENUM.COMMON_PROBLEM.value}
  })
}
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
