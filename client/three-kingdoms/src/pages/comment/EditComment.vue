<template>
  <el-row class="uploadComment">
    <el-col :span="2">
      <el-avatar :size="50" :src="user.avatar"></el-avatar>
    </el-col>
    <el-col :span="18">
      <el-input
        v-model="commentData"
        style="width: 100%; height: 100%"
        :placeholder="placeHolder"
      />
    </el-col>
    <el-col :span="2" style="margin-left: 1rem">
      <el-button type="primary" style="height: 100%" @click="submitComment"
        >发表评论</el-button
      >
    </el-col>
    
  </el-row>
</template>

<script setup>
import { ref, onMounted } from "vue";
import { getUsersServices } from "@/api/user";
import { saveCommentService } from "@/api/comment";
import router from "@/router";
import { ElMessage } from "element-plus";
import stores from "@/stores";
const placeHolder = ref("");
const user = ref({});
const holderList = ref([
  "快来发布一条精彩评论吧~",
  "这里是评论区，不是无人区",
  "与其赞同别人的话语，不如自己畅所欲言",
  "你猜我在评论区等待谁？",
  "你渴望拥有力量吗，评论让力量更强大",
  "新的风暴已经出现，你的妙评何时再现",
  "万水千山总是情，评论两句行不行",
  "评论千万条，等你发一条",
  "轻轻敲醒沉睡的心灵，让我看看你的点评",
]);
const commentData = ref("");
const changeHolder = () => {
  let num = Math.floor(Math.random() * holderList.value.length);
  placeHolder.value = holderList.value[num];
};
const getUserData = () => {
  getUsersServices()
    .then((res) => {
      // console.log(res)
      if (res.code.includes("OK")) {
        user.value = res.data;
        user.value.avatar = res.data.avatar
          ? res.data.avatar
          : "https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png";
      } else {
        user.value = null;
      }
    })
    .catch(() => {
      user.value = null;
    });
};
onMounted(() => {
  getUserData();
  changeHolder();
});

// 提交评论
const submitComment = () => {
  let data = {}
  data.mid = router.currentRoute.value.params.id
  data.content = commentData.value
  saveCommentService(data).then((res)=>{
    if(res.code.includes('OK')){
      commentData.value = null;
      ElMessage.success('评论发布成功！')
      stores.dispatch('getCommentAction',data.mid)
    }
    else{
      ElMessage.error(res.msg)
    }
  }).catch(()=>{
    ElMessage.error('评论发布失败，请稍后重试')
  })
};
</script>

<style scoped>
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
</style>