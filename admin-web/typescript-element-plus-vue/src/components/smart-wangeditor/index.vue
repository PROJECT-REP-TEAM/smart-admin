<!--
 * @Author: LiHaiFan
 * @Date: 2022-06-05 15:26:04
 * @LastEditTime: 2022-06-05 18:03:12
 * @LastEditors: LiHaiFan
 * @Description: 
 * @FilePath: \typescript-element-plus-vue\src\components\smart-wangeditor\index.vue
-->
<template>
  <div style="border: 1px solid #ccc">
    <Toolbar style="border-bottom: 1px solid #ccc" :editor="editorRef" />
    <Editor
      style="height: 500px; overflow-y: hidden"
      v-model="editorHtml"
      :defaultConfig="editorConfig"
      @onCreated="handleCreated"
      @onChange="handleChange"
    />
  </div>
</template>
<script lang="ts" setup>
  import { shallowRef, onBeforeUnmount, watch, ref } from 'vue';
  import { FILE_FOLDER_TYPE_ENUM } from '/@/constants/business/file';
  import { fileApi } from '/@/api/support/file/file-api';
  import '@wangeditor/editor/dist/css/style.css';
  import { IEditorConfig } from '@wangeditor/editor';
  import { Editor, Toolbar } from '@wangeditor/editor-for-vue';

  //菜单
  const editorConfig: Partial<IEditorConfig> = { MENU_CONF: {} };

  //上传
  let customUpload = {
    async customUpload(file: string | Blob, insertFn: (arg0: any) => void) {
      try {
        const formData = new FormData();
        formData.append('file', file);
        let res = await fileApi.uploadFile(formData, FILE_FOLDER_TYPE_ENUM.COMMON.value);
        let data = res.data;
        insertFn(data.fileUrl);
      } catch (error) {}
    },
  };
  editorConfig.MENU_CONF['uploadImage'] = customUpload;
  editorConfig.MENU_CONF['uploadVideo'] = customUpload;

  // ----------------------- 以下是公用变量 emits props ----------------
  const editorHtml = ref();
  let props = defineProps({
    modelValue: String,
  });
  watch(
    () => props.modelValue,
    (nVal) => {
      console.log(nVal);
      editorHtml.value = nVal;
    },
    {
      immediate: true,
      deep: true,
    }
  );

  // 获取编辑器实例html
  const emit = defineEmits(['update:modelValue']);
  const editorRef = shallowRef();
  const handleCreated = (editor: any) => {
    editorRef.value = editor;
  };
  const handleChange = (editor: any) => {
    emit('update:modelValue', editorHtml.value);
  };

  function getHtml() {
    return editorRef.value.getHtml();
  }
  function getText() {
    return editorRef.value.getText();
  }

  // 组件销毁时，也及时销毁编辑器
  onBeforeUnmount(() => {
    const editor = editorRef.value;
    if (editor == null) return;
    editor.destroy();
  });

  defineExpose({
    editorRef,
    getHtml,
    getText,
  });

  // ----------------------- 以下是业务内容 ----------------------------
</script>
<style scoped lang="scss"></style>
