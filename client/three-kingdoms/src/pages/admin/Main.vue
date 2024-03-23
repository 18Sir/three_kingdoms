<template>
  <div class="main-box">
    <el-container>
      <!-- <el-header>
        <div class="main-header">
          <SearchVue />
          <el-button type="primary">增加{{ stores.getters.getAdminSearch }}</el-button>
        </div>
      </el-header> -->
      <el-main class="data-box">
        <DataTableVue/>
      </el-main>
    </el-container>
  </div>
</template>

<script setup>
import stores from "@/stores";
import { watch } from "vue";
import DataTableVue from './ActorTable.vue'
import SearchVue from "./Search.vue";

</script>

<style scoped>
.main-box {
  width: 100%;
  /* height: 85vh; */
}
.main-header{
    display: flex;
    justify-content: space-evenly;
}
/* .data-box{
    max-height: 86vh;
    overflow: auto;
} */
</style>