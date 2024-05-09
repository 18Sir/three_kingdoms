<template>
  <div class="login-box">
    <el-row style="height: 100vh">
      <el-col :span="12">
        <img src="../../images/background.png" class="left-img" />
      </el-col>
      <el-col
        :span="12"
        style="
          background-color: #eee;
          display: flex;
          justify-content: center;
          align-items: center;
        "
      >
        <el-card shadow="always">
          <!-- 注册操作 -->
          <transition name="el-zoom-in-center">
            <div v-show="isRegister" class="transition-box">
              <el-form :rules="rules" :model="userData">
                <h1>注册</h1>
                <el-form-item prop="uname">
                  <el-input
                    v-model="userData.uname"
                    placeholder="请输入用户名"
                    clearable
                  >
                    <template #prepend>
                      <el-icon><User /></el-icon> 用户名:
                    </template>
                  </el-input>
                </el-form-item>

                <el-form-item prop="password">
                  <el-input
                    v-model="userData.password"
                    type="password"
                    placeholder="请输入密码"
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

                <el-form-item>
                  <el-row justify="space-between" style="height: auto">
                    <el-button type="primary" @click="register">注册</el-button>
                    <router-link
                      class="el-button"
                      to="/"
                      style="text-decoration: none"
                      >取消</router-link
                    >
                  </el-row>
                </el-form-item>

                <el-form-item>
                  <el-link
                    type="info"
                    :underline="false"
                    @click="isRegister = !isRegister"
                  >
                    <el-icon><ArrowLeft /></el-icon>返回登录
                  </el-link>
                </el-form-item>
              </el-form>
            </div>
          </transition>

          <!-- 登录操作 -->
          <transition name="el-fade-in">
            <div v-show="!isRegister" class="transition-box">
              <el-form :rules="rules" :model="userData">
                <h1>登录</h1>
                <el-form-item prop="uname">
                  <el-input
                    v-model="userData.uname"
                    placeholder="请输入用户名"
                    clearable
                  >
                    <template #prepend>
                      <el-icon><User /></el-icon> 用户名:
                    </template>
                  </el-input>
                </el-form-item>

                <el-form-item prop="password">
                  <el-input
                    v-model="userData.password"
                    type="password"
                    placeholder="请输入密码"
                    show-password
                  >
                    <template #prepend>
                      <el-icon><Lock /></el-icon> 密码:
                    </template>
                  </el-input>
                </el-form-item>

                <el-form-item>
                  <el-row justify="space-between" style="height: auto">
                    <el-checkbox
                      v-model="checked"
                      label="记住密码"
                      size="large"
                    />
                    <el-link type="primary" :underline="false">
                      忘记密码？
                    </el-link>
                  </el-row>
                </el-form-item>

                <el-form-item>
                  <el-row justify="space-between" style="height: auto">
                    <el-button type="primary" @click="login">登录</el-button>
                    <router-link
                      class="el-button"
                      to="/"
                      style="text-decoration: none"
                      >取消</router-link
                    >
                  </el-row>
                </el-form-item>

                <el-form-item style="width: 100%; text-align: right">
                  <el-row justify="end" style="height: auto">
                    <el-link
                      type="info"
                      :underline="false"
                      @click="isRegister = !isRegister"
                    >
                      没有账号？前往注册<el-icon><ArrowRight /></el-icon>
                    </el-link>
                  </el-row>
                </el-form-item>
              </el-form>
            </div>
          </transition>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref } from "vue";
import { ElLoading, ElMessage } from "element-plus";
import { decryption, encryption } from "@/util/crypto.js";

const isRegister = ref(false);
const userData = ref({
  uname: "",
  password: "",
  rePassword: "",
});
//如果记住了密码就读取用户名与密码
const localData = {
  uname: localStorage.getItem("uname"),
  password: localStorage.getItem("password"),
};
if (localData.password != null) {
  userData.value.uname = localData.uname;
  userData.value.password = decryption(localData.password);
}

const checked = ref(true);
//校验密码
const checkPassword = (rule, value, callback) => {
  if (value === "") {
    callback(new Error("请再次确认密码"));
  } else if (value !== userData.value.password) {
    callback(new Error("两次输入的密码不一致"));
  } else {
    callback;
  }
};
//表单校验规则
const rules = {
  uname: [
    { required: true, message: "请输入用户名", trigger: "blur" },
    { min: 2, max: 16, message: "用户名长度应为2-16位", trigger: "change" },
  ],
  password: [
    { required: true, message: "请输入密码", trigger: "blur" },
    { min: 6, max: 16, message: "密码长度应为6-16位", trigger: "change" },
  ],
  rePassword: [{ validator: checkPassword, trigger: "blur" }],
};
import { userRegisterService, userLoginService } from "@/api/user.js";
import stores from "@/stores";
import router from "@/router";

const options = {
  lock: true,
  fullscreen: true,
  text: "登录成功，正在进入主页...",
  background: "rgba(240, 240, 240, 0.7)",
};

//延时加载
const load = () => {
  //添加延时加载
  const loader = ElLoading.service(options);
  setTimeout(() => {
    loader.close();

    // 加载后进入主页
    router.push({
      path: "/",
    });
  }, 2000);
};

// //用户登录
const login = async () => {
  if (userData.value.uname === "" || userData.value.password === "") {
    ElMessage.error("用户名或密码不能为空");
    return;
  }
  let result = await userLoginService(userData.value);
  if (result.code.includes("OK")) {
    stores.commit("setToken", result.data);
    load();
  } else {
    ElMessage.error(result.msg ? result.msg : "登录失败");
  }
  //是否记住密码
  if (checked.value) {
    localStorage.setItem("uname", userData.value.uname);
    localStorage.setItem("password", encryption(userData.value.password));
  } else {
    localStorage.removeItem("uname");
    localStorage.removeItem("password");
  }
};

//用户注册
const register = async () => {
  if (userData.value.uname === "" || userData.value.password === "") {
    ElMessage.error("用户名或密码不能为空");
    return;
  }
  let result = await userRegisterService(userData.value);
  if (result.code.includes("OK")) {
    ElMessage.success(result.msg ? result.msg : "注册成功");
    isRegister.value = false;
  } else {
    ElMessage.error(result.msg ? result.msg : "注册失败");
  }
};
</script>

<style scoped>

.login-box :deep(.el-row) {
  width: 100%;
}
.login-box :deep(.el-col) {
  height: 100vh;
}
.login-box :deep(.left-img) {
  width: 100%;
  height: 100%;
}
.login-box :deep(h1) {
  width: 100%;
  text-align: center;
}
.login-box :deep(.el-card) {
  width: 75%;
  height: 55%;
}
.login-box :deep(.el-form) {
  height: 100%;
}
.login-box :deep(.el-button) {
  width: 36%;
  height: 6vh;
}
.login-box :deep(.el-input) {
  height: 100%;
}
.login-box :deep(.el-form-item) {
  height: 6vh;
  margin-top: 5%;
}
</style>