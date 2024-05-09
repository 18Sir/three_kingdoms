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
        <comment-info
          :comment="item"
          :index="index"
          :pIndex="index"
          @showReplyComment="showReplyComment"
          @showJuBao="showJuBao"
        />
        <!-- 回复的评论列表 -->
        <div v-if="item.commentList">
          <el-row
            style="height: auto; align-items: center; box-sizing: border-box"
            v-for="(item2, index2) in item.commentList"
            :key="index2"
          >
            <el-col :span="2" style="align-self: flex-start; margin-top: 15px">
              <el-avatar :size="40" :src="item2.avatar"></el-avatar>
            </el-col>
            <el-col :span="20" class="right-comment">
              <comment-info
                :comment="item2"
                :index="index2"
                :pIndex="index"
                @showReplyComment="showReplyComment"
                @showJuBao="showJuBao"
              />
            </el-col>
          </el-row>
        </div>
        <ReplyComment
          id="reply-box"
          v-if="item.isReply"
          :replyCid="replyCid"
          :replyUser="replyUser"
          :replyHeight="replyHeight"
        />
        <el-divider style="margin-top: 10px" />
      </el-col>
    </el-row>
    <!-- 举报 -->
    <el-dialog
      v-model="jubaoDialog"
      title="请选择举报理由"
      width="500"
      align-center
      draggable
    >
      <el-form ref="juBaoComment" :model="juBaoData" :rules="rules" status-icon>
        <el-form-item prop="reason">
          <el-radio-group v-model="juBaoData.reason">
            <el-row class="jubao-title"
              ><el-text type="info">违反法律法规</el-text></el-row
            >
            <el-row>
              <el-radio value="违法违禁">违法违禁</el-radio>
              <el-radio value="色情">色情</el-radio>
              <el-radio value="低俗">低俗</el-radio>
              <el-radio value="赌博诈骗">赌博诈骗</el-radio>
            </el-row>
            <el-row class="jubao-title"
              ><el-text type="info">谣言及不实信息</el-text></el-row
            >
            <el-row>
              <el-radio value="涉政谣言">涉政谣言</el-radio>
              <el-radio value="虚假不实信息">虚假不实信息</el-radio>
              <el-radio value="涉社会事件谣言">涉社会事件谣言</el-radio>
            </el-row>
            <el-row class="jubao-title"
              ><el-text type="info">侵犯个人权益</el-text></el-row
            >
            <el-row>
              <el-radio value="人身攻击">人身攻击</el-radio>
              <el-radio value="侵犯隐私">侵犯隐私</el-radio>
            </el-row>
            <el-row class="jubao-title"
              ><el-text type="info">有害社区环境</el-text></el-row
            >
            <el-row>
              <el-radio value="垃圾广告">垃圾广告</el-radio>
              <el-radio value="引战">引战</el-radio>
              <el-radio value="刷屏">刷屏</el-radio>
              <el-radio value="帖子不相关">帖子不相关</el-radio>
              <el-radio value="青少年不良信息">青少年不良信息</el-radio>
            </el-row>
            <el-row style="width: 100%">
              <el-radio value="其他">其他</el-radio>
              <el-input
                v-if="juBaoData.reason == '其他'"
                v-model="juBaoData.detail"
                maxlength="50"
                style="width: 100%"
                placeholder="请填写举报理由"
                show-word-limit
                type="textarea"
              />
            </el-row>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="jubaoDialog = false">取消</el-button>
          <el-button type="primary" @click="submitJuBao(juBaoComment)">
            确认
          </el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>
  
  <script setup>
import { Icon } from "@iconify/vue";
import { ref, onMounted, computed, watch } from "vue";
import router from "@/router";
import stores from "@/stores";
import { savejuBaoCommentService } from "@/api/juBao";
import { ElMessage } from "element-plus";
import ReplyComment from "./ReplyComment.vue";
import CommentInfo from "./CommentInfo.vue";

const jubaoDialog = ref(false);
const juBaoData = ref({});
const commentData = ref(null);
const replyCid = ref();
//回复的用户数据
const replyUser = ref();
//回复的评论的层级
const replyHeight = ref();

// 举报评论的ref绑定
const juBaoComment = ref();
const rules = {
  reason: [
    { required: true, message: "请选择举报理由", trigger: ["blur", "change"] },
  ],
};
const commentList = computed(() => {
  return stores.getters.getComment;
});
const userData = computed(() => {
  return stores.getters.getUserData;
});

let currentReplyCid = 0;

//回复评论
const showReplyComment = (t, i, pi) => {
  console.log(stores.getters.getComment);
  //先全部隐藏
  for (let c of commentList.value) {
    c.isReply = false;
  }
  //然后单独显示
  commentList.value[pi].isReply = currentReplyCid != t.cid;

  //判断评论的层级（判断是回复帖子的和回复评论的）
  if (t.height > 0) {
    replyCid.value = commentList.value[pi].cid;
  } else {
    replyCid.value = t.cid;
  }
  replyUser.value = t;
  replyHeight.value = t.height + 1;
  currentReplyCid = t.cid;
};

//显示举报
const showJuBao = (t) => {
  jubaoDialog.value = true;
  commentData.value = t;
};

//提交举报
const submitJuBao = (juBaoCommentRef) => {
  if (!juBaoCommentRef) return;
  juBaoCommentRef.validate((valid, fields) => {
    if (valid) {
      let data = {
        beUid: commentData.value.uid,
        mid: commentData.value.mid,
        cid: commentData.value.cid,
        reason:
          juBaoData.value.reason == "其他"
            ? juBaoData.value.detail
            : juBaoData.value.reason,
      };
      savejuBaoCommentService(data).then((res) => {
        ElMessage.success(res.msg);
      });
    } else {
      ElMessage.error("请完善信息");
    }
  });
  jubaoDialog.value = false;
};

onMounted(() => {
  
});
</script>
  
  <style scoped>
.comment-info-box {
  width: 100%;
}
.jubao-title {
  flex-basis: 100%;
}
</style>