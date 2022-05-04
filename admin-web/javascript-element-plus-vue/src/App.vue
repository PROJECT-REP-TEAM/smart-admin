

<template>
  <RouterView v-loading.fullscreen.lock="spinning" />
</template>
<script setup>
import { computed } from "vue";
import { useSpinStore } from "/@/store/modules/spin";

let spinStore = useSpinStore();
const spinning = computed(() => spinStore.loading);

</script>
<style>
</style>
