<template>
  <div class="card">
    <el-form :model="userData" label-width="auto" class="form-box">
      <el-form-item label="用户名">
        <el-input v-model="userData.uname" />
      </el-form-item>
      <el-form-item label="性别">
        <el-radio-group v-model="userData.sex">
          <el-radio-button label="男" value="男" />
          <el-radio-button label="女" value="女" />
          <el-radio-button label="保密" value="保密" />
        </el-radio-group>
      </el-form-item>
      <el-form-item label="出生日期">
        <el-date-picker
          v-model="userData.birth"
          type="date"
          placeholder="选择一个出生日期"
          style="width: 100%"
        />
      </el-form-item>
      <el-form-item class="form-footer">
        <el-button type="primary" @click="onSubmit">保存</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script setup>
import { onMounted, ref } from "vue";
import { getUsersServices, updateUserServices } from "@/api/user";
import { ElMessage } from "element-plus";

onMounted(() => {
  getUserData();
});

const userData = ref({});
const onSubmit = () => {
  updateUserServices(userData.value).then((res) => {
    ElMessage.success(res.msg);
    getUserData()
  });
};

const getUserData = () => {
  getUsersServices().then((res) => {
    userData.value = res.data;
  });
};
</script>

<style>
.card{
    padding: 2rem;
}
.form-box {
  width: 100%;
}
.form-footer .el-form-item__content{
    justify-content: center;
}
</style>