<template>
  <div class="left-menu">
    <el-menu
      active-text-color="#ffd04b"
      class="el-menu-vertical-demo"
      :default-active="activeIndex"
      background-color="#545c64"
      text-color="#fff"
      unique-opened
      router
      @open="handleOpen"
      @close="handleClose"
      @select="handleSelect"
    >
      <el-menu-item index="1" route="/admin/actor">
        <template #title>
          <span>人物管理</span>
        </template>
      </el-menu-item>
      <el-menu-item index="2" route="/admin/event">
        <template #title>
          <span>事件管理</span>
        </template>
      </el-menu-item>
      <el-menu-item index="3" route="/admin/addr">
        <template #title>
          <span>地点管理</span>
        </template>
      </el-menu-item>
      <el-menu-item index="4" route="/admin/power">
        <template #title>
          <span>势力管理</span>
        </template>
      </el-menu-item>
      <el-sub-menu index="5" route="/admin/msg">
        <template #title>
          <span>帖子管理</span>
        </template>
        <el-menu-item-group>
          <el-menu-item index="5-1" route="/admin/msg/list">帖子列表</el-menu-item>
          <el-menu-item index="5-2" route="/admin/msg/type">帖子类型</el-menu-item>
        </el-menu-item-group>
      </el-sub-menu>
      <el-menu-item index="6" route="/admin/user">
        <template #title>
          <span>用户管理</span>
        </template>
      </el-menu-item>
      <el-sub-menu index="7" route="/admin/comment">
        <template #title>
          <span>举报管理</span>
        </template>
        <el-menu-item-group>
          <el-menu-item index="7-1" route="/admin/jubao/msg">帖子列表</el-menu-item>
          <el-menu-item index="7-2" route="/admin/jubao/comment">评论列表</el-menu-item>
        </el-menu-item-group>
      </el-sub-menu>
    </el-menu>
  </div>
</template>

<script setup>
import router from "@/router";
import stores from "@/stores";
import { onMounted, ref } from "vue";

const handleOpen = () => {};
const handleClose = () => {};
const activeIndex = ref("1")
const selectList = ref(["人物", "事件", "地点", "势力", "帖子", "用户"]);
const handleSelect = (i,ip,item)=>{
    stores.commit("setAdminSearch",selectList.value[ip[0]-1])
}
onMounted(()=>{
  const path = router.currentRoute.value.path
  if(path.includes('actor')){
    activeIndex.value = '1'
  }else if(path.includes('event')){
    activeIndex.value = '2'
  }else if(path.includes('addr')){
    activeIndex.value = '3'
  }else if(path.includes('power')){
    activeIndex.value = '4'
  }else if(path.includes('msg/list')){
    activeIndex.value = '5-1'
  }else if(path.includes('msg/type')){
    activeIndex.value = '5-2'
  }else if(path.includes('user')){
    activeIndex.value = '6'
  }
})
</script>

<style scoped>
.left-menu {
  max-height: 100vh;
  display: flex;
  justify-content: center;
}
</style>