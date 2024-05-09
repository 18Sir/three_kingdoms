<template>
    <el-row class="uploadComment">
      <el-col :span="2">
        <el-avatar :size="30" :src="user.avatar"></el-avatar>
      </el-col>
      <el-col :span="18">
        <el-input
          v-model="commentData"
          style="width: 100%; height: 100%"
          :placeholder="'回复@' + replyUser.uname + ':'"
        />
      </el-col>
      <el-col :span="2" style="margin-left: 1rem">
        <el-button type="primary" style="height: 100%" @click="submitComment"
          >发布</el-button
        >
      </el-col>
      
    </el-row>
  </template>
  
  <script setup>
  import { ref, onMounted, computed } from "vue";
  import { getUsersServices } from "@/api/user";
  import { replyCommentService } from "@/api/comment";
  import router from "@/router";
  import { ElMessage } from "element-plus";
  import stores from "@/stores";
  const user = ref({});
  const commentData = ref("");
  const props = defineProps({
    replyCid:Number,
    replyUser:Object,
    replyHeight:Number,
  })

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
  });
  
  // 提交评论
  const submitComment = () => {
    let data = {}
    data.mid = router.currentRoute.value.params.id
    data.content = commentData.value
    data.beCid = props.replyCid
    data.replyUid = props.replyUser.uid
    data.height = props.replyHeight
    replyCommentService(data).then((res)=>{
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
    height: 40px;
    align-items: center;
    margin: 7px 0px;
  }
  .uploadComment .el-col {
    height: 90%;
    display: flex;
    align-items: center;
  }
  </style>