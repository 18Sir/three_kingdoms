<template>
  <div class="discuss-box">
    <el-container>
      <el-main class="discuss-main">
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
                  <Share @updateShares="handleUpdateShares" :mid="mid"></Share>
                </div>
                <div>
                  <el-button type="danger" plain @click="jubaoDialog = true"
                    >举报</el-button
                  >
                </div>
              </div>
            </template>
          </el-card>
          <el-card
            class="right-icon-box"
            style="z-index: 99"
            body-style="display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;"
          >
            <!-- 取消点赞图标 -->
            <Icon
              v-if="!isLike"
              icon="iconamoon:like-light"
              class="icon"
              @click="updateLike"
            />
            <!-- 点赞图标 -->
            <Icon
              v-else
              icon="iconamoon:like-duotone"
              class="icon like-icon"
              @click="updateLike"
            />
            <el-text size="large">{{ msgData.likes }}</el-text>
            <div style="margin: 0.3rem 0 0.1rem 0;">
              <Icon
                icon="iconamoon:dislike-light"
                v-if="!isUnLike"
                @click="updateUnLike"
                class="icon"
              />
              <Icon
                icon="iconamoon:dislike-duotone"
                v-else
                @click="updateUnLike"
                class="icon like-icon"
              />
            </div>

            <Icon icon="f7:ellipses-bubble" class="icon" />
            <el-text size="large">{{ msgData.commentNum }}</el-text>
          </el-card>
        </div>

        <!-- 评论信息 -->
        <div class="comment-vue">
          <Comment :key="msgData.mid" />
        </div>
        <!-- 举报 -->
        <el-dialog
          v-model="jubaoDialog"
          title="请选择举报理由"
          width="500"
          align-center
          draggable
        >
          <el-form ref="juBaoMsg" :model="juBaoData" :rules="rules" status-icon>
            <el-form-item prop="reason">
              <el-radio-group v-model="juBaoData.reason">
                <el-row class="jubao-title"
                  ><el-text type="info">违反法律法规</el-text></el-row
                >
                <el-row>
                  <el-radio value="违法违禁">违法违禁</el-radio>
                  <el-radio value="赌博诈骗">赌博诈骗</el-radio>
                  <el-radio value="侵权申诉">侵权申诉</el-radio>
                </el-row>
                <el-row class="jubao-title"
                  ><el-text type="info">谣言及不实信息</el-text></el-row
                >
                <el-row>
                  <el-radio value="涉政谣言">涉政谣言</el-radio>
                  <el-radio value="虚假不实信息">虚假不实信息</el-radio>
                  <el-radio value="严重脱离史实">严重脱离史实</el-radio>
                </el-row>
                <el-row class="jubao-title"
                  ><el-text type="info">不友善行为</el-text></el-row
                >
                <el-row>
                  <el-radio value="人身攻击">人身攻击</el-radio>
                  <el-radio value="引战">引战</el-radio>
                </el-row>
                <el-row class="jubao-title"
                  ><el-text type="info">违反公序良俗</el-text></el-row
                >
                <el-row>
                  <el-radio value="色情低俗">色情低俗</el-radio>
                  <el-radio value="血腥暴力">血腥暴力</el-radio>
                  <el-radio value="青少年不良信息">青少年不良信息</el-radio>
                </el-row>
              </el-radio-group>
            </el-form-item>
            <el-form-item prop="detail">
              <el-row class="jubao-title"
                ><el-text type="info">详细描述(必填)</el-text></el-row
              >
              <el-input
                v-model="juBaoData.detail"
                maxlength="300"
                style="width: 90%"
                placeholder="填写具体违规内容出现位置，更容易举报成功哦"
                show-word-limit
                type="textarea"
              />
            </el-form-item>
          </el-form>
          <template #footer>
            <div class="dialog-footer">
              <el-button @click="jubaoDialog = false">取消</el-button>
              <el-button type="primary" @click="submitJuBao(juBaoMsg)">
                确认
              </el-button>
            </div>
          </template>
        </el-dialog>
      </el-main>
    </el-container>
  </div>
</template>

<script setup>
import {
  getMsgByMidWithViewsService,
  updateMsgLikesService,
  updateMsgUnLikesService,
} from "@/api/msg";
import { savejuBaomsgService } from "@/api/juBao.js";
import router from "@/router";
import { onMounted, provide, ref } from "vue";
import Share from "@/pages/discuss/Share.vue";
import Comment from "../comment/Comment.vue";
import { Icon } from "@iconify/vue";
import { ElMessage } from "element-plus";
import stores from "@/stores";

//帖子数据
const msgData = ref({});
const userData = ref({});
const isLike = ref(false);
const isUnLike = ref(false);
const jubaoDialog = ref(false);
const juBaoData = ref({});
// 举报帖子的ref绑定
const juBaoMsg = ref();
const rules = {
  reason: [
    { required: true, message: "请输入举报理由", trigger: ["blur", "change"] },
  ],
  detail: [
    { required: true, message: "请填写详细描述", trigger: ["blur", "change"] },
  ],
};

//将文章数据传递给分享组件
provide("msgData", msgData);
provide("userData", userData);

//获取id
const mid = router.currentRoute.value.params.id;

//获取帖子数据并增加访问量
const getMsgData = () => {
  getMsgByMidWithViewsService(mid).then((res) => {
    msgData.value = res.data.msg;
    userData.value = res.data.user;
    isLike.value = res.data.isLikes;
    isUnLike.value = res.data.isUnLikes;
    stores.commit("setMsgData", res.data.msg);
  });
};

//点赞逻辑
const updateLike = () => {
  updateMsgLikesService(mid, !isLike.value).then((res) => {
    isLike.value = !isLike.value;
    isUnLike.value = false;
    msgData.value.likes = res.data;
    ElMessage.info(res.msg);
  });
};

//点赞逻辑
const updateUnLike = () => {
  updateMsgUnLikesService(mid, !isUnLike.value).then((res) => {
    isUnLike.value = !isUnLike.value;
    isLike.value = false;
    msgData.value.likes = res.data;
    ElMessage.info(res.msg);
  });
};

//增加分享量
const handleUpdateShares = (shares) => {
  msgData.value.share = shares;
};

//提交举报
const submitJuBao = (juBaoMsgRef) => {
  if (!juBaoMsgRef) return;
  juBaoMsgRef.validate((valid, fields) => {
    if (valid) {
      let data = {
        beUid: userData.value.uid,
        mid: mid,
        reason: juBaoData.value.reason,
        detail: juBaoData.value.detail,
      };
      savejuBaomsgService(data).then((res) => {
        ElMessage.success(res.msg);
      });
    } else {
      ElMessage.error("请完善信息");
    }
  });
  jubaoDialog.value = false;
};

onMounted(() => {
  getMsgData();
});
</script>

<style scoped>
.discuss-main {
  width: 100%;
  height: auto;
  display: flex;
  flex-direction: column;
  justify-content: start;
  align-items: center;
}
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
  height: 160px;
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
.like-icon {
  color: rgb(244, 107, 221);
}
.jubao-title {
  flex-basis: 100%;
}
</style>