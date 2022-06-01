<!--
 * @Description:
 * @version:
 * @Author: zhuoda
 * @Date: 2021-08-12 17:34:23
 * @LastEditors: zhuoda
 * @LastEditTime: 2021-09-01 21:03:33
-->
<template>
  <div class='clearfix'>
    <el-upload
      :accept='props.accept'
      :http-request='customRequest'
      :file-list='fileList'
      list-type='picture-card'
      :headers="{ 'x-access-token': useUserStore().getToken }"
      :before-upload='beforeUpload'
      @on-preview='handlePreview'
      @on-change='handleChange'
      @on-remove='handleRemove'
    >
      <div v-if='fileList.length < props.maxSize'>
        <el-icon>
          <Plus />
        </el-icon>
        <div class='ant-upload-text'>
          {{ buttonText }}
        </div>
      </div>
    </el-upload>
    <el-dialog v-model='previewVisible' @close='handleCancel'>
      <img alt='example' style='width: 100%' :src='previewUrl' />
    </el-dialog>
  </div>
</template>
<script setup>
  import { ref, computed } from 'vue';
  import { fileApi } from '/@/api/support/file-api';
  import { useUserStore } from '/@/store/modules/user';
  import { useSpinStore } from '/@/store/modules/spin';
  import { FILE_FOLDER_TYPE_ENUM } from '/@/constants/business/file';
  import { ElMessage } from 'element-plus';
  // ========================
  const props = defineProps({
    value: String,
    buttonText: {
      type: String,
      default: '点击上传附件',
    },
    showUploadBtn: {
      type: Boolean,
      default: true,
    },
    defaultFileList: {
      type: Array,
      default: () => [],
    },
    multiple: {
      type: Boolean,
      default: false,
    },
    // 最多上传文件数量
    maxUploadSize: {
      type: Number,
      default: 10,
    },
    maxSize: {
      type: Number,
      default: 10,
    },
    // 上传的文件类型
    accept: {
      type: String,
      default: '',
    },
    // 文件上传类型
    folder: {
      type: Number,
      default: FILE_FOLDER_TYPE_ENUM.COMMON.value,
    },
  });

  const emit = defineEmits(['update:value', 'change']);

  // 重新修改图片展示字段
  const files = computed(() => {
    let res = [];
    if (props.fileList && props.fileList.length > 0) {
      props.fileList.forEach((element) => {
        element.url = element.fileUrl;
        res.push(element);
      });
      return res;
    }
    return res;
  });
  // ======================== 逻辑
  const previewVisible = ref(false);
  const fileList = ref(files.value);
  const previewUrl = ref('');

  const customRequest = async (options) => {
    useSpinStore().show();
    try {
      console.log(options);
      const formData = new FormData();
      formData.append('file', options.file);
      let res = await fileApi.uploadFile(
        formData,
        props.folder,
      );
      let file = res.data;
      file.url = file.fileUrl;
      fileList.value.push(file);
      emit('change', fileList.value);
    } catch (e) {
      console.log(e);
    } finally {
      useSpinStore().hide();
    }
  };

  function handleChange(info) {
    let fileStatus = info.file.status;
    let file = info.file;
    if (fileStatus == 'removed') {
      let index = fileList.value.findIndex((e) => e.fileId == file.fileId);
      if (index != -1) {
        fileList.value.splice(index, 1);
        emit('change', fileList.value);
      }
    }
  }

  function handleRemove(file) {
    console.log(fileList.value);
  }

  function beforeUpload(file) {
    const isLimitSize = file.size / 1024 / 1024 < props.maxSize;
    if (!isLimitSize) {
      return ElMessage.error(`上传的文件必须小于${props.maxSize}Mb`);
    }
    return isLimitSize;
  }

  function handleCancel() {
    previewVisible.value = false;
  }

  const handlePreview = async (file) => {
    previewUrl.value = file.url || file.preview;
    previewVisible.value = true;
  };

  function clear() {
    fileList.value = [];
  }

  defineExpose({
    clear,
  });
</script>
<style lang='scss' scoped>
</style>
