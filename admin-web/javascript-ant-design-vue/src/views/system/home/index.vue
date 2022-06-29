<!--
 * @Author: zhuoda
 * @Date: 2021-08-03 10:27:11
 * @LastEditTime: 2022-06-25
 * @LastEditors: zhuoda
 * @Description:
 * @FilePath: /smart-admin/src/views/system/home/index.vue
-->
<template>
  <!--  顶部用户信息-->
  <a-row>
    <HomeHeader/>
  </a-row>
  <!--下方左右布局-->
  <a-row :gutter="[10,10]">
    <!--左侧-->
    <a-col :span="17">
      <a-row :gutter="[10,10]">
        <!--系统通知-->
        <a-col :span="12">
          <HomeNotice3/>
        </a-col>
        <!--业务通知-->
        <a-col :span="12">
          <HomeNotice2/>
        </a-col>
        <!--待办、已办-->
        <a-col :span="24">
          <ToBeDoneCard />
        </a-col>
        <!--各类报表-->
        <a-col :span="6">
          <Gauge :percent="saleTargetPercent"/>
        </a-col>
        <a-col :span="6">
          <Pie />
        </a-col>
        <a-col :span="12">
          <Category />
        </a-col>
        <a-col :span="24">
          <Gradient />
        </a-col>
      </a-row>
    </a-col>
    <!--右侧-->
    <a-col :span="7">
      <a-row :gutter="[10,10]">
        <!--快捷入口-->
        <a-col :span="24">
          <HomeQuickEntry/>
        </a-col>
        <!--关注公众号-->
        <a-col :span="24">
          <OfficialAccountCard/>
        </a-col>
        <!--更新日志-->
        <a-col :span="24">
          <ChangelogCard />
        </a-col>
        <!--常见问题-->
        <a-col :span="24">
          <CommonProblemCard />
        </a-col>
      </a-row>
    </a-col>
  </a-row>
</template>
<script setup>
import {computed} from 'vue';
import {useRouter} from 'vue-router';
import HomeHeader from './home-header.vue';
import HomeNotice2 from './home-notice2.vue';
import HomeNotice3 from './home-notice3.vue';
import HomeQuickEntry from './components/quick-entry/home-quick-entry.vue';
import OfficialAccountCard from './components/official-account-card.vue'
import ToBeDoneCard from './components/to-be-done-card.vue';
import ChangelogCard from './components/changelog-card.vue';
import CommonProblemCard from './components/common-problem-card.vue';
import Gauge from './components/echarts/gauge.vue';
import Category from './components/echarts/category.vue';
import Pie from './components/echarts/pie.vue';
import Gradient from './components/echarts/gradient.vue';
// ----------------------- 以下是字段定义 emits props ---------------------
let router = useRouter();
// ----------------------- 以下是计算属性 watch监听 ------------------------
// 业绩完成百分比
const saleTargetPercent = computed(() => {
  return 75;
});
defineExpose({});
</script>
<style lang="less" scoped>
@import './index.less';
</style>
