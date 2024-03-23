<template>
  <div>
    <el-menu
      :default-active="activeIndex"
      class="header"
      mode="horizontal"
      :ellipsis="false"
      bg-color="white"
      text-color="grey"
      active-color="black"
      router
    >
      <el-menu-item index="1" route="/"
        ><img src="@/assets/images/logo.png" alt="" style="width: 150px"
      /></el-menu-item>
      <el-menu-item index="2" route="/actor">人物传记</el-menu-item>
      <el-menu-item index="3" route="/event">历史事件</el-menu-item>
      <el-menu-item index="4" route="/power">势力分布</el-menu-item>
      <el-menu-item index="5" route="/discuss">交流专区</el-menu-item>
      <div class="flex-grow" />
      <!-- <el-menu-item index=""><SearchVue /></el-menu-item> -->
      <SearchVue/>
      <!-- <SearchVue/> -->
      <div class="flex-grow" />
      <UserInfoVue />
    </el-menu>
  </div>
</template>

<script setup>
import Title from "./header/Title.vue";
import SearchVue from "./header/Search.vue";
import UserInfoVue from "./header/UserInfo.vue";
import { ref, reactive, inject, onMounted } from "vue";

const activeIndex = inject("index");

// const handleSelect = (key, keyPath) => {

// }
</script>

<style scoped>
.flex-grow {
  flex-grow: 1;
}
.header {
  width: 100%;
  height: 8vh;
  box-sizing: border-box;
  position: fixed;
  top: 0;
  display: flex;
  /* background-color: rgba(126, 126, 126, 0.064); */
  z-index: 10;
}
</style>