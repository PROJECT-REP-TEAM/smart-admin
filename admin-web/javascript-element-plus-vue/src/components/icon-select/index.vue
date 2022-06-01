<template>
  <div>
    <el-popover placement='bottom-start' :width='264'>
      <!--      <template #title>-->
      <!--        <el-input-->
      <!--          v-model='searchValue'-->
      <!--          placeholder='输入英文关键词进行搜索'-->
      <!--          @change='filterIcon'-->
      <!--        />-->
      <!--      </template>-->
      <template #reference>
        <slot name='iconSelect'></slot>
      </template>
      <div class='icon-box'>
        <div
          v-for='(item, index) in iconArr'
          :key='item'
          @click='handleClick(item)'
          class='icon-content'
          :style="{ background: icon === item ? '#268961' : ''}"
        >
          <component :is='$icons[item]' />
        </div>
        <div v-if='iconIndex<icons.length' class='icon-content' @click='moreIcon'>
          <div class='more-icon'>
            更多
          </div>
        </div>
      </div>
    </el-popover>
  </div>
</template>

<script setup>
  import * as vueIcons from '@element-plus/icons';
  import { reactive, ref } from 'vue';

  const icons = Object.keys(vueIcons);
  let iconIndex = ref(27);
  let iconArr = reactive([...icons.filter((e, index) => index < iconIndex.value)]);

  function moreIcon() {
    let maxIndex = iconIndex.value + 28;
    let more = icons.filter((e, index) => index >= iconIndex.value && index < maxIndex);
    iconArr.push(...more);
    iconIndex.value = maxIndex;
    console.log(iconIndex.value);
  }

  let searchValue = ref();

  function filterIcon() {
    if (searchValue.value) {
      iconArr = iconArr.filter(item => item.toLowerCase().includes(searchValue.value.toLowerCase()));
    }
  }

  const emit = defineEmits(['updateIcon']);

  function handleClick(icon) {
    emit('updateIcon', icon);
  }


</script>

<style scoped lang='scss'>
  .icon-box {
    overflow-y: auto;
    font-size: 20px;
    width: 250px;
    height: 230px;
    display: flex;
    flex-wrap: wrap;
    flex-direction: row;
    align-content: flex-start;
    justify-content: center;
  }

  .icon-content {
    width: 45px;
    height: 40px;
    margin: 5px;
    cursor: pointer;
    text-align: center;
    border-radius: 6px;
    border: 1px solid #ccc;
    line-height: 40px;

    .more-icon {
      font-size: 14px;
    }

  }

  .icon-content:hover {
    background: #1890ff;
  }
</style>
