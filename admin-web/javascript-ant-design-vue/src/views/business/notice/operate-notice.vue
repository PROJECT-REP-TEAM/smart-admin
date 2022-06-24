<template>
  <a-card class="box-card">
    <a-form ref="formRef" :model="form" :rules="rules" laba-width="100px">
      <a-form-item label="公告类型" prop="noticeType">
        <smart-enum-select v-model:value="form.noticeType" enum-name="NOTICE_TYPE_ENUM" placeholder="请选择通知类型"
                           style="width: 100%"/>
      </a-form-item>

      <a-form-item label="所属栏目" prop="noticeBelongType">
        <smart-enum-select v-model:value="form.noticeBelongType" enum-name="NOTICE_BELONG_TYPE_ENUM" placeholder="请选择所属栏目"
                           style="width: 100%"/>
      </a-form-item>

      <a-form-item label="发布时间" prop="publishTime">
        <a-date-picker v-model:value="form.publishTime"
                       :allowClear="false"
                       format="YYYY-MM-DD HH:mm:ss"
                       style="width: 200px" />
      </a-form-item>

      <a-form-item label="文章标题" prop="noticeTitle">
        <a-input v-model:valiue="form.noticeTitle" placeholder="请输入文章标题"></a-input>
      </a-form-item>
      <a-form-item
          v-if="form.noticeType === NOTICE_TYPE_ENUM.LINK.value"
          label="链接地址"
      >
        <a-input v-model:value="form.linkAddress" placeholder="请输入链接地址"></a-input>
      </a-form-item>
      <a-form-item label="是否置顶" prop="topFlag">
      </a-form-item>
      <a-form-item
          label="上传封面"
          prop="coverFileKey"
      >
      </a-form-item>
      <a-form-item label="显示状态" prop="disabledFlag">
      </a-form-item>
      <a-form-item
          v-if="form.noticeType === NOTICE_TYPE_ENUM.DOCUMENT.value"
          label="上传附件"
          :prop="form.noticeType === NOTICE_TYPE_ENUM.DOCUMENT.value ? 'accessoryFileKeys' : ''"
      >
      </a-form-item>
      <a-form-item
          v-show="form.noticeType === NOTICE_TYPE_ENUM.ARTICLE.value"
          label="文章内容"
          :prop="form.noticeType === NOTICE_TYPE_ENUM.ARTICLE.value ? 'noticeContent' : ''"
      >
        <SmartWangeditor ref="smartWangeditor" :moda-value="form.noticeContent"></SmartWangeditor>
      </a-form-item>
    </a-form>
    <span class="dialog-footer">
      <a-button @click="useUserStore().closePage(route, router)">取 消</a-button>
      <a-button type="primary" @click="submit">发 布</a-button>
    </span>
  </a-card>
</template>
<script setup>
import SmartEnumSelect from "/@/components/smart-enum-select/index.vue";
import { ref, reactive, onMounted } from 'vue';
import _ from 'lodash';
import { useSpinStore } from '/@/store/modules/system/spin';
import { NOTICE_TYPE_ENUM } from '/@/constants/business/notice-const.js';
import SmartWangeditor from '/@/components/smart-wangeditor/index.vue';
import { useUserStore } from '/@/store/modules/system/user';
import { useRoute, useRouter } from 'vue-router';
import { noticeApi } from "/@/api/business/notice/notice-api"
// ----------------------- 以下是业务内容 ----------------------------
const formRef = ref();

const formDefault = {
  noticeId: undefined,
  noticeType: undefined,
  noticeBelongType: undefined,
  noticeTitle: "",
  noticeContent: "",
  linkAddress: "",
  coverFileKey: "",
  accessoryFileKeys: "",
  topFlag: false,
  publishTime:undefined,
  disabledFlag:false
};
let form = reactive({ ...formDefault });
let smartWangeditor = ref();
const rules = {
  noticeType: [{ required: true, message: "请选择通知类型" }],
  noticeBelongType: [{ required: true, message: "请选择所属栏目" }],
  noticeTitle: [{ required: true, message: "公告标题不能为空" }],
  publishTime: [{ required: true, message: "请选择发布时间" }],
  topFlag: [{ required: true, message: "请选择是否置顶" }],
  disabledFlag: [{ required: true, message: "请选择状态" }],
};

const route = useRoute();
const router = useRouter();

onMounted(() => {
  getDetail();
});

// 提交表单
function submit() {
  formRef.value.validate((valid) => {
    if (!valid) {
      message.warning('数据校验失败');
      return;
    }
    save();
  });
}

// 保存更新
async function save() {
  try {
    useSpinStore().show();
    let params = _.cloneDeep(form.value);
    params.noticeContent = smartWangeditor.value.getInstanceHtml();
    if (params.noticeContent) {
      params.noticeContent = replaceHttp(params.noticeContent);
    }
    if (form.noticeType === NOTICE_TYPE_ENUM.ARTICLE.value && !params.noticeContent) {
      message.warning('请完善文章内容');
      return;
    }
    if (route.query.id) {
      await noticeApi.updateNotice(params);
      message.success('修改成功');
    } else {
      await noticeApi.createNotice(params);
      message.success('添加成功');
    }
    useUserStore().closePage(route, router);
  } catch (e) {
    console.log(e);
  } finally {
    useSpinStore().hide();
  }
}

// 获取详情
async function getDetail() {
  if (!route.query.noticeId) {
    return;
  }
  try {
    useSpinStore().show();
    let result = await noticeApi.getNoticeDetail(route.query.noticeId);
    Object.assign(form,result.data)
  } catch (e) {
    console.log(e);
  } finally {
    useSpinStore().hide();
  }
}
</script>
<style lang='less' scoped></style>
