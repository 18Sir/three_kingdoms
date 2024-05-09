<template>
  <el-container style="max-height: max-content">
    <el-header style="display: flex; justify-content: center">
      <el-row style="width: 70%; align-items: center">
        <!-- 筛选 -->
        <el-select
          v-if="router.currentRoute.value.path.includes('actor')"
          v-model="filter"
          multiple
          class="m-2"
          :placeholder="defaultValue"
          collapse-tags
          collapse-tags-tooltip
          :max-collapse-tags="2"
          size="large"
          style="width: 240px"
          @change="selectChange"
        >
          <el-option
            v-for="item in filterOption"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          />
        </el-select>
      </el-row>
    </el-header>
    <el-main class="main-box">
      <!-- 空数据时展示内容 -->
      <el-empty v-if="dataIsNull()" description="数据走丢了">
        <router-link to="/" style="text-decoration: none">
          <el-button type="primary">返回首页</el-button>
        </router-link>
      </el-empty>
      <!-- 有数据时展示表格 -->
      <div v-else class="table-box">
        <!-- 人物数据 -->
        <el-table
          v-if="router.currentRoute.value.path.includes('actor')"
          ref="actorRef"
          :data="ActorData"
          height="64vh"
          table-layout="fixed"
          highlight-current-row
          @row-click="clickHandle"
        >
          <el-table-column
            fixed
            width="60"
            type="index"
            :index="changeIndex"
          ></el-table-column>
          <el-table-column prop="afname" label="名" />
          <el-table-column prop="atname" label="字" />
          <el-table-column prop="btime" label="生于" />
          <el-table-column prop="dtime" label="死于" />
          <el-table-column prop="power" label="所属势力" />
        </el-table>
        <!-- 事件数据 -->
        <el-table
          v-else-if="router.currentRoute.value.path.includes('event')"
          ref="eventRef"
          :data="eventData"
          height="64vh"
          table-layout="fixed"
          highlight-current-row
          @row-click="clickHandle"
        >
          <el-table-column
            fixed
            width="60"
            type="index"
            :index="changeIndex"
          ></el-table-column>
          <el-table-column prop="ename" label="事件名" />
          <el-table-column prop="etime" label="时间" />
          <el-table-column prop="addrList" label="地区" />
          <el-table-column prop="actorList" label="相关人物" />
        </el-table>
      </div>
    </el-main>
    <el-footer style="height: auto">
      <el-row justify="center"
        ><el-pagination
          background
          :page-size="pageOptions.pageSize"
          :pager-count="pageOptions.pagerCount"
          layout="prev, pager, next"
          :total="pageOptions.total"
          hide-on-single-page
          @current-change="currentChange"
          v-model:current-page="pageOptions.currentPage"
      /></el-row>
    </el-footer>
  </el-container>
</template>

<script setup>
import {
  getActorByNamePowerService,
  getAllActorByNameService,
  getAllActorService,
} from "@/api/actor";
import { inject, onMounted, reactive, ref, toRef, watch } from "vue";
import router from "@/router";
import { getAllEventsService, getEventsByNameService } from "@/api/event";

let actorName = inject("name").value;

//分页配置数据
const pageOptions = ref({
  pageSize: 11,
  pagerCount: 11,
  total: 0,
  currentPage: 1,
});

//同步序号
const changeIndex = (i) => {
  i++;
  return i + (pageOptions.value.currentPage - 1) * pageOptions.value.pageSize;
};

//筛选数据配置
const filter = ref([]);
const filterOption = ref();
const defaultValue = ref("");
//人物数据
const ActorData = ref([]);
//事件数据
const eventData = ref([]);
//判断数据是否为空
const dataIsNull = () => {
  if (
    router.currentRoute.value.path.includes("actor") &&
    ActorData.value.length < 1
  ) {
    return true;
  } else if (
    router.currentRoute.value.path.includes("event") &&
    eventData.value.length < 1
  ) {
    return true;
  } else {
    return false;
  }
};

//筛选数据
const filterData = [
  {
    filter: "可选择势力筛选",
    options: [
      { value: "魏", label: "魏" },
      { value: "蜀", label: "蜀" },
      { value: "吴", label: "吴" },
      { value: "群雄", label: "群雄" },
    ],
  },
];
//改变筛选值
const selectChange = (v) => {
  // console.log("改变", v);
  pageFilter.value = v;
  getWhat(1);
};

//是否开启筛选
const pageFilter = ref([]);

//判断查询数据的方法
const getWhat = (current) => {
  const path = router.currentRoute.value.path;
  const filters = pageFilter.value;
  // console.log(path,filters);
  if (filters.length < 1) {
    //正常查询
    if (path.includes("actor")) {
      (defaultValue.value = filterData[0].filter),
        (filterOption.value = filterData[0].options);
      if (actorName === "all") {
        getAllPageData(current);
      } else {
        getAllByNamePageData(actorName, current);
      }
    }
    if (path.includes("event")) {
      if (actorName === "all") {
        getAllEventPageData(current);
      } else {
        getAllByNameEventPageData(actorName, current);
      }
    }
  } else {
    //筛选查询
    if (path.includes("actor")) {
      getActorByFilter(filters);
    }
  }
};

//初始化数据
onMounted(() => {
  // console.log(router.currentRoute);
  getWhat(1);
});

//筛选查询人物
const getActorByFilter = (v) => {
  let current = pageOptions.value.currentPage;
  const size = pageOptions.value.pageSize;
  getActorByNamePowerService(actorName, current, size, v)
    .then((res) => {
      ActorData.value = res.data.records;
      pageOptions.value.total = res.data.total;
    })
    .catch(() => {
      if (current > 1) {
        pageOptions.value.currentPage = 1;
        getActorByFilter(v);
      }
      ActorData.value = [];
    });
};

//获取所有人物分页数据
const getAllPageData = (current) => {
  getAllActorService(current, pageOptions.value.pageSize)
    .then((res) => {
      // console.log(res);
      ActorData.value = res.data.records;
      pageOptions.value.total = res.data.total;
    })
    .catch(() => {
      ActorData.value = [];
    });
};

//根据名字检索所有人物分页数据
const getAllByNamePageData = (name, current) => {
  getAllActorByNameService(name, current, pageOptions.value.pageSize)
    .then((res) => {
      ActorData.value = res.data.records;
      pageOptions.value.total = res.data.total;
    })
    .catch(() => {
      ActorData.value = [];
    });
};

//获取所有事件分页数据
const getAllEventPageData = (current) => {
  getAllEventsService(current, pageOptions.value.pageSize)
    .then((res) => {
      console.log(res);
      res.data.records = getNameList(res.data.records);
      eventData.value = res.data.records;
      pageOptions.value.total = res.data.total;
    })
    .catch(() => {
      eventData.value = [];
    });
};

//根据名字检索所有事件分页数据
const getAllByNameEventPageData = (name, current) => {
  getEventsByNameService(name, current, pageOptions.value.pageSize)
    .then((res) => {
      res.data.records = getNameList(res.data.records);
      eventData.value = res.data.records;
      pageOptions.value.total = res.data.total;
    })
    .catch(() => {
      eventData.value = [];
    });
};

//事件内只提取人物和地区名
const getNameList = (data) => {
  if (data != null) {
    data = data.map((item) => ({
      eid: item.eid,
      ename: item.ename,
      etime: item.etime,
      actorList: item.actorList.map((item2) => item2.afname),
      addrList: item.addrList.map((item2) => item2.addrName),
    }));
  }
  return data;
};

//分页改变事件
const currentChange = () => {
  // console.log(pageOptions.value.currentPage);
  getWhat(pageOptions.value.currentPage);
};

//表格组件
const actorRef = ref();
const eventRef = ref();

//点击对应人物事件
const clickHandle = (row, column, event) => {
  const path = router.currentRoute.value.path;
  if (path.includes("actor")) {
    router.push({
      path: `/actor/info/${row.aid}/${row.afname}`,
    });
  } else if (path.includes("event")) {
    router.push({
      path: `/event/info/${row.eid}/${row.ename}`,
    });
  }
};

watch(
  () => router.currentRoute.value.params,
  (nv, ov) => {
    actorName = nv.name;
    getWhat(1);
  }
);
</script>

<style scoped>
.el-table {
  width: 80%;
}
.main-box{
  height: auto;
  margin-bottom: 3rem;
}
.table-box {
  width: 100%;
  max-width: 100vw;
  display: flex;
  justify-content: center;
}
</style>