<!--
 * @Description: 
 * @Author: zhuoda
 * @Date: 2021-08-03
 * @LastEditTime: 2021-08-27
 * @LastEditors: zhuoda
-->
<template>
  <div class="header-search">
    <a-icon type="search" class="search-icon" @click="enterSearchMode" />
    <a-auto-complete
      ref="input"
      :getPopupContainer="
        (e) => {
          return e.parentNode || document.body;
        }
      "
      :class="['search-input', searchMode ? 'enter' : 'leave']"
      placeholder="站内搜索"
      @blur="leaveSearchMode"
    />
  </div>
</template>

<script lang="ts">
import { defineComponent, ref } from "vue";

const dataSource: string[] = ["选项一", "选项二"];

export default defineComponent({
  name: "HeaderSearch",
  components: {},
  setup(props, { emit }) {
    const searchMode = ref(false);

    const input = ref<HTMLElement | null>(null);
    const enterSearchMode = () => {
      searchMode.value = true;
      emit("active", true);
      setTimeout(() => input.value?.focus(), 300);
    };
    const leaveSearchMode = () => {
      searchMode.value = false;
      setTimeout(() => emit("active", false), 300);
    };
    return {
      input,
      searchMode,
      dataSource,
      enterSearchMode,
      leaveSearchMode,
    };
  },
});
</script>

<style lang="less">
.header-search {
  .search-icon {
    font-size: 16px;
    cursor: pointer;
  }

  .search-input {
    border: 0;
    border-bottom: 1px solid @border-color-split;
    transition: width 0.3s ease-in-out;

    input {
      border: 0;
      box-shadow: 0 0 0 0;
    }

    &.leave {
      width: 0px;

      input {
        display: none;
      }
    }

    &.enter {
      width: 200px;

      input:focus {
        box-shadow: 0 0 0 0;
      }
    }
  }
}
</style>
