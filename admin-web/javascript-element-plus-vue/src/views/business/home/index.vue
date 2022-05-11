<template>
  <el-row :gutter="24">
    <el-col :span="8">
      <el-card header="本月业绩" class="no-footer">
        <div class="content statistice">
          <p class="count">¥10,241,024</p>
          <div class="footer">上月总收款金额：¥1024</div>
        </div>
      </el-card>
    </el-col>
    <el-col :span="8">
      <el-card header="本月订单数" class="no-footer">
        <div class="content statistice">
          <p class="count">1024</p>
          <div class="footer">上月总订单数：1024</div>
        </div>
      </el-card>
    </el-col>
    <el-col :span="8">
      <el-card header="移动客户端" class="no-footer">
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
      </el-card>
    </el-col>
    <el-col :span="12">
      <el-card header="业绩完成情况">
        <div class="content large gauge">
          <Gauge :percent="saleTargetPercent" />
          <div>
            <p>本月目标金额：<span class="count">1024</span></p>
            <p>本月完成金额：<span class="count">1024</span></p>
            <p>日均完成金额：<span class="count">￥1024</span></p>
          </div>
        </div>
      </el-card>
    </el-col>
    <el-col :span="12">
      <el-card header="待办事项">
        <div class="content large wait-handle">
          <p>
            <el-tag color="#108ee9">审批</el-tag> 有{{
              0
            }}个审批待审核
            <a>查看更多></a>
          </p>
          <p>
            <el-tag color="#f50">退款</el-tag> 有{{
              0
            }}个退款被驳回
            <a>查看更多></a>
          </p>
          <p>
            <el-tag color="#2db7f5">缺陷</el-tag> 有{{
              0
            }}个缺陷被找到
            <a>查看更多></a>
          </p>
          <p>
            <el-tag color="#d3adf7">尾款</el-tag> 有{{
              0
            }}元尾款待收回
            <a>查看更多></a>
          </p>
          <p>
            <el-tag color="#87d068">合同</el-tag> 有{{
              0
            }}个合同工客户已签署
            <a>查看更多></a>
          </p>
          <p>
            <el-tag color="#108ee9">问题</el-tag> 有{{
              0
            }}个问题被驳回
            <a>查看更多></a>
          </p>
          <p>
            <el-tag color="#f50">能量</el-tag> 有{{
              0
            }}个能量被收取
            <a>查看更多></a>
          </p>
        </div>
      </el-card>
    </el-col>
    <el-col :span="12">
      <el-card header="缺陷排行榜">
        <template #header>
          <span class="card-header">
            <span>缺陷排行榜</span>
            <el-button type="primary" plain>查看更多</el-button>
          </span>
        </template>
        <div class="content">
          <el-table :data="bugList" :border="true" height="360">
            <el-table-column type="index"></el-table-column>
            <el-table-column prop="name" label="姓名"></el-table-column>
            <el-table-column prop="count" label="数量"></el-table-column>
            <el-table-column prop="week" label="周期"></el-table-column>
            <el-table-column prop="comment" label="评价"></el-table-column>
          </el-table>
        </div>
      </el-card>
    </el-col>
    <el-col :span="12">
      <el-card>
        <template #header>
          <span class="card-header">
            <span>技术排行榜</span>
            <el-button type="primary" plain>查看更多</el-button>
          </span>
        </template>
        <div class="content">
          <el-table size="small" :data="bugList" :border="true" height="360">
            <el-table-column type="index"></el-table-column>
            <el-table-column prop="name" label="姓名"></el-table-column>
            <el-table-column prop="count" label="数量"></el-table-column>
            <el-table-column prop="week" label="周期"></el-table-column>
            <el-table-column prop="comment" label="评价"></el-table-column>
          </el-table>
        </div>
      </el-card>
    </el-col>
  </el-row>
</template>
<script setup>
import { computed, reactive, ref } from "vue";
import { useRouter } from "vue-router";
import Gauge from "./components/gauge.vue";
// ----------------------- 以下是字段定义 emits props ---------------------
let router = useRouter();
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
const bugList = ref([]);

for (let index = 0; index < nameList.length; index++) {
  bugList.value.push({
    name: nameList[index],
    count: index,
    week: index + 3,
    comment: commentList[index % commentList.length],
  });
}
console.log(bugList)
function enterDetail(path, query) {
  router.push({ path, query });
}
// ----------------------- 以下是暴露的方法内容 ----------------------------
defineExpose({});
</script>

<style lang="scss" scoped>
:deep(.el-card) {
  margin-bottom: 20px;
}
:deep(.el-card__header){
  font-weight: 500;
  font-size: 16px;
}
:deep(.el-tag) {
  color: #FFFFFF;
}
.no-footer {
  margin-bottom: 20px;
}

.content {
  height: 150px;

  &.large {
    height: 360px;
  }

  &.statistice {
    display: flex;
    flex-direction: column;
    justify-content: space-between;
  }
  &.app {
    display: flex;
    align-items: center;
    padding-bottom: 24px;
    .app-qr {
      display: flex;
      align-items: center;
      flex-direction: column;
      margin-right: 40px;
      > img {
        height: 120px;
      }
      > span {
        font-size: 14px;
      }
    }
  }

  &.gauge {
    display: flex;
    align-items: center;
  }

  &.wait-handle {
    padding-bottom: 24px;
    overflow-y: auto;
    >p {
      font-size: 18px;
      margin-bottom: 1em;
    }

    a {
      color: #1890ff;
      cursor: pointer;
    }
    :deep(.el-tag){
      padding: 1px 8px;
      font-size: 15px;
    }
  }

  .count {
    font-size: 30px;
    font-weight: 700;
    margin-bottom: 10px;
  }
}
.footer {
  width: 100%;
  border-top: 1px solid #e9e9e9;
  padding: 10px 0;
  background: #fff;
  z-index: 1;
}
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

</style>

