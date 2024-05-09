<template>
  <el-container class="discuss-box">
    <UploadMsg />
    <el-aside
      :width="leftWidth"
      style="
        min-width: 75px;
        max-width: 140px;
        display: flex;
        justify-content: center;
        flex-direction: column;
        background-color: #faf4ed;
      "
      class="leftBox"
    >
      <el-switch
        v-model="isCollapse"
        style="justify-content: center"
        size="large"
        :before-change="isChange"
        inline-prompt
        active-text="展开"
        inactive-text="收起"
        @change="changeLayout"
      />
      <el-menu
        default-active="-1"
        :collapse="!isCollapse"
        class="leftMenu"
        background-color="#f8f4ed"
        @select="changeMsgList"
      >
        <el-menu-item index="-1">
          <el-icon><i class="pi pi-tag"></i></el-icon>
          <template #title>全部</template>
        </el-menu-item>
        <el-menu-item
          v-for="item in msgTypeList"
          :key="item.tid + ''"
          :index="item.tid + ''"
        >
          <el-icon><i class="pi pi-tag"></i></el-icon>
          <template #title>{{ item.tname }}</template>
        </el-menu-item>
      </el-menu>
    </el-aside>
    <el-aside
      width="50%"
      style="
        height: 80%;
        margin: 5% 0px 0px 30px;
        background-color: #f8f4ed;
        align-self: flex-start;
      "
    >
      <el-row style="width: 100%">
        <el-menu
          :default-active="activeIndex"
          mode="horizontal"
          @select="select"
          :ellipsis="false"
          style="height: 2.5rem; background-color: #f0f5e5"
        >
          <el-menu-item style="color: #869d9d" index="1" @click="sortMsg('hot')"
            >按热度排序</el-menu-item
          >
          <el-menu-item
            style="color: #869d9d"
            index="2"
            @click="sortMsg('time')"
            >按时间排序</el-menu-item
          >
        </el-menu>
      </el-row>
      <el-card
        class="msg-box"
        shadow="hover"
        :body-style="{ padding: '10px' }"
        v-for="(item, index) in msgList"
        :key="index"
        v-infinite-scroll="loadMsg"
        :infinite-scroll-delay="1000"
      >
        <el-row>
          <!-- 左边头像 -->
          <el-col :span="2" style="min-width: 44px">
            <el-avatar :size="35" :src="item.userAvatar" /><br />
            <el-link class="userName" href="">{{ item.userName }}</el-link>
          </el-col>
          <!-- 右侧帖子内容 -->
          <el-col :span="21">
            <el-text line-clamp="2" size="small">
              <el-link
                class="title"
                :href="'http://localhost:5173/#/discuss/info/' + item.mid"
                target="_blank"
                >{{ item.title }}</el-link
              ><br />
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
      <div style="width: 100%;margin: 1rem 0;text-align: center;">
        <el-text
        v-if="showMsgNum >= totalNum"
        style="font-size: larger;"
        >已经到底啦! &gt;__&lt;
      </el-text>
      </div>
      
    </el-aside>
  </el-container>
</template>

<script setup>
import { onMounted, ref } from "vue";
import elementResizeListen from "element-resize-detector";
import { ElMessage } from "element-plus";
import {
  getMsgsCheckedService,
  getMsgsCheckedByTypeService,
} from "@/api/msg.js";
import { getAllMsgTypeService } from "@/api/msgType";
import { Icon } from "@iconify/vue";
import UploadMsg from "@/pages/discuss/UploadMsg.vue";
import router from "@/router";
import stores from "@/stores";

//左侧菜单容器宽度
const leftWidth = ref("10%");
//是否展开类型筛选菜单
const isCollapse = ref(true);
//是否允许用户自行展开类型筛选菜单
const isControl = ref(true);
//帖子列表数据
const msgList = ref({});
// 帖子类型数据
const msgTypeList = ref([]);
//帖子总数据
const msgAllList = ref({});
//默认显示的排序规则
const activeIndex = ref("1");
//默认显示的帖子数
const showMsgNum = ref(6);
//总帖子数
const totalNum = ref(0);

//获取全部已审核帖子
const getAllMsgs = () => {
  getAllMsgTypeService().then((res) => {
    msgTypeList.value = res.data;
  });
  getMsgsCheckedService(1, showMsgNum.value)
    .then((res) => {
      if (res.code.includes("OK")) {
        msgAllList.value = res.data.records;
        totalNum.value = res.data.total;
        msgList.value = msgAllList.value;
      } else {
        msgList.value = {};
      }
    })
    .catch(() => {
      msgList.value = {};
    });
};
//根据类型筛选已审核帖子
const getAllMsgsByType = (type) => {
  getMsgsCheckedByTypeService(type, 1, showMsgNum.value)
    .then((res) => {
      if (res.code.includes("OK")) {
        msgList.value = res.data.records;
        totalNum.value = res.data.total;
      } else {
        msgList.value = {};
      }
    })
    .catch(() => {
      msgList.value = {};
    });
};
//选择的帖子类型
const msgType = ref("-1");

//排序帖子
const sortMsg = (sort) => {
  stores.commit("setMsgSort", sort);
  if (msgType.value == "-1") {
    getAllMsgs();
  } else {
    getAllMsgsByType(msgType.value);
  }
};

const loadMsg = () => {
  if (showMsgNum.value >= totalNum.value) {
    return;
  }
  showMsgNum.value += 3;
  if (msgType.value == -1) {
    getAllMsgs();
  } else {
    getAllMsgsByType(msgType.value);
  }
  console.log(showMsgNum.value);
};

//点击左侧菜单变换数据
const changeMsgList = (i, path, item) => {
  msgType.value = i;
  if (i == "-1") {
    getAllMsgs();
  } else {
    // filterMsgList(i)
    getAllMsgsByType(msgType.value);
  }
};
//前端筛选数据
const filterMsgList = (type) => {
  msgList.value = msgAllList.value.filter((msg) => {
    return msg.type == type;
  });
};

//根据浏览器宽高控制左侧菜单宽高
const changeMenu = (element) => {
  const w = element.offsetWidth * 0.1;
  if (w < 110) {
    isCollapse.value = false;
    isControl.value = false;
  } else {
    isCollapse.value = true;
    isControl.value = true;
  }
};

//展开菜单前判断是否可展开
const isChange = () => {
  if (!isControl.value) {
    ElMessage.error("当前宽度不够，无法展开");
    return false;
  }
  return true;
};

//左侧菜单变化时，更改宽度
const changeLayout = () => {
  leftWidth.value = document.querySelector(".leftMenu").offsetWidth;
};

onMounted(() => {
  //初始获取全部帖
  getAllMsgs();

  //监听容器变化
  const erl = elementResizeListen();
  erl.listenTo(document.querySelector(".discuss-box"), (element) => {
    changeMenu(element);
  });
});
</script>

<style scoped>
.discuss-box {
  height: 100%;
  justify-content: center;
  align-items: center;
}
.discuss-box :deep(.title) {
  font-size: large;
  font-weight: bold;
}
.discuss-box :deep(.userName) {
  overflow: hidden; /* 控制溢出部分的处理方式 */
  white-space: nowrap; /* 防止换行 */
  text-overflow: ellipsis; /* 显示省略号 */
  /* 支持多行文本省略 */
  -webkit-line-clamp: 3; /* 指定显示的行数 */
  -webkit-box-orient: vertical; /* 垂直排列 */
  font-size: small;
  overflow-x: hidden;
  overflow-y: hidden;
}
.discuss-box :deep(.msg-box) {
  margin: 5px 0px;
  background-color: #f2e7e5;
}

.discuss-box :deep(.tabIcon) {
  display: flex;
  align-items: center;
}

.discuss-box :deep(.msg-icon:hover) {
  cursor: pointer;
  color: blue;
}
</style>