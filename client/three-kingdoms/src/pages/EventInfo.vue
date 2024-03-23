<template>
  <el-container>
    <div class="card">
      <Splitter style="height: auto">
        <SplitterPanel
          class="flex align-items-start justify-content-center"
          style="padding-top: 15px;"
          :size="10"
          :minSize="7"
        >
          <div style="position: fixed;">
            <el-row class="tac">
              <el-col :span="24">
                <el-affix :offset="90">
                  <h5 class="mb-2">相关人物</h5>
                  <el-menu class="el-menu-vertical-demo" @select="selectActor">
                    <el-menu-item-group
                      v-for="(item, index) in actorList"
                      :key="index"
                    >
                      <el-menu-item :index="item.afname" :path="'e' + index">
                        <!-- <el-icon><icon-menu /></el-icon> -->
                        <span>{{ item.afname }}</span>
                      </el-menu-item>
                    </el-menu-item-group>
                  </el-menu>
                </el-affix>
              </el-col>
            </el-row>
          </div>
        </SplitterPanel>
        <SplitterPanel
          class="flex align-items-center justify-content-center"
          :size="75"
          :minSize="50"
          style="padding-top: 15px;"
        >
          <div>
            <el-container>
              <el-header class="storyTitle"
                ><h1>{{ eventData.title }}</h1></el-header
              >
              <el-main>
                <div
                  v-if="eventData.story"
                  v-html="eventData.story"
                  class="story-box"
                ></div>
                <el-empty v-else description="敬请期待管理员添加此事件内容">
                </el-empty>
              </el-main>
            </el-container>
          </div>
        </SplitterPanel>
        <SplitterPanel
          class="flex align-items-center justify-content-center"
          :size="20"
          :minSize="15"
          style="height: 100%"
        >
          <MapVue />
        </SplitterPanel>
      </Splitter>
    </div>

    <!-- <el-aside width="10%">
      <el-row class="tac">
        <el-col :span="24">
          <el-affix :offset="90">
            <h5 class="mb-2">相关人物</h5>
            <el-menu class="el-menu-vertical-demo" @select="selectActor">
              <el-menu-item-group
                v-for="(item, index) in actorList"
                :key="index"
              >
                <el-menu-item :index="item.afname" :path="'e' + index">
                  <span>{{ item.afname }}</span>
                </el-menu-item>
              </el-menu-item-group>
            </el-menu>
          </el-affix>
        </el-col>
      </el-row>
    </el-aside> -->
    <!-- <el-container>
      <el-header class="storyTitle"
        ><h1>{{ eventData.title }}</h1></el-header
      >
      <el-main>
        <div
          v-if="eventData.story"
          v-html="eventData.story"
          class="story-box"
        ></div>
        <el-empty v-else description="敬请期待管理员添加此事件内容"> </el-empty>
      </el-main>
    </el-container> -->
  </el-container>
  <el-backtop :right="100" :bottom="100">
    <div style="width: 100%; height: 100%; text-align: center; font-size: 14px">
      回到顶部
    </div>
  </el-backtop>
</template>

<script setup>
import { getEventByIdService } from "@/api/event";
import router from "@/router";
import { onMounted, ref, watch } from "vue";

const eventData = ref({});
const eid = ref(
  router.currentRoute.value.params.id
    ? router.currentRoute.value.params.id
    : 1000
);
//人物列表
const actorList = ref([]);

onMounted(() => {
  getInfo();
});
//存储基础事件内容，后续在此基础上修改格式
const baseStory = ref("");

//中间事件内容
const getInfo = () => {
  getEventByIdService(eid.value).then((res) => {
    let data = {
      story: storyFormat(res.data.econtent),
      title: res.data.ename,
    };
    eventData.value = data;
    baseStory.value = data.story;
    actorList.value = res.data.actorList;
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

//选中左侧人物事件
const selectActor = (index, path, item) => {
  let re = new RegExp(index, "g");
  // eventData.value.story = baseStory.value.replace(index, "<span id='e"+item.path+"' class='light-name'>"+index+"</span>");
  eventData.value.story = baseStory.value.replace(
    re,
    "<span class='light-name'>" + index + "</span>"
  );
  let time = setTimeout(() => {
    let target = document.querySelector(".light-name");
    if (target) {
      clearTimeout(time);
      //平滑跳转
      target.scrollIntoView({ behavior: "smooth" });
    }
  }, 10);
};

//侦听路由变化，改变页面数据
watch(
  () => router.currentRoute.value.params,
  (nv, ov) => {
    eid.value = nv.id;
    getInfo();
  }
);
</script>

<style>
.el-menu-item {
  height: 30px;
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
.light-name {
  /* background-color: #d2b42c; */
  color: #645822;
  font-weight: bold;
  font-size: large;
}
</style>