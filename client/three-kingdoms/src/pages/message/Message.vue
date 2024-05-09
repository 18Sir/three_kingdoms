<template>
  <div class="message-layout">
    <el-container>
      <el-header> <HeaderVue></HeaderVue> </el-header>
      <div style="padding: 0 15%">
        <el-container style="background-color: rgba(219, 220, 188, 0.3)">
          <el-aside width="15%" class="left-title">
            <span class="left-item user-center">消息中心</span>
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
                <el-icon>
                  <span :class="item.icon" style="font-size: 0.5rem"></span>
                </el-icon>
                <span>{{ item.label }}</span>
              </el-menu-item>
            </el-menu>
          </el-aside>
          <el-main class="right-content">
            <el-container style="padding: 1rem;">
              <el-header class="content-right-header">
                <div class="system-msg-box">
                  <span>{{ headerName }}</span>
                </div>
              </el-header>
              <el-main class="content-right-main">
                <router-view></router-view>
              </el-main>
            </el-container>
          </el-main>
        </el-container>
      </div>
    </el-container>
  </div>
</template>

<script setup>
import router from "@/router";
import HeaderVue from "@/views/header/Header.vue";
import { ref, onMounted } from "vue";

const items = ref([
  {
    label: "系统通知",
    icon: "pi pi-circle-fill",
    route: "/message/system",
    class: "default-active",
  },
]);
const defaultActive = ref("0");
const headerName = ref(items.value[0].label);

onMounted(() => {
  setDefaultMenu();
});

// 初始化默认的菜单
const setDefaultMenu = () => {
  const path = router.currentRoute.value.path;
  if (path.includes("system")) {
    defaultActive.value = "0";
    headerName.value = items.value[0].label;
  }
};
</script>

<style scoped>
.message-layout {
  width: 100%;
  height: 100vh;
  background-image: url("https://img.zcool.cn/community/0129de6095300611013e3b7dd8934b.png@1280w_1l_2o_100sh.png");
}
.message-layout :deep(.el-header) {
  width: 100%;
  height: 8vh;
}
.reight-content{
  padding: 0.5rem;
}

.right-content .content-right-header {
  padding: 0rem;
  border-bottom: 1px solid #ccc;
  display: flex;
  justify-content: start;
  margin-bottom: 1rem;
  align-items: center;
  color: #343839;
}
.right-content .system-msg-box{
  width: 100%;
  margin-bottom: 0.5rem;
  padding: 1rem 0.8rem;
  border-radius: 0.3rem;
  background-color: #fff;
  font-size: 1rem;
  box-shadow: 2px 2px 8px #aaa;
}

.right-content .content-right-main {
  width: 100%;
  height: 75vh;
  box-sizing: border-box;
  overflow: auto;
  padding: 0;
}
/* 滚动条样式 */
::-webkit-scrollbar{
  width: 1rem;
}
/* 滚动条轨道 */
::-webkit-scrollbar-track {
  background-color: rgba(219, 220, 188, 0.1);
}

/* 滚动条滑块 */
::-webkit-scrollbar-thumb {
  background-color: rgba(134, 134, 115, 0.7);
}

.message-layout :deep(.left-title) {
  height: 92vh;
  background-color: rgba(219, 220, 188, 0.5);
}
.left-title .left-menu {
  background-color: rgba(0, 0, 0, 0);
}
.left-title .user-center {
  border-bottom: 1px solid #ccc;
}
.left-title .left-item {
  width: 100%;
  height: 4rem;
  display: flex;
  justify-content: center;
  align-content: center;
  flex-wrap: wrap;
  font-weight: bolder;
  font-size: 16px;
  background-color: rgba(0, 0, 0, 0);
}
</style>