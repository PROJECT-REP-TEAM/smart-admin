<!--
 * @Description:
 * @version:
 * @Author: zhuoda
 * @Date: 2021-08-12 17:34:23
 * @LastEditors: zhuoda
 * @LastEditTime: 2021-09-01 21:03:33
-->
<template>
  <div class="clearfix">
    <a-upload
      :accept="props.accept"
      :customRequest="customRequest"
      :file-list="fileList"
      list-type="picture-card"
      :headers="{ 'x-access-token': useUserStore().getToken }"
      :before-upload="beforeUpload"
      @preview="handlePreview"
      @change="handleChange"
      @remove="handleRemove"
    >
      <div v-if="fileList.length < props.maxSize">
        <PlusOutlined />
        <div class="ant-upload-text">
          {{ buttonText }}
        </div>
      </div>
    </a-upload>
    <a-modal :visible="previewVisible" :footer="null" @cancel="handleCancel">
      <img alt="example" style="width: 100%" :src="previewUrl" />
    </a-modal>
  </div>
</template>
<script lang="ts" setup>
import { ref, computed } from "vue";
import { message } from "ant-design-vue";
import { fileApi } from "/@/api/business/file/file-api";
import { useUserStore } from "/@/store/modules/system/user";
import { useSpinStore } from "/@/store/modules/system/spin";
import { FILE_FOLDER_TYPE_ENUM } from "/@/constants/business/file";
// =========== 计算属性 获取token =============
const getToken = computed(() => {
  return "";
});

// ========================
interface UploadProps {
  value?: string;
  buttonText?: string;
  showUploadBtn?: boolean;
  defaultFileList?: any;
  multiple?: boolean;
  maxUploadSize?: number; // 最多上传文件数量
  accept?: string; // 上传的文件类型
  maxSize?: number;
  folder?: number; //文件上传类型
  fileList?: Array<FileUploadVo>;
}

const props = withDefaults(defineProps<UploadProps>(), {
  value: undefined,
  buttonText: "点击上传附件",
  showUploadBtn: true,
  defaultFileList: [],
  multiple: false,
  maxUploadSize: 10,
  maxSize: 10,
  accept: "",
  folder: FILE_FOLDER_TYPE_ENUM.COMMON.value,
});

const emit = defineEmits<{
  (e: "update:value", value: any): void;
  (e: "change", value: any): void;
}>();

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
const previewVisible = ref<boolean>(false);
const fileList = ref(files.value);
const previewUrl = ref<string>("");

const customRequest = async (options: any) => {
  useSpinStore().show();
  try {
    console.log(options);
    const formData = new FormData();
    formData.append("file", options.file);
    let res = await fileApi.uploadFile(
      formData,
      props.folder
    );
    let file = res.data;
    file.url = file.fileUrl;
    fileList.value.push(file);
    emit("change", fileList.value);
  } catch (e) {
    console.log(e);
  } finally {
    useSpinStore().hide();
  }
};

function handleChange(info: any) {
  let fileStatus: string = info.file.status;
  let file = info.file;
  if (fileStatus == "removed") {
    let index = fileList.value.findIndex((e) => e.fileId == file.fileId);
    if (index != -1) {
      fileList.value.splice(index, 1);
      emit("change", fileList.value);
    }
  }
}

function handleRemove(file) {
  console.log(fileList.value);
}

function beforeUpload(file) {
  const isLimitSize = file.size / 1024 / 1024 < props.maxSize;
  if (!isLimitSize) {
    return message.error(`上传的文件必须小于${props.maxSize}Mb`);
  }
  return isLimitSize;
}
function handleCancel() {
  previewVisible.value = false;
}
const handlePreview = async (file: any) => {
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
<style lang="less" scoped>
:deep(.ant-upload-picture-card-wrapper) {
  display: flex;
}
</style>
