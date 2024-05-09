<template>
  <el-steps
    style="max-width: 100%"
    :active="activeStep"
    finish-status="success"
    simple
  >
    <el-step title="验证身份" />
    <el-step title="修改密码" />
    <el-step title="操作成功" />
  </el-steps>

  <div class="form-box">
    <el-form :rules="rules" :model="userData" ref="emailRef">
      <div v-if="activeStep == 0">
        <el-form-item prop="email">
          <el-input
            v-model="email"
            type="email"
            placeholder="绑定的邮箱"
            disabled
          >
            <template #prepend>
              <el-icon><Cellphone /></el-icon>&nbsp;绑定的邮箱:
            </template>
          </el-input>
        </el-form-item>

        <el-form-item prop="code" class="code-row">
          <el-col :span="16" style="height: 100%">
            <el-input
              v-model="userData.code"
              type="text"
              placeholder="请输入验证码"
            >
            </el-input>
          </el-col>
          <el-col :span="6" style="height: 100%">
            <el-button style="width: 100%; height: 100%" @click="getCode()"
              >获取验证码</el-button
            >
          </el-col>
        </el-form-item>
      </div>

      <div v-else-if="activeStep == 1">
        <el-form-item prop="password">
          <el-input
            v-model="userData.password"
            type="password"
            placeholder="请输入新密码"
            show-password
          >
            <template #prepend>
              <el-icon><Lock /></el-icon> 密码:
            </template>
          </el-input>
        </el-form-item>

        <el-form-item prop="rePassword">
          <el-input
            v-model="userData.rePassword"
            type="password"
            placeholder="请再次输入密码"
            show-password
          >
            <template #prepend>
              <el-icon><Lock /></el-icon> 确认密码:
            </template>
          </el-input>
        </el-form-item>
      </div>

      <div v-else style="height: 5rem; text-align: center">
        <el-text size="large">
          您已成功绑定密码，点击下方按钮可退出此对话框
        </el-text>
      </div>

      <el-form-item class="form-footer-item">
        <el-button
          class="step-button"
          type="primary"
          @click="goSteps(activeStep, emailRef)"
        >
          {{ activeStep < 3 ? "下一步" : "完成" }}
        </el-button>
      </el-form-item>
    </el-form>
  </div>
</template>
  
  <script setup>
import { computed, onMounted, ref, watch } from "vue";
import {
  verifyEmailCodeService,
  sendEmailCodeService,
  updatePasswordService,
} from "@/api/user";
import { ElMessage } from "element-plus";
import { getUserData } from "@/util/getter.js";
import stores from "@/stores";

const activeStep = ref(0);
const emailRef = ref();

const user = computed(() => {
  return stores.getters.getUserData;
});
const email = computed(() => {
  return user.value.email;
});
const userData = ref({});
watch(email, (nv, ov) => {
  userData.value.email = nv;
});
const isBind = computed(() => {
  return (
    user.value.email == null ||
    user.value.email == undefined ||
    user.value.email.length < 2
  );
});

onMounted(() => {
  stores.dispatch("setUserData");
});

//校验密码
const checkPassword = (rule, value, callback) => {
  if (value === "") {
    callback(new Error("请再次确认密码"));
  } else if (value !== userData.value.password) {
    callback(new Error("两次输入的密码不一致"));
  } else {
    callback();
  }
};
//表单校验规则
const rules = {
  password: [
    { required: true, message: "请输入密码", trigger: "blur" },
    { min: 6, max: 16, message: "密码长度应为6-16位", trigger: "change" },
  ],
  rePassword: [{ validator: checkPassword, trigger: "blur" }],
};

const emit = defineEmits(["hideDialog", "dialog"]);

const goSteps = (step, formEl) => {
  if (!formEl) return;
  switch (step) {
    case 0:
      verifyEmailCode();
      break;
    case 1:
      verifyPassword();
      break;
    case 2:
      emit("hideDialog");
      break;
  }
};
// 修改密码
const verifyPassword = () => {
  emailRef.value.validate((valid) => {
    if (!valid) {
      ElMessage.error("两次密码输入不一致");
    } else {
      let params = {
        password: userData.value.password,
      };
      updatePasswordService(params).then((res) => {
        console.log(res);
        if (res.code.includes("OK")) {
          activeStep.value = 2;
          ElMessage.success(res.msg);
        } else {
          ElMessage.error(res.msg);
        }
      });
    }
  });
};
//获取验证码
const getCode = () => {
  // let bool = userData.value.email ? true : false

  let params = {
    email: email.value,
  };
  sendEmailCodeService(params).then((res) => {
    if (res.code.includes("OK")) {
      ElMessage.success(res.msg);
    } else {
      ElMessage.error(res.msg);
    }
  });
};
//验证邮箱验证码
const verifyEmailCode = () => {
  if (userData.value.code == "" || userData.value.code == null) {
    ElMessage.error("验证码不能为空！");
    return;
  }
  verifyEmailCodeService(userData.value.code).then((res) => {
    if (res.code.includes("OK")) {
      activeStep.value = 1;
      ElMessage.success(res.msg);
    } else {
      ElMessage.error(res.msg);
    }
  });
};
</script>
  
  <style>
.form-box {
  width: 100%;
  padding: 2rem 5rem;
}
.form-box .el-form-item {
  height: 3rem;
}
.form-box .el-form-item .el-input {
  height: 100%;
}

.form-footer-item .el-form-item__content {
  display: flex;
  justify-content: center;
  height: 100%;
}
.code-row .el-form-item__content {
  justify-content: space-between;
}
.step-button {
  width: 50%;
  height: 100%;
}
</style>