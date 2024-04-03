<template>
  <el-container>
    <el-main
      style="
        width: 100%;
        height: auto;
        display: flex;
        flex-direction: column;
        justify-content: start;
        align-items: center;
      "
    >
      <!-- 帖子信息 -->
      <div class="msg-box">
        <el-card
          body-class="msg-content"
          style="width: 900px; min-width: 900px"
          class="msg"
        >
          <template #header>
            <div class="card-header">
              <!-- 标题 -->
              <el-row>
                <el-col :span="24">
                  <span class="msg-title">{{ msgData.title }}</span></el-col
                >
              </el-row>
              <!-- 浏览等显示内容 -->
              <el-row style="margin: 1rem 0px" class="msg-info">
                <el-text type="info">{{ msgData.time }}</el-text>
                <el-text type="info">{{ msgData.visits }}浏览</el-text>
                <el-text type="info">{{ msgData.share }}分享</el-text>
                <el-text type="info">{{ msgData.commentNum }}评论</el-text>
              </el-row>
              <!-- 帖主信息展示 -->
              <el-row>
                <el-col :span="2">
                  <el-avatar size="large" :src="userData.avatar"></el-avatar>
                </el-col>
                <el-col
                  :span="20"
                  style="
                    margin-left: 0.3rem;
                    display: flex;
                    flex-direction: column;
                    justify-content: space-evenly;
                  "
                >
                  <el-row
                    ><el-col
                      ><el-link :underline="false" style="font-size: large">{{
                        userData.uname
                      }}</el-link></el-col
                    >
                  </el-row>
                  <el-row
                    ><el-text type="info"
                      >文章：{{ userData.msgNum }}</el-text
                    ></el-row
                  >
                </el-col>
              </el-row>
            </div>
          </template>
          <div v-html="msgData.content"></div>
          <template #footer>
            <div class="footer-box">
              <div class="share-box">
                <Share></Share>
              </div>
              <div>
                <el-button type="danger" plain>举报</el-button>
              </div>
            </div>
          </template>
        </el-card>
        <el-card
          class="right-icon-box"
          body-style="display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;"
        >
          <Icon icon="iconamoon:like-light" class="icon" />
          <el-text size="large">{{ msgData.likes }}</el-text>
          <Icon icon="f7:ellipses-bubble" class="icon" />
          <el-text size="large">{{ msgData.commentNum }}</el-text>
        </el-card>
      </div>

      <!-- 评论信息 -->
      <div class="comment-vue">
        <Comment />
      </div>
    </el-main>
  </el-container>
</template>

<script setup>
import { ArrowRight } from "@element-plus/icons-vue";
import { getMsgByMidService } from "@/api/msg";
import router from "@/router";
import { onMounted, provide, ref } from "vue";
import Share from "./views/Share.vue";
import Comment from "./views/Comment.vue";
import { Icon } from "@iconify/vue";
import Image from 'primevue/image';

//帖子数据
const msgData = ref({});
const userData = ref({});
//将文章数据传递给分享组件
provide("msgData", msgData);
provide("userData", userData);

//获取id
const mid = router.currentRoute.value.params.id;
//获取帖子数据
const getMsgData = () => {
  getMsgByMidService(mid).then((res) => {
    msgData.value = res.data.msg;
    userData.value = res.data.user;
  });
};

onMounted(() => {
  getMsgData();
});
</script>

<style scoped>
.msg-box {
  width: 100%;
  height: auto;
  display: flex;
  justify-content: center;
  align-items: center;
  position: relative;
}
.msg {
  padding: 0 5%;
  box-sizing: border-box;
}
.msg-title {
  font-weight: bolder;
  font-size: x-large;
}
.msg-info .el-text {
  margin: 0px 0.5rem;
}
.footer-box {
  display: flex;
  justify-content: space-between;
}
.comment-vue {
  width: 900px;
  margin-top: 10px;
}
.right-icon-box {
  width: 60px;
  height: 120px;
  position: fixed;
  top: 300px;
  right: 180px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  background-color: aliceblue;
}
.right-icon-body {
  display: flex;
}
.right-icon-box .el-text {
  align-self: center;
}
.icon {
  width: 30px;
  height: 30px;
  cursor: pointer;
}
</style>