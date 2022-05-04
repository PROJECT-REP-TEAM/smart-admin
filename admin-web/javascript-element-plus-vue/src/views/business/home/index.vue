<template>
  aaa1212
</template>
<script setup>
import { onMounted, ref } from 'vue';
import { useRouter } from 'vue-router';

const router = useRouter();

const currentYear = ref(null);
let date = ref();
let weekString = ref();

onMounted(() => {
  // 更新屏幕宽度
  getDate();
});

function getDate () {
  let array = new Array('日', '一', '二', '三', '四', '五', '六');

  let time = new Date();
  let year = time.getFullYear();
  let month = time.getMonth() + 1;
  let week = time.getDay();
  let day = time.getDate();

  date.value = '  ' + year + '-' + month + '-' + day;
  weekString.value = ' 星期' + array[week];
  currentYear.value = `${year}`;
}

// ----------------------- 以下是暴露的方法内容 ----------------------------
defineExpose({});
</script>

<style lang="scss" scoped>
.top {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.user-name {
  font-size: 20px;
  font-weight: bold;
}

.heard {
  background-color: #efefef;
  width: 80px;
  height: 80px;
  border-radius: 50%;
}

.col {
  margin-left: 30px;
  margin-right: auto;
  display: flex;
  flex-direction: column;
}

.item {
  margin: 20px 40px;
  display: flex;
  align-items: center;
  padding: 20px;
  border: 1px solid black;
}

.card {
  margin-top: 20px;
}

.icon {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  background-color: #52c41a;
}

.list {
  margin: 20px 0;
  padding: 0 20px 20px;
  border: 1px solid #EDEDED;
}

.list-top {
  margin-top: 10px;
  padding-bottom: 10px;
  width: 100%;
  display: flex;
  justify-content: space-between;
  align-items: center;

  .list-tab-name {
    font-weight: 700;
    font-size: 15px;
  }

  .unread-num {
    margin-left: 10px;
    margin-right: auto;
    padding: 0 10px;
    height: 20px;
    line-height: 20px;
    border-radius: 10px;
    background: red;
    color: white;
    text-align: center;
  }
}

.item-name {
  flex-shrink: 1;
  flex-grow: 1;

}

.home-navigation {
  //margin-top: 40px;
}
</style>

