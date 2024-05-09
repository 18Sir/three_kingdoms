<template>
  <el-container>
    <el-header> <HeaderVue /> </el-header>

    <el-main>
      <router-view></router-view>
    </el-main>
  </el-container>
</template>
  
  <script setup>
import HeaderVue from "@/views/header/Header.vue";
import { onMounted, provide, ref } from "vue";

provide("index", "/actor");
const loading = ref(true);

onMounted(() => {
  setTimeout(() => {
    loading.value = false;
  }, 1000);
});
</script>