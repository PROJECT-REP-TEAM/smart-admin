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
            我们希望用一套漂亮优雅的代码和一套整洁高效的代码规范，让大家在这浮躁的世界里感受到一股把代码写好的清流
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
      <el-form ref="formRef" class="login-form" :model="loginForm" :rules="rules">
        <el-form-item name="loginName">
          <el-input v-model="loginForm.loginName" placeholder="请输入用户名" />
        </el-form-item>
        <el-form-item name="password">
          <el-input
              v-model="loginForm.password"
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
        </el-form-item>
        <el-form-item name="loginName">
          <el-input
              class="captcha-input"
              v-model="loginForm.captchaCode"
              placeholder="请输入验证码"
          />
          <img class="captcha-img" :src="captchaBase64Image" @click="getCaptcha" />
        </el-form-item>
        <el-form-item>
          <el-checkbox v-model="rememberPwd">记住密码</el-checkbox>
        </el-form-item>
        <el-form-item>
          <div class="btn" @click="handleFinish">登录</div>
        </el-form-item>
      </el-form>
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
<script setup>
import { ElMessage } from "element-plus";
import { onMounted, onUnmounted, reactive, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { useSpinStore } from "/@/store/modules/spin";
import { useUserStore } from "/@/store/modules/user";
import { saveTokenToCookie } from "/@/lib/cookie-util";
import { loginApi } from "/@/api/system/login";
import { DEVICE_ENUM } from '/@/constants/system/device';

let browserWarn = ref(false);
onMounted(browserCheck);

function browserCheck() {
  // 浏览器的userAgent字符串
  let userAgent = navigator.userAgent;
  // 判断是否Opera浏览器
  let isOpera = userAgent.indexOf("Opera") > -1;
  // 判断是否IE浏览器
  let isIE = userAgent.indexOf("compatible") > -1 && userAgent.indexOf("MSIE") > -1 && !isOpera;
  // 判断是否Safari浏览器
  let isSafari = userAgent.indexOf("Safari") > -1 && userAgent.indexOf("Chrome") == -1;
  if (isSafari) {
    browserWarn.value = true;
  }
  if (isIE) {
    browserWarn.value = true;
  }
}

// eslint-disable-next-line no-undef
const loginForm = reactive({
  loginName: "",
  password: "",
  captchaCode: "",
  captchaUuid: "",
  loginDevice: DEVICE_ENUM.PC.value
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
      useSpinStore().show();
      const res = await loginApi.login(loginForm);
      useUserStore().setUserSession(res.data);
      saveTokenToCookie(res.data.token ? res.data.token : "");
      // 存储用户菜单与权限
      ElMessage.success("登录成功");
      await router.push("/home");
    } catch (e) {
      console.log(e);
      await getCaptcha();
    } finally {
      useSpinStore().hide();
    }
  });
}
function onShowPassword() {
  showPassword.value = !showPassword.value;
}
//--------------------- 验证码 ---------------------------------
const captchaBase64Image = ref("");
async function getCaptcha() {
  let captchaResult = await loginApi.getCaptcha();
  captchaBase64Image.value = captchaResult.data.captchaBase64Image;
  loginForm.captchaUuid = captchaResult.data.captchaUuid;
  loginForm.captchaCode = "";
}
onMounted(getCaptcha);
</script>
<style lang="scss" scoped>
@import "./login.scss";
</style>
