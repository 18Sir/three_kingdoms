<template>
  <div class="card">
    <el-upload
      ref="uploadAvatar"
      class="avatar-uploader"
      action="#"
      list-type="picture-card"
      :before-upload="beforeAvatarUpload"
      :auto-upload="false"
      :http-request="upload"
      :on-change="fileChange"
      multiple
      :file-list="fileList"
    >
      <el-image v-if="imageUrl" :src="imageUrl" class="avatar" fit="cover" />
      <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
      <template #file="{ file }">
        <el-tooltip content="新头像" placement="top">
          <img :src="file.url" alt="新图片" />
        </el-tooltip>
        <!-- <img :src="file.url" alt="新图片" /> -->
      </template>
    </el-upload>
    <el-button class="ml-3 submit-button" type="success" @click="submitUpload">
      确认上传
    </el-button>
  </div>
</template>

<script setup>
import { onMounted, ref } from "vue";
import { ElMessage } from "element-plus";
import { Plus } from "@element-plus/icons-vue";
import { getUsersServices, uploadAvatarService } from "@/api/user";
import stores from "@/stores";

const imageUrl = ref("");
const uploadAvatar = ref();
const avatarFile = ref();
const fileList = ref([]);

onMounted(() => {
  getUsersServices().then((res) => {
    imageUrl.value = res.data.avatar;
  });
});

const beforeAvatarUpload = (file) => {
  //允许的图片类型
  const imgType = file.type === "image/jpeg" || file.type === "image/png";
  //文件大小3M
  const imgLimit = file.size / 1024 / 1024 < 3;
  console.log(file);
  if (!imgType) {
    ElMessage.error("头像只能是jpg或png格式");
    return false;
  } else if (!imgLimit) {
    ElMessage.error("头像大小不能超过3MB！");
    return false;
  }
  return true;
};
const fileChange = (file, list) => {
  // console.log(file,list);
  avatarFile.value = file;
  // this.file = file.raw;
  if (list.length > 0) {
    fileList.value = [list[list.length - 1]];
  } else {
    fileList.value = list;
  }
  console.log(fileList.value);
};

const upload = () => {
  uploadAvatar.value.submit();
  uploadAvatar.value.clearFiles();
};

const submitUpload = () => {
  var forms = new FormData();
  console.log(avatarFile.value.raw);
  forms.append("file", avatarFile.value.raw);
  console.log(forms);
  uploadAvatarService(forms).then((res) => {
    if (res.code.includes("OK")) {
      ElMessage.success(res.msg);
      imageUrl.value = res.data;
      fileList.value = [];
    } else {
      ElMessage.error(res.msg);
    }
  });
};
</script>

<style scoped>
.avatar-uploader .avatar {
  width: 10rem;
  height: 10rem;
  display: block;
}
</style>

<style>
.card {
  padding: 2rem;
  width: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
}

.avatar-uploader .el-upload {
  border: 1px dashed var(--el-border-color);
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  transition: var(--el-transition-duration-fast);
}

.avatar-uploader .el-upload:hover {
  border-color: var(--el-color-primary);
}

.el-icon.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 10rem;
  height: 10rem;
  text-align: center;
}

.submit-button {
  margin-top: 1rem;
}
</style>