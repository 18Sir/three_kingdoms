<template>
  <div class="comment-info-box">
    <el-row>
      <el-text style="font-weight: bold">{{ comment.uname }}</el-text>
    </el-row>
    <el-row>
      <el-text
        >{{
          comment.replyUid && comment.height > 1
            ? "回复@" + comment.replyUser.uname + ":"
            : ""
        }}
        {{ comment.content }}</el-text
      >
    </el-row>
    <el-row class="bottom-icon-box">
      <el-col :span="4" style="min-width: 7rem">
        <el-text type="info">{{ comment.createTime }}</el-text>
      </el-col>
      <el-col :span="3" style="min-width: 4rem">
        <el-text size="large" type="info"
          ><!-- 取消点赞图标 -->
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
          <span style="margin-left: 3px" v-show="comment.likes > 0">{{
            comment.likes
          }}</span>
        </el-text>
      </el-col>
      <el-col :span="3" style="min-width: 4rem">
        <el-text size="large" type="info"
          ><Icon
            icon="iconamoon:dislike-light"
            v-if="!isUnLike"
            @click="updateUnLike"
            class="icon" />
          <Icon
            icon="iconamoon:dislike-duotone"
            v-else
            @click="updateUnLike"
            class="icon like-icon"
        /></el-text>
      </el-col>
      <el-col :span="3" style="min-width: 4rem">
        <el-button
          type="primary"
          :text="true"
          size="small"
          style="padding: 2px"
          @click="replyComment()"
          >回复</el-button
        >
      </el-col>
      <el-col :span="3" style="min-width: 4rem">
        <el-dropdown
          @command="commentCommand"
          style="outline: none"
          :hide-on-click="false"
        >
          <el-icon class="el-icon--right">
            <More />
          </el-icon>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item command="remove" v-if="showDel">
                <el-popconfirm
                  width="220"
                  confirm-button-text="是的"
                  cancel-button-text="不"
                  icon-color="#626AEF"
                  title="删除评论后，其下所有评论都会被删除，是否继续？"
                  @confirm="confirmDelete"
                >
                  <template #reference>
                    <span>删除</span>
                  </template>
                </el-popconfirm>
              </el-dropdown-item>
              <el-dropdown-item
                command="jubao"
                v-if="comment.uid != userData.uid"
                >举报</el-dropdown-item
              >
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { Icon } from "@iconify/vue";
import { ref, onMounted, computed, watch } from "vue";
import { ElMessage } from "element-plus";
import router from "@/router";
import stores from "@/stores";
import {
  delCommentService,
  updateCommentLikesService,
  updateCommentUnLikesService,
} from "@/api/comment";
const props = defineProps({
  comment: Object,
  index: Number,
  pIndex: Number,
});

//接收父组件方法
const emit = defineEmits(["showReplyComment", "showJuBao"]);
//是否点赞
const isLike = ref(props.comment.isLiked);
//是否点踩
const isUnLike = ref(props.comment.isUnLiked);
const showDel = ref(false);

const userData = computed(() => {
  return stores.getters.getUserData;
});
const msgData = computed(() => {
  return stores.getters.getMsgData;
});
const changeShowDel = () => {
  let bool =
    userData.value.admin > 0 ||
    userData.value.uid == msgData.value.uid ||
    userData.value.uid == props.comment.uid;
  showDel.value = bool;
};

watch(
  () => userData,
  (nv, ov) => {
    changeShowDel();
  }
);

watch(
  () => props.comment,
  (nv, ov) => {
    isLike.value = props.comment.isLiked;
    isUnLike.value = props.comment.isUnLiked;
  }
);

onMounted(() => {
  changeShowDel();
});

//回复评论
const replyComment = () => {
  emit("showReplyComment", props.comment, props.index, props.pIndex);
};

//评论更多设置
const commentCommand = (command) => {
  if (command == "jubao") {
    emit("showJuBao", props.comment);
  }
};

//删除评论
const confirmDelete = () => {
  delCommentService(props.comment.cid).then((res) => {
    if (res.code.endsWith("OK")) {
      const mid = router.currentRoute.value.params.id;
      stores.dispatch("getCommentAction", mid);
      ElMessage.success(res.msg);
    } else {
      ElMessage.error(res.msg);
    }
  });
};

//点赞逻辑
const updateLike = () => {
  updateCommentLikesService(props.comment.cid, !isLike.value).then((res) => {
    isLike.value = !isLike.value;
    isUnLike.value = false;
    msgData.value.likes = res.data;
    stores.dispatch("getCommentAction", router.currentRoute.value.params.id);
    ElMessage.info(res.msg);
  });
};

//点踩逻辑
const updateUnLike = () => {
  updateCommentUnLikesService(props.comment.cid, !isUnLike.value).then(
    (res) => {
      isUnLike.value = !isUnLike.value;
      isLike.value = false;
      msgData.value.likes = res.data;
      stores.dispatch("getCommentAction", router.currentRoute.value.params.id);
      ElMessage.info(res.msg);
    }
  );
};
</script>

<style scoped>
.comment-info-box {
  width: 100%;
}
.comment-info-box > .el-row {
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
.icon {
  cursor: pointer;
  font-size: larger;
}
:deep(.el-tooltip__trigger:focus-visible) {
  cursor: pointer;
  outline: unset;
}
.jubao-title {
  flex-basis: 100%;
}
.like-icon {
  color: rgb(244, 107, 221);
}
</style>