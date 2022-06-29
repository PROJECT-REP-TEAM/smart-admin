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
      <a-tabs class="title-tabs" size="small" v-model:activeKey="activeKey" @change="tabsChange">
        <a-tab-pane v-for="item in noticeBelongType" :key="item.value" :tab="item.desc"/>
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
import DefaultHomeCard from "/@/views/system/home/components/default-home-card.vue";
import {ref,computed, inject, onMounted} from "vue";
import {noticeSetup} from "/@/views/business/notice/notice-setup";
import {NOTICE_BELONG_TYPE_ENUM} from "/@/constants/business/notice-const";

let smartEnumPlugin = inject("smartEnumPlugin");

let noticeBelongType = computed(() => {
  let noticeBelongTypeList = smartEnumPlugin.getValueDescList('NOTICE_BELONG_TYPE_ENUM');
  return noticeBelongTypeList.filter(e => e.value != NOTICE_BELONG_TYPE_ENUM.ANNOUNCEMENT.value);
  l
})

let activeKey = ref(NOTICE_BELONG_TYPE_ENUM.SOLICIT_OPINIONS.value);

let data = computed(() => {
  return tableData.value;
})
let {queryForm, tableData, ajaxQuery} = noticeSetup();

function tabsChange(key){
  queryForm.noticeBelongType = key;
  queryForm.pageSize = 5;
  ajaxQuery();
}

onMounted(() => {
  tabsChange(activeKey.value);
})
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
