<template>
  <div class="user-msg-layout">
    <div class="msg-big-box">
      <el-card
        class="msg-box"
        shadow="hover"
        :body-style="{ padding: '10px' }"
        v-for="(item, index) in msgList"
        :key="index"
      >
        <el-row>
          <!-- 右侧帖子内容 -->
          <el-col :span="21">
            <el-row>
              <el-col :span="22">
                <el-link
                  class="title"
                  :href="'http://localhost:5173/#/discuss/info/' + item.mid"
                  target="_blank"
                  >{{ item.title }}</el-link
                >
                <el-button
                style="margin-left: 1rem;"
                  type="primary"
                  :icon="Edit"
                  circle
                  size="small"
                  @click="editData(item)"
              /></el-col>
              <el-col :span="2"
                ><el-tag
                  :type="getStatusTag(item.status).type"
                  size="small"
                  effect="dark"
                  >{{ getStatusTag(item.status).label }}</el-tag
                ></el-col
              >
            </el-row>
            <el-text line-clamp="2" size="small">
              <br />
              <div v-html="item.content"></div>
            </el-text>
            <!-- 右侧下方图标 -->
            <el-row>
              <el-col :span="4" class="tabBox">
                <el-text class="tabIcon"
                  ><el-icon class="msg-icon"><View /></el-icon>&nbsp;{{
                    item.visits
                  }}</el-text
                >
              </el-col>
              <el-col :span="4" class="tabBox">
                <el-text class="tabIcon"
                  ><Icon icon="uiw:like-o" class="msg-icon" />&nbsp;{{
                    item.likes
                  }}</el-text
                >
              </el-col>
              <el-col :span="4" class="tabBox">
                <el-text class="tabIcon"
                  ><el-icon class="msg-icon"><Share /></el-icon>&nbsp;{{
                    item.share
                  }}</el-text
                >
              </el-col>
              <el-col :span="4">
                <el-text>{{ item.time }}</el-text>
              </el-col>
            </el-row>
          </el-col>
        </el-row>
      </el-card>
    </div>
    <!-- 修改帖子 -->
    <Dialog
      v-model:visible="dialog"
      :style="{ width: '800px' }"
      header="修改帖子信息(帖子修改提交后会重新进入审核中)"
      :modal="true"
      class="p-fluid user-msg-update"
    >
      <div class="field">
        <label for="title">标题</label>
        <InputText
          id="title"
          v-model.trim="data.title"
          required="true"
          autofocus
          :class="{ 'p-invalid': submitted && !data.title }"
        />
        <small class="p-error" v-if="submitted && !data.title"
          >标题名需要填写</small
        >
      </div>

      <div class="field">
        <label for="content">帖子内容</label>
        <Editor
          id="content"
          aria-required="true"
          v-model="data.content"
          :class="{ 'p-invalid': submitted && !data.content }"
          editorStyle="height: 320px"
        />
        <small class="p-error" v-if="submitted && !data.content"
          >帖子内容需要填写</small
        >
      </div>
      <div class="field">
        <label for="type">类型:</label>
        <el-tag style="margin-left: 1rem">{{ typeName }}</el-tag>
      </div>

      <template #footer>
        <Button label="取消" icon="pi pi-times" text @click="hideDialog" />
        <Button label="提交修改" icon="pi pi-check" text @click="saveData" />
      </template>
    </Dialog>
  </div>
</template>

<script setup>
import { onMounted, ref } from "vue";
import { getMsgByTokenService, updateMsgService } from "@/api/msg";
import { getAllMsgTypeService } from "@/api/msgType";
import "@/assets/dataTable.css";
import { Icon } from "@iconify/vue";
import Editor from "primevue/editor";
import { Edit } from "@element-plus/icons-vue";

const typeList = ref();
const typeName = ref("其他");
const dialog = ref(false);
const submitted = ref(false);
const data = ref({
  title: null,
  content: null,
  type: 0,
});
const statusOptions = [
  { label: "待审核", type: "info", status: 0 },
  { label: "正常", type: "success", status: 1 },
  { label: "被打回", type: "danger", status: -1 },
];
const statusLabels = ["待审核", "正常", "被打回"];
// 返回对应状态的标签
const getStatusTag = (status) => {
  return statusOptions.filter((tag) => {
    return tag.status == status;
  })[0];
};

const msgList = ref();
const getMsgDatas = () => {
  getMsgByTokenService().then((res) => {
    msgList.value = res.data;
  });
};

//获取所有类型
const getAllMsgType = () => {
  getAllMsgTypeService().then((res) => {
    typeList.value = res.data;
  });
};

//编辑数据
const editData = (prod) => {
  data.value = { ...prod };
  submitted.value = false;
  dialog.value = true;
  typeName.value = typeList.value.filter((v) => {
    return v.tid == data.value.type;
  })[0].tname;
};

//隐藏对话框
const hideDialog = () => {
  dialog.value = false;
  submitted.value = false;
  data.value = {};
};

//提交新增数据
const saveData = () => {
  submitted.value = true;
  if (data.value.title.trim()) {
    updateMsgService(data.value).then((res) => {
      ElMessage.success(res.msg);
      getMsgDatas();
    });
  }
  dialog.value = false;
  data.value = {};
};

onMounted(() => {
  getMsgDatas();
  getAllMsgType();
});
</script>

<style scoped>
.user-msg-layout :deep(.title) {
  font-size: large;
  font-weight: bold;
}
.user-msg-layout :deep(.msg-box) {
  margin: 1rem 0;
  box-shadow: 2px 2px 5px 1px #ddd;
  background-color: #fff;
}

.user-msg-layout :deep(.tabIcon) {
  display: flex;
  align-items: center;
}

.user-msg-update {
  position: fixed;
  top: 20%;
  left: 19%;
  margin: 1rem 0;
}

.user-msg-update :deep(.p-button) {
  padding: 0.5rem 1rem;
}
.user-msg-update :deep(.p-button .p-button-icon-left) {
  margin-right: 0.5rem;
}
</style>