<template>
  <el-container>
    <el-header style="height: 10vh; background-color: #00a0d8">
      <el-image src="../../images/logo.png" fit="fill" style="height: 100%" />
    </el-header>
    <el-main class="user-main">
      <el-container class="user-main-content">
        <el-aside class="content-left">
          <span class="left-item user-center">个人中心</span>
          <el-menu
            :default-active="defaultActive"
            class="left-menu"
            @select="selectMenu"
            router
          >
            <el-menu-item
              v-for="(item, index) in items"
              :key="index"
              :index="index + ''"
              class="left-item"
              :route="item.route"
            >
              <el-icon> <span :class="item.icon"></span> </el-icon>
              <span>{{ item.label }}</span>
            </el-menu-item>
          </el-menu>
        </el-aside>
        <el-container>
          <el-header class="content-right-header">
            <div class="header-start"></div>
            <span>{{ headerName }}</span>
          </el-header>
          <el-main class="content-right-main">
            <router-view></router-view>
          </el-main>
        </el-container>
      </el-container>
    </el-main>
  </el-container>
</template>

<script setup>
import router from "@/router";
import { onMounted, ref } from "vue";

const items = ref([
  {
    label: "我的信息",
    icon: "pi pi-user-edit",
    route: "/user/setting",
    class: "default-active",
  },
  {
    label: "我的头像",
    icon: "pi pi-user",
    route: "/user/face",
  },
  {
    label: "账号安全",
    icon: "pi pi-lock",
    route: "/user/secure",
  },
]);
const defaultActive = ref('0')
const headerName = ref(items.value[0].label);

onMounted(() => {
  setDefaultMenu()
});

// 初始化默认的菜单
const setDefaultMenu = ()=>{
  const path = router.currentRoute.value.path;
  if(path.includes('setting')){
    defaultActive.value = '0';
    headerName.value = items.value[0].label;
  }else if(path.includes('face')){
    defaultActive.value = '1';
    headerName.value = items.value[1].label;
  }else if(path.includes('secure')){
    defaultActive.value = '2';
    headerName.value = items.value[2].label;
  }
}

const selectMenu = (i, ip) => {
  headerName.value = items.value[i].label;
};
</script>

<style scoped>
.user-main {
  width: 100%;
  height: 90vh;
  display: flex;
  justify-content: center;
}
.user-main-content {
  width: 60%;
  height: 38rem;
  flex: 0.6;
  margin-top: 3rem;
  border: 1px solid #ccc;
  box-shadow: 1px 1px 3px 1px #ccc;
}
.user-main-content .content-left {
  width: 10rem;
  overflow: hidden;
  border-right: 1px solid #ccc;
  background-color: #efefef;
}
.user-main-content .content-right-header {
  height: 4rem;
  border-bottom: 1px solid #ccc;
  display: flex;
  justify-content: start;
  align-items: center;
  color: #00a0d8;
}
.user-main-content .content-right-header .header-start {
  width: 0.1rem;
  height: 1rem;
  border: 0.1rem solid #00a0d8;
  border-radius: 0.2rem;
  margin-right: 0.5rem;
}

.user-main-content .content-right-header span {
  font-size: 1rem;
}
.user-main-content .content-right-main {
  background-color: #fff;
}
.content-left .user-center {
  border-bottom: 1px solid #ccc;
}
.content-left :deep(.left-item) {
  width: 100%;
  height: 4rem;
  display: flex;
  justify-content: center;
  align-content: center;
  flex-wrap: wrap;
}

.content-left .left-menu {
  background-color: rgba(0, 0, 0, 0);
}
/* 被选中项 */
.el-menu-item.is-active {
  color: #fff;
  background-color: #00a0d8;
}
</style>