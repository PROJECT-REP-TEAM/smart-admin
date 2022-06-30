<template>
  <default-home-card extra="更多" icon="FireTwoTone" title="更新日志" @extraClick="more">
    <a-empty v-if="$lodash.isEmpty(data)"/>
    <ul v-else>
      <template v-for="(item,index) in data" :key="index">
        <li :class="[item.readFlag ? 'read' : 'un-read']">
          <a class="content" @click="goDetail(item,true)">
            <a-badge :status="item.readFlag ? 'default' : 'geekblue'"/>
            {{ item.noticeTitle }}
          </a>
          <span class="time"> {{ item.publishTime }}</span>
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
  queryForm.noticeBelongType = NOTICE_BELONG_TYPE_ENUM.CHANGELOG.value;
  queryForm.pageSize = 5;
  ajaxQuery();
})

function more() {
  router.push({
    path: '/business/notice/notice-list',
    query: {noticeBelongType: NOTICE_BELONG_TYPE_ENUM.CHANGELOG.value}
  })
}
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
