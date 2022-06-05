<!--
 * @Author: LiHaiFan
 * @Date: 2022-06-05 15:26:04
 * @LastEditTime: 2022-06-05 21:59:54
 * @LastEditors: LiHaiFan
 * @Description: 
 * @FilePath: \typescript-element-plus-vue\src\views\business\setting\major-setting.vue
-->
<template>
  <a-card size="small" :bordered="false" :hoverable="true">
    <category-tree-table
      :category-type="CATEGORY_TYPE_ENUM.MAJOR.value"
    />
  </a-card>
</template>
<script setup lang="ts">
import CategoryTreeTable from "./components/category-tree-table.vue";
import { CATEGORY_TYPE_ENUM } from "/@/constants/business/category";
</script>
