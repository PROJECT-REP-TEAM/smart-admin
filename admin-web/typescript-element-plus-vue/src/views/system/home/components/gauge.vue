<template>
  <div class="gauge-main" id="main"></div>
</template>
<script setup lang="ts">
  import * as echarts from 'echarts/core';
  import { GaugeChart } from 'echarts/charts';
  import { CanvasRenderer } from 'echarts/renderers';
  import { onMounted, watch } from '@vue/runtime-core';
  import { reactive } from 'vue';
  // ----------------------- 以下是字段定义 emits props ---------------------
  const props = defineProps<{
    percent: number;
  }>();
  let option = reactive({});
  // ----------------------- 以下是计算属性 watch监听 ------------------------
  watch(
    () => props.percent,
    () => {
      init();
    }
  );
  // ----------------------- 以下是生命周期 ---------------------------------
  echarts.use([GaugeChart, CanvasRenderer]);
  onMounted(() => {
    init();
  });
  // ----------------------- 以下是方法 ------------------------------------
  function init() {
    option = {
      series: [
        {
          type: 'gauge',
          startAngle: 90,
          endAngle: -270,
          pointer: {
            show: false,
          },
          progress: {
            show: true,
            overlap: false,
            roundCap: true,
            clip: false,
            itemStyle: {
              borderWidth: 1,
              borderColor: '#464646',
            },
          },
          axisLine: {
            lineStyle: {
              width: 20,
            },
          },
          splitLine: {
            show: false,
            distance: 0,
            length: 10,
          },
          axisTick: {
            show: false,
          },
          axisLabel: {
            show: false,
            distance: 50,
          },
          data: [
            {
              value: props.percent,
              name: '完成度',
              title: {
                offsetCenter: ['0%', '-10%'],
              },
              detail: {
                offsetCenter: ['0%', '20%'],
              },
            },
          ],
          title: {
            fontSize: 18,
          },
          detail: {
            fontSize: 16,
            color: 'auto',
            formatter: '{value}%',
          },
        },
      ],
    };
    var chartDom = document.getElementById('main');
    if (chartDom) {
      let myChart = echarts.init(chartDom);
      option && myChart.setOption(option);
    }
  }
  // ----------------------- 以下是暴露的方法内容 ----------------------------
  defineExpose({});
</script>
<style scoped lang="scss">
  .gauge-main {
    width: 360px;
    height: 360px;
    padding: 28px;
    background: #fff;
  }
</style>
