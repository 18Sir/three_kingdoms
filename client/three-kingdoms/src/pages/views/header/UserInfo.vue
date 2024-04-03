<template>
  <el-sub-menu index="7" v-if="userData !== null">
    <template #title>
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
    <el-menu-item index="" @click="goUser">个人中心</el-menu-item>
    <el-menu-item index="7-2" route="/login" @click="exitLogin"
      >退出登录</el-menu-item
    >
    <el-menu-item v-if="userData.admin > 0" index="7-3" route="/admin" @click="goToAdmin">
      前往管理员页面
    </el-menu-item>
  </el-sub-menu>

  <el-menu-item v-else index="8" route="/login">
    尊敬的游客，请先登录
  </el-menu-item>
</template>

<script setup>
import { getUsersServices } from "@/api/user";
import router from "@/router";
import stores from "@/stores";
import { onMounted, onUpdated, reactive, ref } from "vue";

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

//前往个人主页
const goUser = () => {
  const routeUrl = router.resolve({
    path: "/user",
  });
  window.open(routeUrl.href, "_blank");
};

//退出登录逻辑
const exitLogin = () => {
  stores.commit("removeToken");
  userData.value = null;
};
//进入管理员页面
const goToAdmin = ()=>{

}
</script>

<style scoped>
.title-box {
  list-style-type: none;
  width: 20%;
  height: 8vh;
  line-height: 8vh;
  font-size: 1.5vw;
  display: flex;
  align-content: center;
  justify-content: space-around;
  /*background-color: burlywood;*/
}
.title-item {
  width: 20%;
  text-align: center;
  float: left;
  /*background-color: antiquewhite;*/
}
</style>