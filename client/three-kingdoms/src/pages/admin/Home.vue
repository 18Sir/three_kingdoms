<template>
  <div class="admin-box">
    <el-container>
      <el-header style="padding: 0px"><Header /></el-header>
      <el-container>
        <el-aside width="200px" style="background-color: #545c64;height: 90vh;"
          ><LeftMenu
        /></el-aside>
        <el-main style="padding: 1rem;"> <router-view></router-view> </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script setup>
import Header from "./Header.vue";
import LeftMenu from "./LeftMenu.vue";
import ActorTable from "./ActorTable.vue";
</script>

<style scoped>
.admin-box {
  width: 100%;
  height: 100vh;
  max-height: 100vh;
  /* background-color: #545c64; */
}
</style>