<!--
 * @Author: zhuoda
 * @Date: 2021-08-23 10:22:28
 * @LastEditTime: 2021-11-02 22:48:29
 * @LastEditors: lidoudou
 * @Description:

 * @FilePath: /smart-admin/src/views/system/login/login.vue
-->
<template>
  <div class="login-container">
    <div class="logo-box">
    <img class="logo" src="/images/logo.png" />
    </div>
    <div class="login-box">
      <div class="box-item">
        <div class="welcome">
          <p>欢迎登录SmartAdmin</p>
          <p class="desc">保持谦逊，不断学习，热爱代码，热爱生活 !</p>
        </div>
        <div class="login-person">
          <div class="img-box">
            <img src="/images/login-person.png" />
          </div>
          <span class="qr-desc">SmartAdmin2.0 由1024创新实验室强力驱动</span>
        </div>
      </div>
      <div class="box-item login">
        <img class="login-qr" src="/images/login-qr.png" />
        <div class="login-title">账号登录</div>
        <a-form ref="formRef" class="login-form" :model="loginForm" :rules="rules">
          <a-form-item name="loginName">
            <a-input v-model:value="loginForm.loginName" placeholder="请输入用户名" />
          </a-form-item>
          <a-form-item name="loginPwd">
            <a-input
              v-model:value="loginForm.loginPwd"
              :type="showPwd ? 'text' : 'password'"
              placeholder="请输入密码"
            />
            <div class="eye-box">
              <img
                class="eye-icon"
                v-show="!showPwd"
                @click="onShowPwd"
                src="/images/login-form-open-eyes-close.png"
              />
              <img
                class="eye-icon"
                v-show="showPwd"
                @click="onShowPwd"
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
  </div>
</template>
<script lang="ts" setup>
import { reactive, ref } from "vue";
import { useRouter } from "vue-router";
import { loginApi } from "@/api/system/login/login";
import { message } from "ant-design-vue";
import { useUserStore } from "/@/store/modules/system/user";
import { SmartLoading } from "/@/components/smart-loading";
import { saveTokenToCookie } from "/@/utils/cookie-util";
// eslint-disable-next-line no-undef
const loginForm = reactive({
  loginName: "",
  loginPwd: "",
});
const rules = {
  loginName: [{ required: true, message: "用户名不能为空" }],
  loginPwd: [{ required: true, message: "密码不能为空" }],
};

const showPwd = ref(false);
const router = useRouter();
const formRef = ref();
const rememberPwd = ref(false);

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
    } finally {
      SmartLoading.hide();
    }
  });
}
function onShowPwd() {
  showPwd.value = !showPwd.value;
}
</script>
<style lang="less" scoped>
@import "./login.less";
</style>
