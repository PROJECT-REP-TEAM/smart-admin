<template>
  <a-dropdown :trigger="['click']" v-model="show" class="header-trigger">

    <div @click="fetchNotice" >
      <a-badge  count="12">
        <div style="width: 26px;height: 26px">
          <BellOutlined  :style="{fontSize: '18px'}" />
        </div>
      </a-badge>
    </div>

    <template #overlay>
      <div>
        <a-spin :spinning="loading">
          <a-tabs class="dropdown-tabs" :tabBarStyle="{textAlign: 'center'}" :style="{width: '297px'}">
            <a-tab-pane tab="通知" key="1">
              <a-list class="tab-pane">
                <a-list-item>
                  <a-list-item-meta title="你收到了 14 份新周报" description="一年前">
                    <a-avatar style="background-color: white" 
                              src="https://gw.alipayobjects.com/zos/rmsportal/ThXAXghbEsBCCSDihZxY.png"/>
                  </a-list-item-meta>
                </a-list-item>
                <a-list-item>
                  <a-list-item-meta title="你推荐的 曲妮妮 已通过第三轮面试" description="一年前">
                    <a-avatar style="background-color: white" 
                              src="https://gw.alipayobjects.com/zos/rmsportal/OKJXDXrmkNshAMvwtvhu.png"/>
                  </a-list-item-meta>
                </a-list-item>
                <a-list-item>
                  <a-list-item-meta title="这种模板可以区分多种通知类型" description="一年前">
                    <a-avatar style="background-color: white" 
                              src="https://gw.alipayobjects.com/zos/rmsportal/kISTdvpyTAhtGxpovNWd.png"/>
                  </a-list-item-meta>
                </a-list-item>
              </a-list>
            </a-tab-pane>
            <a-tab-pane tab="消息" key="2">
              <a-list class="tab-pane"/>
            </a-tab-pane>
            <a-tab-pane tab="待办" key="3">
              <a-list class="tab-pane"/>
            </a-tab-pane>
          </a-tabs>
        </a-spin>
      </div>
    </template>

  </a-dropdown>
</template>

<script >
import {defineComponent, ref} from "vue";
import { BellOutlined } from '@ant-design/icons-vue';

export default defineComponent({
  name: "HeaderNotice",
  components: {BellOutlined},
  setup() {
    const loading = ref(false);
    const show = ref(false);

    const fetchNotice = () => {
      if (loading.value) {
        loading.value = false;
        return;
      }
      loading.value = true;
      setTimeout(() => {
        loading.value = false;
      }, 1000);
    };

    return {
      loading,
      show,
      fetchNotice
    };
  }
});
</script>

<style lang="less">
.header-trigger{
  height: @header-height;
  line-height: @header-height;
}
.header-notice {
  display: inline-block;
  transition: all 0.3s;

  span {
    vertical-align: initial;
  }

  .notice-badge {
    color: inherit;
  }
}

.dropdown-tabs {
  background-color: @base-bg-color;
  box-shadow: 0 2px 8px @shadow-color;
  border-radius: 4px;

  .tab-pane {
    padding: 0 24px 12px;
    min-height: 250px;
  }
}
</style>
