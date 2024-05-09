<template>
  <el-card class="comment-box" body-class="comment-body">
    <el-row style="width: 100%">
      <el-text>{{ msgData.commentNum }} &nbsp;&nbsp;评论</el-text>
    </el-row>
    <el-row style="margin-top: 10px">
      <el-menu
        :default-active="activeIndex"
        mode="horizontal"
        @select="select"
        :ellipsis="false"
      >
        <el-menu-item index="1" @click="getComment('hot')">按热度排序</el-menu-item>
        <el-menu-item index="2" @click="getComment('time')">按时间排序</el-menu-item>
      </el-menu>
    </el-row>
    <!-- 发表评论 -->
    <div style="height: 20px;width: 100%;"></div>
    <EditComment />
    <el-divider />
    <!-- 评论内容 -->
    <CommentList :key="msgData.mid" />
    <!-- 没有更多评论 -->
    <el-row class="nomore-comment">
      <el-text type="info" style="align-self: flex-start">没有更多评论</el-text>
    </el-row>
  </el-card>
</template>

<script setup>
import { ref, inject, onMounted} from "vue";
import CommentList from "@/pages/comment/CommentList.vue";
import EditComment from "@/pages/comment/EditComment.vue";
import router from "@/router";
import stores from "@/stores";

const msgData = inject("msgData");
const activeIndex = ref("1");
const sort = ref('hot');
//获取评论
const getComment = (sort) => {
  const mid = router.currentRoute.value.params.id;
  stores.commit("setCommentSort",sort);
  stores.dispatch("getCommentAction", mid);
};

const select = () => {};

onMounted(()=>{
  getComment('hot');
  stores.dispatch("setUserData");
})
</script>

<style>
.comment-box {
  width: 100%;
  display: flex;
  flex-direction: column;
  justify-content: start;
  align-items: center;
  box-sizing: border-box;
  margin: 0%;
}
.comment-body {
  width: 90%;
}
.uploadComment {
  width: 100%;
  height: 60px;
  align-items: center;
  margin: 10px 0px;
}
.uploadComment .el-col {
  height: 90%;
  display: flex;
  align-items: center;
}
.nomore-comment {
  width: 100%;
  height: 100px;
  display: flex;
  margin-top: 50px;
  justify-content: center;
}
</style>