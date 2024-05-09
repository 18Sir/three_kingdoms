<template>
  <div class="search-box">
    <el-autocomplete
      ref="autocomplete"
      v-model="searchString"
      :fetch-suggestions="querySearchAsync"
      clearable
      :placeholder="'请输入要搜索的' + selectTitle+'内容'"
      @select="actorSelect"
      @keyup.enter="searchEnter"
    >
      <template #prefix>
        <el-icon><Search /></el-icon>
      </template>
      <template #append>
        <el-button type="primary" @click="searchEnter">搜索</el-button>
      </template>
    </el-autocomplete>
    <!-- 下拉选择检索方式 -->
    <el-dropdown @command="selectType" role="navigation" size="small">
      <span class="dropdown-title">
        当前搜：{{ selectTitle }} <el-icon><ArrowDown /></el-icon
      ></span>
      <template #dropdown>
        <el-dropdown-menu>
          <el-dropdown-item command="0">{{ selectList[0] }}</el-dropdown-item>
          <el-dropdown-item command="1">{{ selectList[1] }}</el-dropdown-item>
          <el-dropdown-item command="2">{{ selectList[2] }}</el-dropdown-item>
        </el-dropdown-menu>
      </template>
    </el-dropdown>
  </div>
</template>
  
<script setup>
import { onMounted, ref } from "vue";
import { getAllActorService, getAllActorByNameService } from "@/api/actor.js";
import router from "@/router";
import { getAllEventsService, getEventsByNameService } from "@/api/event";

const searchString = ref("");
const autocomplete = ref();

onMounted(() => {
  // changeDefaultSelect()
});

//对应页面，默认对应搜索类型
const changeDefaultSelect = () => {
  const path = router.currentRoute.value.path;
  if (path.includes("event")) {
    return selectList.value[1];
  }else if(path.includes("actor")){
    return selectList.value[0]
  }
  return selectList.value[2];
};

//选择搜索类型变量
const selectList = ref(["人物", "事件", "综合"]);
const selectTitle = ref(changeDefaultSelect());

//模糊查询方法
const querySearchAsync = (queryString, cb) => {
  //搜人物
  if (selectTitle.value === selectList.value[0]) {
    //异步获取查询人物资源
    loadAllActor().then((res) => {
      const results = queryString
        ? res.filter(createFilter(queryString, res))
        : res;
      // call callback function to return suggestions
      cb(results);
    });
  } //搜事件
  else if (selectTitle.value === selectList.value[1]) {
    //异步获取查询事件资源
    loadAllEvent().then((res) => {
      const results = queryString
        ? res.filter(createFilter(queryString, res))
        : res;
      // call callback function to return suggestions
      cb(results);
    });
  } //综合人物与事件
  else if (selectTitle.value === selectList.value[2]) {
    loadAllEvent().then((resEvent) => {
      loadAllActor().then((resActor) => {
        const res = resEvent.concat(resActor);
        const results = queryString
          ? res.filter(createFilter(queryString, res))
          : res;
        // call callback function to return suggestions
        cb(results);
      });
    });
  }
};
//过滤器过滤字符方法
const createFilter = (queryString) => {
  return (res) => {
    return (
      // restaurant.value.toLowerCase().indexOf(queryString.toLowerCase()) != -1
      /*index==0表示是否在第一个索引位置找到输入的字符，*/
      res.value.indexOf(queryString) != -1 //这个语句表示只要有关键字即可，不论在哪个位置匹配到
    );
  };
};
//加载人物资源
async function loadAllActor() {
  let name = searchString.value;
  let results = [];
  if (name !== "") {
    results = (await getAllActorByNameService(name, 1, 15)).data.records;
  } else {
    results = (await getAllActorService(1, 100)).data.records;
  }

  let actorList = [];
  if (results != null) {
    for (const key in results) {
      const actorName =
        results[key].afname +
        (results[key].atname ? "  字" + results[key].atname : "");

      actorList.push({
        value: actorName + "（人物）",
        link: "/actor/info/" + results[key].aid + "/" + results[key].afname,
      });
    }
  }
  return actorList;
}

//加载事件资源
async function loadAllEvent() {
  let name = searchString.value;
  let results = [];
  if (name !== "") {
    results = (await getEventsByNameService(name, 1, 15)).data.records;
  } else {
    results = (await getAllEventsService(1, 15)).data.records;
  }

  let eventList = [];
  if (results != null) {
    for (const key in results) {
      const eventName = results[key].ename;

      eventList.push({
        value: eventName + "（事件）",
        link: "/event/info/" + results[key].eid + "/" + results[key].ename,
      });
    }
  }

  return eventList;
}

//选择搜索类型
const selectType = (command) => {
  selectTitle.value = selectList.value[command];
};
//选中人物
const actorSelect = (item) => {
  searchString.value = "";
  router.push({
    path: item.link,
  });
};

//回车事件
const searchEnter = () => {
  const name = searchString.value ? searchString.value : "all";
  let path = `/actor/more/${name}`;
  //如果是搜索事件，则更改路径
  if (selectTitle.value === selectList.value[1]) {
    path = `/event/more/${name}`;
  }
  router.push({
    path,
  });
};
</script>
<style scoped>
.dropdown-title {
  cursor: pointer;
  color: #add5a2;
  display: flex;
  align-items: center;
  margin-left: 10px;
}
.dropdown-title:focus {
  outline: none;
}
.search-box {
  height: 100%;
  display: flex;
  align-items: center;
}
</style>
  