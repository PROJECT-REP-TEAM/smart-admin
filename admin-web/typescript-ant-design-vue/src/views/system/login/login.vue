<!--
 * @Author: zhuoda
 * @Date: 2021-12-03 23:22:28
 * @LastEditTime: 2021-12-13
 * @LastEditors: zhuoda
 * @Description:

-->
<template>
  <div class="login-container">
    <div class="box-item desc">
      <div class="welcome">
        <p>欢迎登录 SmartAdmin V2</p>
        <p class="desc">
          SmartAdmin v2.x 是由河南·洛阳 1024创新实验室
          团队研发的一套互联网企业级的通用型中后台前端解决方案！ 使用最新 SpringBoot 和
          Vue3.2 Composition Api（支持TypeScript和JavaScript双版本）
          ，开发出的一套简洁、易用的中后台解决方案，为广大开发者们节省时间！
        </p>
        <hr />
        <p class="desc">
          我们希望用一套漂亮优雅的代码和一套整洁高效的代码规范，让大家在这浮躁的代码世界里感受到一股把代码写好的清流！
          同时对我们而言，这不仅是一套系统，也是我们团队的一次自我审视与成长。身为开发者，我们希望在一次次的披荆斩棘之后，能收获和留下些什么，这正是我们存在的意义！
          <br />
          保持谦逊，不断学习，热爱代码，热爱生活 !
        </p>
      </div>
      <div class="app-qr-box">
        <div class="app-qr">
          <img src="/images/1024lab-gzh.jpg" />
          <span class="qr-desc"> 关注公众号 </span>
        </div>
        <div class="app-qr">
          <img src="/images/1024lab-gzh.jpg" />
          <span class="qr-desc"> 关注公众号 </span>
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

const loginForm = reactive<LoginForm>({
  loginName: "",
  password: "",
});

const rules = {
  loginName: [{ required: true, message: "用户名不能为空" }],
  password: [{ required: true, message: "密码不能为空" }],
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
      // 存储用户菜单与权限
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
</script>
<style lang="less" scoped>
@import "./login.less";
</style>
