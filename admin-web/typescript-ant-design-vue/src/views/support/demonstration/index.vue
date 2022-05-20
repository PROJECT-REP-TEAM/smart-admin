<template>
  <div>
    <a-card title="地区选择" class="smart-margin-top10">
      <smart-area-cascader type="province_city_district"
                           :width="200"
                           v-model:value="area"
                           @change="changeArea" />
      <json-viewer :value="selectArea" copyable boxed sort class="smart-margin-top10" />
    </a-card>
    <a-card title="下拉组件" class="smart-margin-top10">
      <a-row class="smart-margin-top10">
        <a-col :span="12">
          枚举选择:
          <smart-enum-select :width="200"
                             v-model:value="goodsType"
                             placeholder="请选择类型"
                             enum-name="GOODS_TYPE_ENUM" />
        </a-col>
        <a-col :span="12">
          值：{{goodsType}}
        </a-col>
      </a-row>
      <a-row class="smart-margin-top10">
        <a-col :span="12">
          真假选择:
          <smart-boolean-select :width="200"
                             v-model:value="trueFalse"
                             placeholder="请选择" />
        </a-col>
        <a-col :span="12">
          值：{{trueFalse}}
        </a-col>
      </a-row>
    </a-card>
    <a-card title="文件上传" class="smart-margin-top10">
      <smart-upload ref="uploadRef"
                    :folder="FILE_FOLDER_TYPE_ENUM.COMMON.value"
                    :showUploadBtn="true"
                    :maxSize="20"
                    :multiple="true"
                    accept="image/*"
                    buttonText=""
                    mini
                    @change="changeFile" />
      <json-viewer :value="attachment" copyable boxed sort class="smart-margin-top10"/>
    </a-card>
  </div>


</template>

<script setup lang="ts">
import {reactive, ref,} from 'vue';
import { SmartAreaOption } from '/@/components/smart-area-cascader/smart-area-option';
import SmartAreaCascader from '/@/components/smart-area-cascader/index.vue';
import SmartEnumSelect from '/@/components/smart-enum-select/index.vue';
import SmartBooleanSelect from '/@/components/smart-boolean-select/index.vue';
import SmartUpload from '/@/components/smart-upload/index.vue';
import { FILE_FOLDER_TYPE_ENUM } from '/@/constants/business/file';
import {FileUploadVo} from "/@/api/support/file/model/file-upload-vo";
import _ from 'lodash';

const area = ref<SmartAreaOption[]>([]);
area.value = [
  {
    value: 110100,
    label: '北京市',
  },
  {
    value: 110100,
    label: '北京市',
  },
  {
    value: 110105,
    label: '朝阳区',
  },
] as SmartAreaOption[];

let selectArea = reactive<SmartAreaOption>({});
let goodsType = ref();
let trueFalse = ref();
let attachment = reactive<FileUploadVo>({});

function changeFile(fileList: any) {
  attachment = fileList;
}

function changeArea(value: number[], selectedOptions: SmartAreaOption[]) {
  if (!_.isEmpty(selectedOptions)) {
    // 地区信息
    selectArea.province = area.value[0].value;
    selectArea.provinceName = area.value[0].label;

    selectArea.city = area.value[1].value;
    selectArea.cityName = area.value[1].label;
    if (area.value[2]) {
      selectArea.district = area.value[2].value;
      selectArea.districtName = area.value[2].label;
    }
  }
}

defineExpose({});

</script>

<style scoped  lang="less">

</style>
