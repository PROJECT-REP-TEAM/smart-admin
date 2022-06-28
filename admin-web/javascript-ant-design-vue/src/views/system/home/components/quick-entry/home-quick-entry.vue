<template>
  <div class="card-container">
    <a-card style="height: 100%" size="small">
      <template #title>
        <div class="title">
          <thunderbolt-two-tone :style="{ fontSize: '18px' }"/>
          快捷入口
        </div>
      </template>
      <template #extra><a href="#" @click="editFlag = !editFlag">{{ editFlag ? '完成' : '编辑' }}</a></template>
      <div class="quick-entry-list">
        <a-row>
          <a-col v-for="(item,index) in quickEntry" :key="index" span="4">
            <div class="quick-entry" @click="turnToPage(item.path)">
              <component :is='$antIcons[item.icon]' :style="{ fontSize:'30px'}"/>
              <span class="entry-title">{{ item.title }}</span>
              <close-circle-outlined v-if="editFlag" class="delete-icon" @click="deleteQuickEntry(index)"/>
            </div>
          </a-col>
          <a-col span="4" v-if="editFlag && quickEntry.length < maxCount">
            <div class="add-quick-entry" @click="addHomeQuickEntry" >
              <div class="add-icon">
                <plus-outlined :style="{ fontSize:'30px'}"/>
              </div>
            </div>
          </a-col>
        </a-row>
      </div>
    </a-card>
    <HomeQuickEntryModal  ref="homeQuickEntryModal" @addQuickEntry="addQuickEntry"/>
  </div>
</template>
<script setup>
import {onMounted, ref} from "vue";
import {router} from "/@/router";
import HomeQuickEntryModal from './home-quick-entry-modal.vue'
import localKey from '/@/constants/local-storage-key-const';
import { localRead, localSave } from '/@/utils/local-util';
import _ from "lodash";
import InitQuickEntryList from './init-quick-entry-list';

//---------------- 初始化展示 --------------------
onMounted(()=>{
  initQuickEntry();
})
let quickEntry = ref([])
function initQuickEntry(){
  let quickEntryJson = localRead(localKey.HOME_QUICK_ENTRY);
  if(!quickEntryJson){
    quickEntry.value = _.cloneDeep(InitQuickEntryList);
    return;
  }
  let quickEntryList = JSON.parse(quickEntryJson);
  if(_.isEmpty(quickEntryList)){
    quickEntry.value = _.cloneDeep(InitQuickEntryList);
    return;
  }
  quickEntry.value = quickEntryList;
}

// 页面跳转
function turnToPage(path) {
  if(editFlag.value){
    return;
  }
  router.push({path});
}
//----------------  编辑快捷入口 --------------------
let editFlag = ref(false);
let maxCount = ref(12);
// 快捷入口删除
function deleteQuickEntry(index){
  quickEntry.value.splice(index,1)
  localSave(localKey.HOME_QUICK_ENTRY,JSON.stringify(quickEntry.value));
}
// 添加快捷入口
let homeQuickEntryModal = ref();
function addHomeQuickEntry(){
  homeQuickEntryModal.value.showModal();
}
function addQuickEntry(row){
  quickEntry.value.push(row);
  localSave(localKey.HOME_QUICK_ENTRY,JSON.stringify(quickEntry.value));
}
</script>
<style lang='less' scoped>
.card-container {
  background-color: #fff;
  height: 100%;
  .title {
    &::before {
      content: '';
      position: absolute;
      top: 3px;
      left: 0;
      width: 3px;
      height: 30px;
      background-color: @primary-color;
    }
  }

  .quick-entry-list {
    height: 100%;
    .quick-entry {
      padding: 10px 0;
      display: flex;
      flex-direction: column;
      align-items: center;
      justify-content: center;
      cursor: pointer;
      border-radius: 4px;
      position: relative;

      .entry-title {
        margin-top: 5px;
      }

      &:hover {
        background-color: #F0FFFF;
      }

      .delete-icon {
        position: absolute;
        color: #F08080;
        top: 5px;
        right: 25px;
      }
    }

    .add-quick-entry {
      display: flex;
      align-items: center;
      justify-content: center;
      .add-icon {
        width: 70px;
        height: 70px;
        background-color: #fafafa;
        border: 1px dashed #d9d9d9;
        border-radius: 2px;
        cursor: pointer;
        transition: border-color .3s;
        display: flex;
        align-items: center;
        justify-content: center;
        color: #A9A9A9;

        &:hover {
          border-color: @primary-color;
          color: @primary-color;
        }
      }
    }
  }
}
</style>
