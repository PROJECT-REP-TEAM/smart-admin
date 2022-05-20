<template>
  <div>
    <a-card title="基本信息">
      <div class="detail-title">
        <ContainerFilled />
        请求地址：{{ detail.url }}
      </div>
      <a-row class="detail-info-box">
        <a-col :span="16">
          <a-row class="detail-info">
            <a-col :span="12"> 用户id：{{ detail.operateUserId }}</a-col>
            <a-col :span="12"> 用户名称： {{ detail.operateUserName }}</a-col>
            <a-col :span="12"> 请求模块： {{ detail.module }}</a-col>
            <a-col :span="12"> 请求日期： {{ detail.createTime }}</a-col>
          </a-row>
          <a-row class="detail-info">
            <a-col> 请求地址： {{ detail.url }}</a-col>
          </a-row>
          <a-row class="detail-info">
            <a-col> 请求方法： {{ detail.method }}</a-col>
          </a-row>
          <a-row class="detail-info">
            <a-col> 请求内容： {{ detail.content }}</a-col>
          </a-row>
        </a-col>
        <a-col :span="8">
          <p class="detail-right-title">请求状态</p>
          <p class="detail-right">{{ detail.successFlag ? "成功" : "失败"}}</p>
        </a-col>
      </a-row>
    </a-card>
    <a-card title="请求参数" class="smart-margin-top10">
      <json-viewer :value="detail.param ? JSON.parse(detail.param):''" copyable boxed sort />
    </a-card>
    <a-card title="失败原因" class="smart-margin-top10">
      <div>
        {{detail.failReason}}
      </div>
    </a-card>
  </div>


</template>

<script setup lang="ts">
import {useSpinStore} from "/@/store/modules/system/spin";
import {operateLogApi} from "/@/api/support/operate-log/operate-log-api";
import {useRoute} from "vue-router";
import {onMounted, reactive} from "vue";
import { OperateLogVo } from "/@/api/support/operate-log/model/operate-log-vo";

onMounted(async () => {
  await getDetail();
});

const route = useRoute();

let detail = reactive<OperateLogVo>({});
async function getDetail() {
  if (!route.query.operateLogId) {
    return;
  }
  try {
    useSpinStore().show();
    let res = await operateLogApi.detail(route.query.operateLogId);
    detail = Object.assign(detail,res.data);
  } catch (e) {
    console.log(e);
  } finally {
    useSpinStore().hide();
  }
}

defineExpose({});

</script>

<style scoped  lang="less">
.detail-title {
  display: flex;
  align-items: center;
  font-size: 20px;
  font-weight: bold;
}
.detail-info-box {
  margin-top: 15px;
}
.detail-info {
  .ant-col {
    line-height: 1.46;
    margin-bottom: 12px;
    padding-right: 5px;
  }
}
.detail-right-title {
  text-align: right;
  color: grey;
}

.detail-right {
  padding-left: 5px;
  font-size: 20px;
  font-weight: bold;
  text-align: right;
}
</style>
