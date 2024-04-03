<template>
  <div class="card">
    <el-row v-for="(item, index) in options" :key="index">
      <el-col :span="8"
        ><div class="grid-content ep-bg-purple">
          <el-text>
            <el-icon v-if="item.success" color="#5dbe8a"
              ><SuccessFilled
            /></el-icon>
            <el-icon v-else color="#e8b004"><WarningFilled /></el-icon>
            <span style="margin-left: 0.5rem">{{ item.label }}</span>
          </el-text>
        </div></el-col
      >
      <el-col :span="8"
        ><div class="grid-content ep-bg-purple">
          <el-text :type="item.status">{{ item.info }}</el-text>
        </div></el-col
      >
      <el-col :span="8"
        ><div class="grid-content ep-bg-purple">
          <el-button type="primary" text @click="showDialog(item.type)">
            {{ item.text }}
          </el-button>
        </div></el-col
      >
    </el-row>
  </div>

  <el-dialog v-model="dialog" :title="options[showType].text" width="800">
    <UpdatePassword v-if="showType == 0" @hideDialog="hideDialog" />
    <UpdateEmail v-else @hideDialog="hideDialog" />
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="dialog = false">取消</el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref, computed, watch } from "vue";
import UpdatePassword from "./UpdatePassword.vue";
import UpdateEmail from "./UpdateEmail.vue";
import { getUserData } from "@/util/getter.js";
import { ElMessage } from "element-plus";
import router from "@/router";

const dialog = ref(false);
const showType = ref(0);

const user = computed(() => {
  return getUserData();
});

const options = ref([
  {
    label: "设置密码",
    success: true,
    info: "已设置密码",
    text: "修改密码",
    status: "",
    type: 0,
  },
  {
    label: "绑定邮箱",
    success: false,
    info: "尚未绑定邮箱",
    text: "绑定邮箱",
    status: "danger",
    type: 1,
  },
]);

const emailSuccessOption = {
  label: "绑定邮箱",
  success: true,
  info: "已绑定邮箱",
  text: "换绑邮箱",
  status: "primary",
  type: 1,
};

const emailWarnOption = {
  label: "绑定邮箱",
  success: false,
  info: "尚未绑定邮箱",
  text: "绑定邮箱",
  status: "danger",
  type: 1,
};

// options.value[1] = computed(()=>{
//   return !user.value.email ? emailSuccessOption : emailWarnOption
// })
watch(
  () => user.value.email,
  (nv, ov) => {
    if (nv != null && nv.length > 4) {
      options.value[1] = emailSuccessOption;
    } else {
      options.value[1] = emailWarnOption;
    }
  }
);

const showDialog = (type) => {
  if(type == 0 && !user.value.email){
    ElMessage.error('修改密码需绑定邮箱')
    return;
  }
  showType.value = type;
  dialog.value = true;
};

const hideDialog = () => {
  dialog.value = false;
  ElMessage.success('由于修改了密码，需要您重新登录')
  router.push({
    path:'/login'
  })
};
</script>

<style scoped>
.card {
  padding: 1rem;
}
.card .el-row {
  width: 100%;
  height: 3rem;
  align-items: center;
}
</style>