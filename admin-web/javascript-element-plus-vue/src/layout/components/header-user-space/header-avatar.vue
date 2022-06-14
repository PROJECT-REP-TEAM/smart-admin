<template>
  <el-dropdown class="header-trigger">
    <div class="user-info" style="cursor: pointer">
      <span class="name">{{ userInfo.actualName }}</span>
      <el-avatar style="margin: 0 10px" :size="24">
        {{ avatarName }}
      </el-avatar>
    </div>
    <template #dropdown>
      <el-dropdown-menu class="avatar-menu">
        <el-dropdown-item @click="showUpdatePwdModal">修改密码</el-dropdown-item>
      </el-dropdown-menu>
      <el-dropdown-menu class="avatar-menu">
        <el-dropdown-item @click="onLogout">退出登录</el-dropdown-item>
      </el-dropdown-menu>
    </template>
  </el-dropdown>
  <el-dialog title="修改密码" v-model="visible">
    <el-form
      ref="formRef"
      :model="form"
      :rules="rules"
    >
      <el-form-item label="原密码" prop="oldPassword">
        <el-input
          v-model="form.oldPassword"
          type="password"
          placeholder="请输入原密码"
        />
      </el-form-item>
      <el-form-item label="新密码" prop="newPassword">
        <el-input v-model="form.newPassword" type="password" placeholder="请输入新密码" />
      </el-form-item>
      <el-form-item label="确认密码" prop="confirmPwd">
        <el-input
          v-model="form.confirmPwd"
          type="password"
          placeholder="请输入确认密码"
        />
      </el-form-item>
    </el-form>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="visible = false">取 消</el-button>
        <el-button type="primary" @click="updatePwd">确 定</el-button>
      </span>
    </template>
  </el-dialog>
</template>
<script setup>
  import { computed, ref, reactive } from '@vue/reactivity';
  import { onMounted } from '@vue/runtime-core';
  import { useRouter } from 'vue-router';
  import { useUserStore } from '/@/store/modules/user';
  import { clearAllCoolies } from '/@/lib/cookie-util';
  import { localClear } from '/@/lib/local-util';
  import { employeeApi } from '/@/api/system/employee-api';
  import { smartSentry } from '/@/lib/smart-sentry';
  import { useSpinStore } from '/@/store/modules/spin';
  import { ElMessage } from 'element-plus';
  // 头像背景颜色
  const AVATAR_BACKGROUND_COLOR_ARRAY = ['#f56a00', '#87d068', '#1890ff'];

  // ----------------------- 以下是字段定义 emits props ---------------------
  const avatarName = ref('');
  const router = useRouter();
  // ----------------------- 以下是计算属性 watch监听 ------------------------
  const userInfo = computed(() => useUserStore().getUserInfo);

  // ----------------------- 以下是方法 ------------------------------------
  const onLogout = () => {
    localClear();
    clearAllCoolies();
    useUserStore().logout();
    router.push({ name: 'Login' });
  };

  function updateAvatar() {
    const name = useUserStore().getUserInfo.actualName;
    if (name) {
      avatarName.value = name.substr(0, 1);
      const avatar = document.getElementById('smartAdminAvatar');
      if (avatar) {
        avatar.style.backgroundColor = AVATAR_BACKGROUND_COLOR_ARRAY[hashcode(avatarName.value) % 3];
      }
    }
  }

  function hashcode(str) {
    let hash = 0,
      i,
      chr;
    if (str.length === 0) return hash;
    for (i = 0; i < str.length; i++) {
      chr = str.charCodeAt(i);
      hash = (hash << 5) - hash + chr;
      hash |= 0; // Convert to 32bit integer
    }
    return hash;
  }

  const visible = ref(false);
  //  组件
  const formRef = ref();
  const formDefault = {
    oldPassword: '',
    newPassword: ''
  };
  let form = reactive({
    ...formDefault
  });
  const rules = {
    oldPassword: [{ required: true, message: '请输入原密码' }],
    newPassword: [
      { required: true, message: '请输入新密码' },
      { message: '密码不能小于6位', min: 6 },
      { pattern: '^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])[a-zA-Z0-9]{6,10}$', message: '必须包含大小写字母和数字的组合，不能使用特殊字符，长度在 6-10 之间' }
    ],
    confirmPwd: [
      { required: true, message: '请输入确认密码' },
      { message: '密码不能小于6位', min: 6 },
      { pattern: '^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])[a-zA-Z0-9]{6,10}$', message: '必须包含大小写字母和数字的组合，不能使用特殊字符，长度在 6-10 之间' }
    ]
  };

  function showUpdatePwdModal() {
    visible.value = true;
  }
  async function updatePwd() {
    formRef.value
      .validate()
      .then(async () => {
        if (form.newPassword != form.confirmPwd) {
          ElMessage.error('新密码与确认密码不一致');
          return;
        }
        useSpinStore().show();
        try {
          await employeeApi.updateEmployeePassword(form);
          ElMessage.success('修改成功');
          visible.value = false;
        } catch (error) {
          console.log(error);
          smartSentry.captureException(error);
        } finally {
          useSpinStore().hide();
        }
      })
      .catch((error) => {
        console.log('error', error);
        ElMessage.error('参数验证错误，请仔细填写表单数据!');
      });
  }

  onMounted(updateAvatar);

  // ----------------------- 以下是暴露的方法内容 ----------------------------
  defineExpose({});
</script>
<style lang="scss">
  .user-info {
    display: flex;
    align-items: center;
    justify-content: center;
  }
</style>
