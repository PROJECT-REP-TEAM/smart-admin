<template>
  <a-card class="box-card">
    <a-form ref="formRef"
            :model="form"
            :rules="rules">
      <a-form-item label="公告类型" name="noticeType">
        <smart-enum-select v-model:value="form.noticeType" class="form-width" enum-name="NOTICE_TYPE_ENUM"
                           placeholder="请选择通知类型"/>
      </a-form-item>

      <a-form-item label="所属栏目" name="noticeBelongType">
        <smart-enum-select v-model:value="form.noticeBelongType" class="form-width"
                           enum-name="NOTICE_BELONG_TYPE_ENUM"
                           placeholder="请选择所属栏目"/>
      </a-form-item>

      <a-form-item label="发布时间" name="publishTime">
        <a-date-picker v-model:value="form.publishTime"
                       :allowClear="false"
                       class="form-width"
                       format="YYYY-MM-DD HH:mm:ss"/>
      </a-form-item>

      <a-form-item label="公告标题" name="noticeTitle">
        <a-input v-model:value="form.noticeTitle" class="form-width" placeholder="请输入文章标题" />
      </a-form-item>
      <a-form-item
          v-if="form.noticeType === NOTICE_TYPE_ENUM.LINK.value"
          label="链接地址"
      >
        <a-input v-model:value="form.linkAddress" class="form-width" placeholder="请输入链接地址"></a-input>
      </a-form-item>
      <a-form-item label="是否置顶" name="topFlag">
        <a-switch v-model:checked="form.topFlag" checked-children="是" un-checked-children="否"/>
      </a-form-item>
      <a-form-item
          label="上传封面"
          name="coverFileKey"
      >
        <Upload accept=".jpg,.jpeg,.png,.gif" buttonText="点击上传封面"></Upload>
      </a-form-item>
      <a-form-item label="禁用状态" name="disabledFlag">
        <a-switch v-model:checked="form.disabledFlag" checked-children="禁用" un-checked-children="启用"/>
      </a-form-item>
      <a-form-item
          v-if="form.noticeType === NOTICE_TYPE_ENUM.DOCUMENT.value"
          :name="form.noticeType === NOTICE_TYPE_ENUM.DOCUMENT.value ? 'accessoryFileKeys' : ''"
          label="上传附件"
      >
        <Upload :maxUploadSize="5" :multiple="true" accept=".doc,.docx,.xls,.xlsx,.ppt,.pptx,.zip,.jar,.7z"></Upload>
      </a-form-item>
      <a-form-item
          v-show="form.noticeType === NOTICE_TYPE_ENUM.ARTICLE.value"
          :name="form.noticeType === NOTICE_TYPE_ENUM.ARTICLE.value ? 'noticeContent' : ''"
          label="文章内容"
      >
        <SmartWangeditor ref="smartWangeditor" v-model="form.noticeContent"></SmartWangeditor>
      </a-form-item>
    </a-form>
    <a-space class="footer">
      <a-button @click="useUserStore().closePage(route, router)">取 消</a-button>
      <a-button type="primary" @click="submit">发 布</a-button>
    </a-space>
  </a-card>
</template>
<script setup>
import SmartEnumSelect from "/@/components/smart-enum-select/index.vue";
import {onMounted, reactive, ref} from 'vue';
import _ from 'lodash';
import {useSpinStore} from '/@/store/modules/system/spin';
import {NOTICE_TYPE_ENUM} from '/@/constants/business/notice-const.js';
import SmartWangeditor from '/@/components/smart-wangeditor/index.vue';
import {useUserStore} from '/@/store/modules/system/user';
import {useRoute, useRouter} from 'vue-router';
import {noticeApi} from "/@/api/business/notice/notice-api"
import Upload from "/@/components/upload/index.vue"
import dayjs from "dayjs";
import {message} from "ant-design-vue";
// ----------------------- 以下是业务内容 ----------------------------
const formRef = ref();

const formDefault = {
  noticeId: undefined,
  noticeType: NOTICE_TYPE_ENUM.ARTICLE.value,
  noticeBelongType: undefined,
  noticeTitle: "",
  noticeContent: "",
  linkAddress: "",
  coverFileKey: "",
  accessoryFileKeys: "",
  topFlag: false,
  publishTime: dayjs(),
  disabledFlag: false
};
let form = reactive({...formDefault});
let smartWangeditor = ref();
const rules = {
  noticeType: [{required: true, message: "请选择通知类型", trigger: 'change'}],
  noticeBelongType: [{required: true, message: "请选择所属栏目", trigger: 'change'}],
  noticeTitle: [{required: true, message: "公告标题不能为空", trigger: 'change'}],
  publishTime: [{required: true, message: "请选择发布时间", trigger: 'change'}],
  topFlag: [{required: true, message: "请选择是否置顶", trigger: 'change'}],
  disabledFlag: [{required: true, message: "请选择状态", trigger: 'change'}],
};

const route = useRoute();
const router = useRouter();

onMounted(() => {
  if (route.query.noticeId) {
    getDetail();
  }
});

// 提交表单
function submit() {
  formRef.value
      .validate()
      .then(() => {
        save();
      })
      .catch((error) => {
        console.log("error", error);
        message.error("参数验证错误，请仔细填写表单数据!");
      });
}

// 保存更新
async function save() {
  try {
    useSpinStore().show();
    let params = _.cloneDeep(form);
    params.noticeContent = smartWangeditor.value.getHtml();
    if (params.noticeContent) {
      // params.noticeContent = replaceHttp(params.noticeContent);
    }
    // 日期格式化
    params.publishTime = params.publishTime.format("YYYY-MM-DD HH:mm:ss");
    if (form.noticeType === NOTICE_TYPE_ENUM.ARTICLE.value && !params.noticeContent) {
      message.warning('请完善文章内容');
      return;
    }
    if (params.noticeId) {
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
  try {
    useSpinStore().show();
    let result = await noticeApi.getNoticeDetail(route.query.noticeId);
    Object.assign(form, result.data)
    form.publishTime = dayjs(form.publishTime,"YYYY-MM-DD HH:mm:ss")
  } catch (e) {
    console.log(e);
  } finally {
    useSpinStore().hide();
  }
}
</script>
<style lang='less' scoped>
.form-width {
  width: 100% !important;
}

.footer {
  width: 100%;
  display: flex;
  justify-content: flex-end;
}
</style>
