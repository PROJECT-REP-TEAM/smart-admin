<!--
 * @Author: zhuoda
 * @Date: 2021-08-03 10:27:11
 * @LastEditTime: 2021-09-01
 * @LastEditors: zhuoda
 * @Description:
 * @FilePath: /smart-admin/src/views/system/home/index.vue
-->
<template>
  <a-row :gutter="24">
    <a-col :span="8">
      <a-card title="本月业绩" class="no-footer">
        <div class="content statistice">
          <p class="count">¥10,241,024</p>
          <div class="footer">上月总收款金额：¥1024</div>
        </div>
      </a-card>
    </a-col>
    <a-col :span="8">
      <a-card title="本月订单数" class="no-footer">
        <div class="content statistice">
          <p class="count">1024</p>
          <div class="footer">上月总订单数：1024</div>
        </div>
      </a-card>
    </a-col>
    <a-col :span="8">
      <a-card title="移动客户端" class="no-footer">
        <div class="content app">
          <div class="app-qr">
            <img src="/images/1024lab-gzh.jpg" />
            <span>1024创新实验室-公众号</span>
          </div>
          <div class="app-qr">
            <img src="/images/1024lab-gzh.jpg" />
            <span>扫描二维码下载App</span>
          </div>
        </div>
      </a-card>
    </a-col>
    <a-col :span="12">
      <a-card title="业绩完成情况">
        <div class="content large gauge">
          <Gauge :percent="saleTargetPercent" />
          <div>
            <p>本月目标金额：<span class="count">1024</span></p>
            <p>本月完成金额：<span class="count">1024</span></p>
            <p>日均完成金额：<span class="count">￥1024</span></p>
          </div>
        </div>
      </a-card>
    </a-col>
    <a-col :span="12">
      <a-card title="待办事项">
        <div class="content large wait-handle">
          <p>
            <a-tag color="#108ee9">审批</a-tag> 有{{
              waitHandle.waitTrackNum || 0
            }}个审批待审核
            <a>查看更多></a>
          </p>
          <p>
            <a-tag color="#f50">退款</a-tag> 有{{
              waitHandle.rejectRefundOrderNum || 0
            }}个退款被驳回
            <a>查看更多></a>
          </p>
          <p>
            <a-tag color="#2db7f5">缺陷</a-tag> 有{{
              waitHandle.rejectReceiveOrderNum || 0
            }}个缺陷被找到
            <a>查看更多></a>
          </p>
          <p>
            <a-tag color="#d3adf7">尾款</a-tag> 有{{
              waitHandle.userTotalAmount || 0
            }}元尾款待收回
            <a>查看更多></a>
          </p>
          <p>
            <a-tag color="#87d068">合同</a-tag> 有{{
              waitHandle.signContractNum || 0
            }}个合同工客户已签署
            <a>查看更多></a>
          </p>
          <p>
            <a-tag color="#108ee9">问题</a-tag> 有{{
              waitHandle.rejectCompleteReceiveOrderNum || 0
            }}个问题被驳回
            <a>查看更多></a>
          </p>
          <p>
            <a-tag color="#f50">能量</a-tag> 有{{
              waitHandle.rejectCompleteReceiveOrderNum || 0
            }}个能量被收取
            <a>查看更多></a>
          </p>
        </div>
      </a-card>
    </a-col>
    <a-col :span="12">
      <a-card title="缺陷排行榜">
        <template #extra><a>查看更多</a></template>
        <div class="content large">
          <a-table
            size="small"
            :scroll="{ y: 300 }"
            :dataSource="bugList"
            :columns="bugColumns"
            :pagination="false"
            bordered
          >
            <template #bodyCell="{ text, record, index, column }">
              <template  v-if="column.dataIndex === 'index'">
                <span>{{ index + 1 }}</span>
              </template>
              <template v-else-if="column.dataIndex === 'name'">
                <span>{{ record.name }}</span>
              </template>
              <template  v-else-if="column.dataIndex === 'count'">
                <span v-if="text">{{ text }}</span>
                <span v-else>-</span>
              </template>
              <template  v-else-if="column.dataIndex === 'week'">
                <span v-if="text">{{ text }}</span>
                <span v-else>-</span>
              </template>
              <template  v-else-if="column.dataIndex === 'comment'">
                <span v-if="text">{{ text }}</span>
                <span v-else>-</span>
              </template>
            </template>
          </a-table>
        </div>
      </a-card>
    </a-col>
    <a-col :span="12">
      <a-card title="技术排行榜">
        <template #extra><a>查看更多</a></template>
        <div class="content large">
          <a-table
            size="small"
            :scroll="{ y: 300 }"
            :dataSource="bugList"
            :columns="bugColumns"
            :pagination="false"
            bordered
          >
            <template #bodyCell="{ text, record, index, column }">
              <template  v-if="column.dataIndex === 'index'">
                <span>{{ index + 1 }}</span>
              </template>
              <template v-else-if="column.dataIndex === 'name'">
                <span>{{ record.name }}</span>
              </template>
              <template  v-else-if="column.dataIndex === 'count'">
                <span v-if="text">{{ text }}</span>
                <span v-else>-</span>
              </template>
              <template  v-else-if="column.dataIndex === 'week'">
                <span v-if="text">{{ text }}</span>
                <span v-else>-</span>
              </template>
              <template  v-else-if="column.dataIndex === 'comment'">
                <span v-if="text">{{ text }}</span>
                <span v-else>-</span>
              </template>
            </template>

          </a-table>
        </div>
      </a-card>
    </a-col>
  </a-row>
</template>
<script setup lang="ts">
import { computed, reactive, ref } from "vue";
import { useRouter } from "vue-router";
import Gauge from "./components/gauge.vue";
import { HomeWaitHandleVo } from "/@/api/system/home/model/home-wait-handle-vo";
// ----------------------- 以下是字段定义 emits props ---------------------
let waitHandle = ref<HomeWaitHandleVo>({});
let router = useRouter();
const bugColumns = reactive([
  {
    title: "",
    dataIndex: 'index',
    width: 80,
  },
  {
    title: "姓名",
    dataIndex: 'name',
  },
  {
    title: "数量",
    dataIndex: "count",
  },
  {
    title: "周期",
    dataIndex: "week",
  },
  {
    title: "评价",
    dataIndex: "comment",
  },
]);
// ----------------------- 以下是计算属性 watch监听 ------------------------
// 业绩完成百分比
const saleTargetPercent = computed(() => {
  return 75;
});
// ----------------------- 以下是方法 ------------------------------------
const nameList = [
  "初晓",
  "胡克",
  "开云",
  "罗伊",
  "佩弦",
  "清野",
  "琴酒",
  "善逸",
  "玄朋",
  "卓大",
  "初晓",
  "胡克",
  "开云",
  "罗伊",
  "佩弦",
  "清野",
  "琴酒",
  "善逸",
  "玄朋",
  "卓大",
];
const commentList = ["好棒", "oh yeah", "nice", "good job", "漂亮"];
const bugList = [];

for (let index = 0; index < nameList.length; index++) {
  bugList.push({
    name: nameList[index],
    count: index,
    week: index + 3,
    comment: commentList[index % commentList.length],
  });
}
// ----------------------- 以下是暴露的方法内容 ----------------------------
defineExpose({});
</script>
<style scoped lang="less">
@import "./index.less";
</style>
