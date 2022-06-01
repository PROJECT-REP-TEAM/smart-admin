<template>
  <div>
    <el-card title='基本信息'>
      <div class='detail-title'>
        <el-icon><Bicycle /></el-icon>
        请求地址：{{ detail.url }}
      </div>
      <el-row class='detail-info-box'>
        <el-col :span='16'>
          <el-row class='detail-info'>
            <el-col :span='12'> 用户id：{{ detail.operateUserId }}</el-col>
            <el-col :span='12'> 用户名称： {{ detail.operateUserName }}</el-col>
            <el-col :span='12'> 请求模块： {{ detail.module }}</el-col>
            <el-col :span='12'> 请求日期： {{ detail.createTime }}</el-col>
          </el-row>
          <el-row class='detail-info'>
            <el-col> 请求地址： {{ detail.url }}</el-col>
          </el-row>
          <el-row class='detail-info'>
            <el-col> 请求方法： {{ detail.method }}</el-col>
          </el-row>
          <el-row class='detail-info'>
            <el-col> 请求内容： {{ detail.content }}</el-col>
          </el-row>
        </el-col>
        <el-col :span='8'>
          <p class='detail-right-title'>请求状态</p>
          <p class='detail-right'>{{ detail.successFlag ? '成功' : '失败' }}</p>
        </el-col>
      </el-row>
    </el-card>
    <el-card title='请求参数' class='smart-margin-top10'>
      <json-viewer :value="detail.param ? JSON.parse(detail.param):''" copyable boxed sort />
    </el-card>
    <el-card title='失败原因' class='smart-margin-top10'>
      <div>
        {{ detail.failReason }}
      </div>
    </el-card>
  </div>


</template>

<script setup>
  import { useSpinStore } from '/@/store/modules/spin';
  import { operateLogApi } from '/@/api/support/operate-log-api';
  import { useRoute } from 'vue-router';
  import { onMounted, reactive } from 'vue';

  onMounted(async () => {
    await getDetail();
  });

  const route = useRoute();

  let detail = reactive({});

  async function getDetail() {
    if (!route.query.operateLogId) {
      return;
    }
    try {
      useSpinStore().show();
      let res = await operateLogApi.detail(route.query.operateLogId);
      detail = Object.assign(detail, res.data);
    } catch (e) {
      console.log(e);
    } finally {
      useSpinStore().hide();
    }
  }

  defineExpose({});

</script>

<style scoped lang='scss'>
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
