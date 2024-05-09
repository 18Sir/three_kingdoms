<template>
  <el-menu
    :default-active="activeIndex"
    class="el-menu-demo"
    mode="horizontal"
    :ellipsis="false"
    @select="handleSelect"
    background-color="#545c64"
    text-color="#fff"
  >
    <el-menu-item index="0">
      <el-text style="color: orange; font-size: larger"
        >三国历史可视化后台管理系统</el-text
      >
    </el-menu-item>
    <div class="flex-grow" />
    <el-sub-menu index="1"
      ><template #title>
        <el-row
          style="
            display: flex;
            align-items: center;
            justify-content: space-around;
            width: 10vw;
          "
        >
          <el-text class="mx-1" size="large" type="primary">{{
            userData.uname
          }}</el-text>
          <el-avatar :size="40" :src="userData.avatar" />
        </el-row>
      </template>
      <el-menu-item index="1-1" route="/" @click="goToUser"
        >返回用户界面</el-menu-item
      >
      <el-menu-item index="1-2" route="/login" @click="exitLogin"
        >退出登录</el-menu-item
      >
    </el-sub-menu>
  </el-menu>
</template>

<script setup>
import { getUsersServices } from "@/api/user";
import router from "@/router";
import stores from "@/stores";
import { onMounted, ref } from "vue";

const activeIndex = ref('0');
const handleSelect = () => {};

const userData = ref({});

const getUserData = () => {
  getUsersServices()
    .then((res) => {
      // console.log(res)
      if (res.code.includes("OK")) {
        userData.value = res.data;
        userData.value.avatar = res.data.avatar
          ? res.data.avatar
          : "https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png";
          stores.commit("setUserData",res.data);
      } else {
        userData.value = null;
      }
    })
    .catch(() => {
      userData.value = null;
    });
};

onMounted(() => {
  getUserData();
});

//返回用户界面
const goToUser = ()=>{
  router.push("/")
}

//退出登录逻辑
const exitLogin = () => {
  stores.commit("removeToken");
  userData.value = null;
};
</script>

<style scoped>
.flex-grow {
  flex-grow: 1;
}
</style>