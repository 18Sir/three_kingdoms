<template>
  <div
    id="visual-map"
    class="visual-map"
    style="width: 100%; height: 100%"
  ></div>
</template>
  
  <script setup>
import * as echarts from "echarts";
import { onMounted, ref } from "vue";
import visualMap from "@/json/map/powerMap.json";
import stores from "@/stores";

const visualData = ref({});
const mapChart = ref();

const initMap = () => {
  mapChart.value = echarts.init(document.getElementById("visual-map"));
  echarts.registerMap("visualMap", { geoJSON: visualMap });
  mapChart.value.setOption(option);
};

onMounted(() => {
  initMap();
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
  visualMap: {
    show: true,
    type: "piecewise", // 定义为分段型 viusalMap
    pieces: [{ value: 1, label: "被选中的", color: "aqua" }],
    color: getAllColor(),
  },
  series: {
    type: "map",
    map: "visualMap",
    zoom: 3,
    x: "center",
    center: [110, 30],
    roam: true,
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

//更改配置
const changeOptions = (name) => {
  let data = { name: name, value: 1 };
  let seriesData = [];
  seriesData.push(data);
  option.series.data = seriesData;
  mapChart.value.setOption(option);
};
//向外暴露方法
defineExpose({
    changeOptions
})

</script>
  
  <style scoped>
.visual-map {
  width: 100%;
  height: 100%;
}
.visual-map :deep(canvas) {
  min-width: 100%;
  min-height: 100%;
}
</style>