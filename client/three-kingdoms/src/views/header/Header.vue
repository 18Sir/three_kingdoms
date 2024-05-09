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
      <el-menu-item index="/" route="/"
        ><img src="@/assets/images/logo.png" alt="" style="width: 150px"
      /></el-menu-item>
      <el-menu-item index="/actor" route="/actor">人物传记</el-menu-item>
      <el-menu-item index="/event" route="/event">历史事件</el-menu-item>
      <el-menu-item index="/power" route="/power">势力分布</el-menu-item>
      <el-menu-item index="/discuss" route="/discuss">交流专区</el-menu-item>
      <div class="flex-grow" />
      <SearchVue />
      <div class="flex-grow" />
      <UserInfoVue />
    </el-menu>
  </div>
  <PageHeader />
</template>

<script setup>
import SearchVue from "@/views/header/Search.vue";
import UserInfoVue from "@/views/header/UserInfo.vue";
import { getUsersServices } from "@/api/user";
import { inject } from "vue";
import PageHeader from "../page/PageHeader.vue";
import stores from "@/stores";
import { onMounted, ref } from "vue";

const activeIndex = inject("index");

const getUserData = () => {
  getUsersServices().then((res) => {
    // console.log(res)
    if (res.code.includes("OK")) {
      res.data.avatar = res.data.avatar
        ? res.data.avatar
        : "https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png";
      stores.commit("setUserData", res.data);
    }
  });
};

onMounted(() => {
  getUserData();
});
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
  z-index: 10;
}
</style>