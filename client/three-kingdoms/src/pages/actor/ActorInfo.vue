<template>
  <el-container>
    <div class="card" style="width: 100%">
      <Splitter style="height: auto">
        <SplitterPanel
          class="flex align-items-start justify-content-center"
          :size="10"
          :minSize="7"
          :maxSize="15"
          style="padding-top: 15px"
          ><div style="position: fixed">
            <el-timeline>
              <el-timeline-item
                v-for="(activity, index) in activities"
                :key="index"
                :timestamp="activity.timestamp"
              >
                {{ activity.content }}
              </el-timeline-item>
            </el-timeline>
          </div>
        </SplitterPanel>
        <SplitterPanel
          class="flex align-items-center justify-content-center"
          :size="60"
          :minSize="50"
          style="padding-top: 15px;"
        >
          <div>
            <el-container>
              <el-header class="storyTitle"
                ><h1>{{ actorData.title }}</h1>
              </el-header>
              <el-main>
                <div
                  v-if="actorData.story !== ''"
                  v-html="actorData.story"
                  class="story-box"
                ></div>
                <el-empty v-else description="敬请期待管理员添加此人的事迹">
                </el-empty>
              </el-main>
            </el-container>
          </div>
        </SplitterPanel>
        <SplitterPanel
          class="flex align-items-center justify-content-center"
          :size="30"
          :minSize="25"
          style="height: 80vh;"
        >
          <MapVue ref="visualMapRef" />
          <span style="position: absolute;bottom: 2rem;color: cadetblue;">点击左边的带下划线的文字，即可在上方地图查看地点位置</span>
        </SplitterPanel>
      </Splitter>
    </div>
  </el-container>

  <el-backtop :right="100" :bottom="100">
    <div style="width: 100%; height: 100%; text-align: center; font-size: 14px">
      回到顶部
    </div>
  </el-backtop>
</template>

<script setup>
import MapVue from "@/views/base/VisualMap.vue";
import { getActorInfoById, getActorEvents } from "@/api/actor";
import router from "@/router";
import { nextTick, onMounted, ref, watch } from "vue";

import Splitter from "primevue/splitter";
import SplitterPanel from "primevue/splitterpanel";

const aid = ref(
  router.currentRoute.value.params.id
    ? router.currentRoute.value.params.id
    : 10000
);
const visualMapRef = ref(null);

const actorData = ref({});

//左侧时间戳的
const activities = ref([]);
const getEvents = () => {
  getActorEvents(aid.value).then((res) => {
    if (res.data != null) {
      activities.value = [];
      let eventsData = res.data;
      for (const key in eventsData) {
        let data = {
          content: eventsData[key].ename,
          timestamp: eventsData[key].etime,
        };
        activities.value.push(data);
      }
    } else {
      activities.value = [];
      activities.value.push({
        content: "暂无数据",
      });
    }
  });
};
//中间传记内容
const getInfo = () => {
  getActorInfoById(aid.value).then(async (res) => {
    let data = {
      story: storyFormat(res.data.story),
      title: titleFormat(res.data.afname),
    };
    actorData.value = data;
    await nextTick();
    addListener();
  });
};
//故事的数据格式处理
const storyFormat = (story) => {
  if (!story) {
    story = "";
  } else {
    story = story.replace(/\r\n\r\n/g, "</br>");
  }

  return story;
};
//标题数据校验
const titleFormat = (title) => {
  if (title === "刘备") {
    title = "先主传（刘备传）";
  } else if (title === "刘禅") {
    title = "后主传（刘禅传）";
  } else {
    title += "传";
  }

  return title;
};
//在地图显示地点逻辑
const addListener = () => {
  let addrList = document.getElementsByClassName("addr");
  for (const addr of addrList) {
    addr.addEventListener('click',function(){
      visualMapRef.value.changeOptions(this.name);
    })
  }
};

//初始化数据
onMounted(() => {
  getEvents();
  getInfo();
});

//侦听路径传参数据变化
watch(
  () => router.currentRoute.value.params,
  (nv, ov) => {
    aid.value = nv.id;
    getEvents();
    getInfo();
  }
);

//拿到数据

//显示
</script>

<style scoped>
.el-main {
  height: 80vh;
}

.story-box {
  line-height: 25px;
}
.storyTitle {
  height: 5vh;
  line-height: 5vh;
  font-size: 5vh;
  text-align: center;
}
.story-box:deep(.addr) {
  text-decoration: underline;
  color: blue;
  cursor: pointer;
}
</style>