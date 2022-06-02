<!--
 * @Description: 
 * @Author: zhuoda
 * @Date: 2021-11-05
 * @LastEditTime: 2022-06-02
 * @LastEditors: zhuoda
-->
<template>
  <div class="smart-table-wrapper">
    <a-button type="primary" shape="circle" @click="v2showColumnOperator">
      <template #icon>
        <SettingOutlined />
      </template>
    </a-button>
    <a-button type="primary" shape="circle">
      <template #icon>
        <ExportOutlined />
      </template>
    </a-button>
  </div>
</template>

<script>
import { SettingOutlined, ExportOutlined } from "@ant-design/icons-vue";
import { defineComponent, reactive, UnwrapRef } from "vue";
import _ from "lodash";

export default defineComponent({
  name: "SmartTableOperator",
  components: {
    SettingOutlined,
    ExportOutlined,
  },
  props: {
    columnArray: Array,
    obj: Object,
    text: String,
  },
  methods: {
    v2showColumnOperator() {
      // this.obj.a = 1;
      // this.text = '3';
      // this.columnArray.splice(0,1);

      console.log(2, this.columnArray);
    },
  },
  setup(props) {
    const showColumnOperator = () => {
      // arr.splice(0);
      // props.columnArray =  props.columnArray.splice(0,1);
      console.log(props.columnArray);
    };

    return {
      showColumnOperator,
    };
  },
});
</script>
