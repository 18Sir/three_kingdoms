<template>
  <div class="comment-info-box">
    <el-row
      style="height: auto; align-items: center; box-sizing: border-box"
      v-for="(item, index) in commentList"
      :key="index"
    >
      <el-col :span="2" style="align-self: flex-start; margin-top: 15px">
        <el-avatar :size="50" :src="item.avatar"></el-avatar>
      </el-col>
      <el-col :span="20" class="right-comment">
        <el-row>
          <el-text style="font-weight: bold">{{ item.uname }}</el-text>
        </el-row>
        <el-row>
          <el-text>{{ item.content }}</el-text>
        </el-row>
        <el-row class="bottom-icon-box">
          <el-col :span="4">
            <el-text type="info">{{ item.createTime }}</el-text>
          </el-col>
          <el-col :span="3">
            <el-text size="large" type="info"
              ><Icon icon="iconamoon:like-light" class="icon" />
              <span style="margin-left: 3px" v-show="item.likes > 0">{{
                item.likes
              }}</span>
            </el-text>
          </el-col>
          <el-col :span="3">
            <el-text size="large" type="info"
              ><Icon icon="iconamoon:dislike-light" class="icon"
            /></el-text>
          </el-col>
          <el-col :span="3">
            <el-button
              type="primary"
              :text="true"
              size="small"
              style="padding: 2px"
              >回复</el-button
            >
          </el-col>
        </el-row>
        <el-divider style="margin-top: 10px" />
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { Icon } from "@iconify/vue";
import { ref, inject, onMounted, computed } from "vue";
import { getMsgsCommentService } from "@/api/comment.js";
import router from "@/router";
import stores from "@/stores";

const msgData = inject("msgData");
const commentList = computed(()=>{
  return stores.getters.getComment
})

//获取评论
const getComment = () => {
  const mid = router.currentRoute.value.params.id;
  // getMsgsCommentService(mid).then((res) => {
  //   commentList.value = res.data;
  //   console.log(res);
  // });
  stores.dispatch('getCommentAction',mid)
  // commentList.value = stores.getters.getComment
};

onMounted(() => {
  getComment();
});
</script>

<style scoped>
.comment-info-box {
  width: 100%;
}
.right-comment > .el-row {
  margin-top: 5px;
}
.bottom-icon-box {
  align-items: center;
}
.bottom-icon-box > .el-col {
  display: flex;
  align-items: center;
}
.bottom-icon-box > .el-col > .el-text {
  display: flex;
  align-items: center;
}
.icon{
    cursor: pointer;
    font-size: larger;
}
</style>