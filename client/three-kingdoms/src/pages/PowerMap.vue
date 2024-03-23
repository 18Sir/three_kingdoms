<template>
  <div id="power-map" class="power-map"></div>
</template>

<script setup>
import * as echarts from "echarts";
import { computed, onMounted, ref, watch } from "vue";
import powerMap from "@/json/map/powerMap.json";
import { getAllPowerService, getPowerByIdService } from "@/api/power";
import { ElMessage } from "element-plus";
import stores from "@/stores";

const pid = computed(() => {
  return stores.getters.getPid;
});
const powerData = ref({});
const mapChart = ref();

const initMap = () => {
  mapChart.value = echarts.init(document.getElementById("power-map"));
  echarts.registerMap("powerMap", { geoJSON: powerMap });
  mapChart.value.setOption(option);
};

onMounted(() => {
  initMap();
  getPowerById();
});

//返回全部颜色
const getAllColor = () => {
  let color = [
    "#f07c82",
    "#93b5cf",
    "#57c3c2",
    "#d2d97a",
    "#ce5e8a",
    "#fca104",
    "#b89485",
    "#f6cec1",
    "#f25a47",
    "#efafad",
    "#edc3ae",
    "#fbcd31",
    "#bacf65",
    "#5bae23",
    "#9fa39a",
    "#6e8b74",
    "#66c18c",
    "#c0c4c3",
    "#c6e6e8",
  ];
  return color;
};

let option = {
//   geo: {
//     type: "map",
//     map: "powerMap",
//     roam: true,
//     label: {
//       show: false,
//     },
//     center: [110, 30],
//     layoutCenter: ["50%", "50%"],
//     zoom: 3,
//     scaleLimit: {
//       min: 1,
//       max: 10,
//     },
//   },
  visualMap: {
    show: true,
    type: "piecewise", // 定义为连续型 viusalMap
    pieces: [],
    color: getAllColor(),
  },
  series: {
    type: "map",
    // coordinateSystem: "geo",
    map:"powerMap",
    zoom:3,
    x:'center',
    center: [110, 30],
    roam:true,
    label: {
      show: false,
      formatter: "{b}",
      textStyle: {
        color: "#666",
      },
    },
    symbolSize: 1,
    data: [],
  },
  tooltip: {
    show: true,
    trigger: "item",
    triggerOn: "mousemove",
    formatter: "{b}",
  },
};

watch(
  () => stores.getters.getPid,
  (nv, ov) => {
    getPowerById();
  }
);

//根据id获取势力配置
const getPowerById = () => {
  getPowerByIdService(pid.value)
    .then((res) => {
      if (res.data != null) {
        powerData.value = res.data;
        // console.log(Object.keys(powerData.value.paddr).length);
        changeOptions();
      } else {
        ElMessage.error("势力信息访问出错，请稍后重试");
      }
    })
    .catch(() => {
      ElMessage.error("势力信息访问出错，请稍后重试");
    });
};

//更改配置
const changeOptions = () => {
  const pd = powerData.value;
  let data = [];
  let seriesData = []
  for (const key in pd.paddr) {
    let d = {
      label: pd.pactor.name[pd.paddr[key].type],
      value: pd.paddr[key].type,
    };
    for (const i in pd.paddr[key].addr) {
      let d2 = {
        name: pd.paddr[key].addr[i],
        value: pd.paddr[key].type,
      };
      seriesData.push(d2)
    }
    data.push(d);
  }
  option.visualMap.pieces = data;
  option.series.data = seriesData;
  mapChart.value.setOption(option);
    console.log(option);
};

//配置地图颜色
const getAreaColor = (type) => {
  let color;
  switch (type) {
    case 0:
      color = "#f07c82";
      break;
    case 1:
      color = "#93b5cf";
      break;
    case 2:
      color = "#57c3c2";
      break;
    case 3:
      color = "#d2d97a";
      break;
    case 4:
      color = "#ce5e8a";
      break;
    case 5:
      color = "#fca104";
      break;
    case 6:
      color = "#b89485";
      break;
    case 7:
      color = "#f6cec1";
      break;
    case 8:
      color = "#f25a47";
      break;
    case 9:
      color = "#efafad";
      break;
    case 10:
      color = "#edc3ae";
      break;
    case 11:
      color = "#fbcd31";
      break;
    case 12:
      color = "#bacf65";
      break;
    case 13:
      color = "#5bae23";
      break;
    case 14:
      color = "#9fa39a";
      break;
    case 15:
      color = "#6e8b74";
      break;
    case 16:
      color = "#66c18c";
      break;
    case 17:
      color = "#c0c4c3";
      break;
    case 18:
      color = "#c6e6e8";
      break;
    default:
      color = "white";
      break;
  }
  return color;
};
</script>

<style>
.power-map {
  width: 100%;
 
 height: 100%;
}
.power-map canvas{
    width: 50rem;
}
</style>