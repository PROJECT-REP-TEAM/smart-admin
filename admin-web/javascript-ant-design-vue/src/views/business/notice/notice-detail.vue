<template>
  <a-card>
    <div class="title">{{ detail.noticeTitle }}</div>
    <div class="info">
      <div class="info_time">
        发布时间：{{ detail.publishTime }}
      </div>
      <div class="info-right">
        <div class="info-editer">
          <img src="/@/assets/images/notice/edit_icon.png"/>
          <span>{{ detail.createUserName }}</span>
        </div>
        <div class="info-views">浏览：{{ detail.watchAmount || 0 }}次</div>
      </div>
    </div>
    <div class="html-content" v-html="detail.noticeContent"></div>
    <div v-if="detail.accessoryFileKeys">
      <div
          v-for="(item, index) in detail.accessoryFileKeys"
          :key="index"
          class="file-list-item"
      >
        <img src="/@/assets/images/notice/file_icon.png"/>
        <span>
          附件：
          <a :href="item.fileUrl" target="_blank">{{ item.fileName }}</a>
        </span>
      </div>
    </div>
  </a-card>
</template>
<script setup>
import {useRoute} from "vue-router";
import {useSpinStore} from "/@/store/modules/system/spin";
import {noticeApi} from "/@/api/business/notice/notice-api";
import {onMounted, reactive} from "vue";

const route = useRoute();
onMounted(() => {
  if (route.query.noticeId) {
    getDetail();
  }
});
// 获取详情
let detail = reactive({});

async function getDetail() {
  try {
    useSpinStore().show();
    let result = await noticeApi.getNoticeDetail(route.query.noticeId);
    await noticeApi.watchNotice(route.query.noticeId);
    Object.assign(detail, result.data)
  } catch (e) {
    console.log(e);
  } finally {
    useSpinStore().hide();
  }
}
</script>
<style lang='less' scoped>
.title {
  font-size: 24px;
  font-weight: bold;
  color: #333333;
  width: 100%;
  text-align: center;
  padding: 12px;
}

.info {
  font-size: 14px;
  font-weight: 400;
  color: #999999;
  padding: 12px 0;
  border-bottom: 1px solid #ededed;
  margin-bottom: 28px;
  display: flex;
  align-items: center;

  .info-right {
    margin-left: auto;
    display: flex;
    align-items: center;

    .info-editer {
      display: flex;
      align-items: center;

      > img {
        width: 15px;
        margin-right: 5px;
      }
    }

    .info-views {
      margin-left: 20px;
    }
  }

}

.html-content {
  min-height: 450px;
}

.file-list-item {
  font-size: 16px;

  > img {
    width: 24px;
  }

  > a {
    color: #0a5aa8;
    border-bottom: 1px solid #0a5aa8
  }
}
</style>
