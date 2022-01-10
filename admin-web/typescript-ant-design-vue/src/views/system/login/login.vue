<!--
 * @Author: zhuoda
 * @Date: 2021-12-03 23:22:28
 * @LastEditTime: 2022-01-07
 * @LastEditors: zhuoda
 * @Description:

-->
<template>
  <div class="login-container">
    <div class="box-item desc">
      <div class="welcome">
        <p>欢迎登录 SmartAdmin V2</p>
        <p class="desc">
          SmartAdmin 是由 河南 · 洛阳 <strong>1024创新实验室（1024Lab）</strong>
          使用SpringBoot2.x 和 Vue3.2 Setup标签、 Composition Api
          (同时支持JavaScript和TypeScript双版本)
          ，开发出的一套简洁、易用的中后台解决方案！
          <br />
          <br />
          <span class="setence">
            致伟大的开发者 ：
            <br />
            我们希望用一套漂亮优雅的代码和一套整洁高效的代码规范，让大家在这浮躁的代码世界里感受到一股把代码写好的清流
            !
            <br />
            保持谦逊，不断学习，热爱代码，更热爱生活 !
            <span class="author">1024创新实验室 ( 2022年 · 洛阳 ）</span>
          </span>
        </p>
      </div>
      <div class="app-qr-box">
        <div class="app-qr">
          <img src="/images/1024lab-gzh.jpg" />
          <span class="qr-desc"> 关注公众号 </span>
        </div>
        <div class="app-qr">
          <img src="/images/1024lab-gzh.jpg" />
          <span class="qr-desc"> 扫码 骚扰卓大 :) </span>
        </div>
      </div>
    </div>
    <div class="box-item login">
      <img class="login-qr" src="/images/login-qr.png" />
      <div class="login-title">账号登录</div>
      <a-form ref="formRef" class="login-form" :model="loginForm" :rules="rules">
        <a-form-item name="loginName">
          <a-input v-model:value.trim="loginForm.loginName" placeholder="请输入用户名" />
        </a-form-item>
        <a-form-item name="password">
          <a-input
            v-model:value="loginForm.password"
            :type="showPassword ? 'text' : 'password'"
            placeholder="请输入密码"
          />
          <div class="eye-box">
            <img
              class="eye-icon"
              v-show="!showPassword"
              @click="onShowPassword"
              src="/images/login-form-open-eyes-close.png"
            />
            <img
              class="eye-icon"
              v-show="showPassword"
              @click="onShowPassword"
              src="/images/login-form-open-eyes.png"
            />
          </div>
        </a-form-item>
        <a-form-item name="loginName">
          <a-input
            class="captcha-input"
            v-model:value.trim="loginForm.captchaCode"
            placeholder="请输入验证码"
          />
          <img class="captcha-img" :src="captchaBase64Image" @click="getCaptcha" />
        </a-form-item>
        <a-form-item>
          <a-checkbox v-model:checked="rememberPwd">记住密码</a-checkbox>
        </a-form-item>
        <a-form-item>
          <div class="btn" @click="handleFinish">登录</div>
        </a-form-item>
      </a-form>
      <div class="more">
        <div class="title-box">
          <p class="line"></p>
          <p class="title">其他方式登录</p>
          <p class="line"></p>
        </div>
        <div class="login-type">
          <img src="/images/wechat-icon.png" />
          <img src="/images/qq-icon.png" />
          <img src="/images/weibo-icon.png" />
          <img src="/images/google-icon.png" />
          <img src="/images/ali-icon.png" />
        </div>
      </div>
    </div>
  </div>
</template>
<script lang="ts" setup>
import { onMounted, onUnmounted, reactive, ref } from "vue";
import { useRouter, useRoute } from "vue-router";
import { loginApi } from "/@/api/system/login/login";
import { message } from "ant-design-vue";
import { useUserStore } from "/@/store/modules/system/user";
import { SmartLoading } from "/@/components/smart-loading";
import { saveTokenToCookie } from "/@/utils/cookie-util";
import { smartSentry } from "/@/lib/smart-sentry";
import { LoginForm } from "/@/api/system/login/model/login-model";

//--------------------- 登录表单 ---------------------------------

const loginForm = reactive<LoginForm>({
  loginName: "",
  password: "",
  captchaCode: "",
  captchaUuid: "",
});

const rules = {
  loginName: [{ required: true, message: "用户名不能为空" }],
  password: [{ required: true, message: "密码不能为空" }],
  captchaCode: [{ required: true, message: "验证码不能为空" }],
};

const showPassword = ref(false);
const router = useRouter();
const route = useRoute();
const formRef = ref();
const rememberPwd = ref(false);

onMounted(() => {
  console.log(route.query);
  document.onkeyup = (e) => {
    if (e.keyCode == 13) {
      handleFinish();
    }
  };
});

onUnmounted(() => {
  document.onkeyup = null;
});

async function handleFinish() {
  formRef.value.validate().then(async () => {
    try {
      SmartLoading.show();
      const res = await loginApi.login(loginForm);
      useUserStore().setUserSession(res.data);
      saveTokenToCookie(res.data.token ? res.data.token : "");
      message.success("登录成功");
      await router.push("/home");
    } catch (e) {
      console.log(e);
      smartSentry.captureException(e);
    } finally {
      SmartLoading.hide();
    }
  });
}

function onShowPassword() {
  showPassword.value = !showPassword.value;
}

//--------------------- 验证码 ---------------------------------
const captchaBase64Image = ref<string>("");
async function getCaptcha() {
  let captchaResult = await loginApi.getCaptcha();
  captchaBase64Image.value = captchaResult.data.captchaBase64Image;
  loginForm.captchaUuid = captchaResult.data.captchaUuid;
}
onMounted(getCaptcha);
</script>
<style lang="less" scoped>
@import "./login.less";
</style>
