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
        <el-form ref="formRef" class="login-form" :model="loginForm" :rules="rules">
          <el-form-item name="loginName">
            <el-input v-model="loginForm.loginName" placeholder="请输入用户名" />
          </el-form-item>
          <el-form-item name="loginPwd">
            <el-input
                v-model="loginForm.loginPwd"
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
  </div>
</template>
<script setup>
import { ElMessage } from "element-plus";
import { onMounted, reactive, ref} from 'vue';
import { useRouter } from "vue-router";
import { useSpinStore } from "/@/store/modules/spin";
import { useUserStore } from "/@/store/modules/user";
import { saveTokenToCookie } from "/@/lib/cookie-util";

let browserWarn = ref(false);
onMounted(browserCheck);

function browserCheck() {
  // 浏览器的userAgent字符串
  let userAgent = navigator.userAgent;
  // 判断是否Opera浏览器
  let isOpera = userAgent.indexOf("Opera") > -1;
  // 判断是否IE浏览器
  let isIE = userAgent.indexOf("compatible") > -1 && serAgent.indexOf("MSIE") > -1 && !isOpera;
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
});
const rules = {
  loginName: [{ required: true, message: "用户名不能为空" }],
  password: [{ required: true, message: "密码不能为空" }],
};
const rememberPwd = ref(false);
const showPwd = ref(false);
const router = useRouter();
const formRef = ref();

async function handleFinish() {
  formRef.value.validate().then(async () => {
    try {
      useSpinStore().show();
      // useUserStore().setUserSession(res.data);
      // saveTokenToCookie(res.data.token ? res.data.token : "");
      // 存储用户菜单与权限
      ElMessage.success("登录成功");
      await router.push("/home");
    } catch (e) {
      console.log(e);
    } finally {
      useSpinStore().hide();
    }
  });
}
function onShowPwd() {
  showPwd.value = !showPwd.value;
}
</script>
<style lang="scss" scoped>
@import "./login.scss";
</style>
