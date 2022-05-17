<template>
  <div>
    <a-popover
        :trigger="trigger"
        placement="bottomLeft"
        v-model="visible"
    >
      <template #title>
        <a-input-search
            v-model="searchValue"
            placeholder="输入英文关键词进行搜索"
            @change="filterIcon"
        />
      </template>

      <template #content>
        <div class="icon-box">
          <div
              v-for="(item, index) in iconArr"
              :key="item"
              @click="handleClick(item)"
              class="icon-content"
              :style="{ background: icon === item ? '#268961' : ''}"
          >
            <component :is="$antIcons[item]" />
          </div>
          <div  v-if="iconIndex<icons.length" class="icon-content" @click="moreIcon">
            <div class="more-icon">
              更多
            </div>
          </div>
        </div>
      </template>
      <slot name="iconSelect"></slot>
    </a-popover>
  </div>
</template>

<script lang="ts" setup>
import * as VueIcon from '@ant-design/icons-vue';
import {onMounted, reactive, ref, watch} from "vue";

const icons = Object.keys(VueIcon);
let iconIndex = ref(27);
let iconArr = reactive<String[]>([...icons.filter((e,index)=>index<iconIndex.value)]);

function moreIcon(){
  let maxIndex = iconIndex.value + 28;
  let more = Object.keys(VueIcon).filter((e,index)=>index>=iconIndex.value && index<maxIndex);
  iconArr.push(...more);
  iconIndex.value = maxIndex;
  console.log(iconIndex.value)
}

let searchValue = ref();
function filterIcon() {
  if (searchValue.value){
    iconArr = iconArr.filter(item => item.toLowerCase().includes(this.searchValue.toLowerCase()) )
  }
}
let visible = ref(false);
watch(
    () => visible.value,
    (newValue) => {
      searchValue.value='';
      iconArr = [...icons];
    }
);

const emit = defineEmits<{
  (e: "updateIcon", value: String): void;
}>();

function handleClick(icon: String) {
  emit('updateIcon',icon)
  visible.value = false;
}


</script>

<style scoped lang="less">
  .icon-box{
    overflow: auto;
    font-size: 20px;
    width: 250px;
    height: 230px;
    display: flex;
    flex-wrap: wrap;
    flex-direction: row;
    align-content: flex-start;
    justify-content: center;
  }

  .icon-content{
    width: 45px;
    height: 40px;
    margin:  5px;
    cursor: pointer;
    text-align: center;
    border-radius: 6px;
    border: 1px solid #ccc;
    .more-icon{
      font-size: 14px;
      margin: 5px;
    }

  }
  .icon-content:hover{
    background: #1890ff;
  }
</style>
