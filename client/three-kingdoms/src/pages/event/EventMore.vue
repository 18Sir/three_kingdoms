<template>
  <el-container>
    <el-main style="padding: 0">
      <PageVue />
    </el-main>
  </el-container>
</template>

<script setup>
import { provide, ref } from "vue";
import PageVue from "../../views/page/Page.vue";
import router from "@/router";

const name = ref();

name.value = router.currentRoute.value.params.name
  ? router.currentRoute.value.params.name
  : "all";
provide("name", name);
</script>
