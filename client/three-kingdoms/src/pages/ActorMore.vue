<template>
  <el-container>
    <el-main style="padding: 0;">
      <PageVue />
    </el-main>
  </el-container>
</template>

<script setup>
import { onMounted, provide, reactive, ref, toRef, watch } from "vue";
import PageVue from "./views/Page.vue";
import router from "@/router";

const name = ref();

name.value = router.currentRoute.value.params.name
  ? router.currentRoute.value.params.name
  : "all";
provide("name", name);

</script>

<style>
</style>