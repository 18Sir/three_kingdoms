<template>
  <div class="map" id="map"></div>
  <el-dialog
    v-model="addrDialog"
    :title="addrData.addrName"
    width="800"
    :before-close="handleClose"
    align-center
    draggable
  >
    <div v-html="addrData.detail ? addrData.detail :'暂无数据，敬请期待'" style="font-size: larger;line-height: 25px"></div>
    <template #footer>
      <div class="dialog-footer">
        <el-button type="primary" @click="addrDialog = false"> 确认 </el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script setup>
import * as echarts from "echarts";
import all from "@/json/map/three_kingdoms.json";
import three_kingdoms from "@/json/map/three_kingdoms2.json";
import bingzhou from "@/json/map/bingzhou.json";
import jiaozhou from "@/json/map/jiaozhou.json";
import jingzhou from "@/json/map/jingzhou.json";
import jizhou from "@/json/map/jizhou.json";
import liangzhou from "@/json/map/liangzhou.json";
import qingzhou from "@/json/map/qingzhou.json";
import sizhou from "@/json/map/sizhou.json";
import xuzhou from "@/json/map/xuzhou.json";
import yangzhou from "@/json/map/yangzhou.json";
import yanzhou from "@/json/map/yanzhou.json";
import yizhou from "@/json/map/yizhou.json";
import yongzhou from "@/json/map/yongzhou.json";
import youzhou from "@/json/map/youzhou.json";
import yuzhou from "@/json/map/yuzhou.json";
import { computed, onMounted, ref, watch } from "vue";
import stores from "@/stores";
import Image from "primevue/image";
import { getAddrByNameService } from "@/api/addr";

const addrData = ref({});
const addrDialog = ref(false);
const mapChart = ref({});

onMounted(() => {
  initMap();
});

const initMap = () => {
  mapChart.value = echarts.init(document.getElementById("map"));
  echarts.registerMap("map", { geoJSON: all });
  echarts.registerMap("three_kingdoms", { geoJSON: three_kingdoms });
  echarts.registerMap("bingzhou", { geoJSON: bingzhou });
  echarts.registerMap("jiaozhou", { geoJSON: jiaozhou });
  echarts.registerMap("jingzhou", { geoJSON: jingzhou });
  echarts.registerMap("jizhou", { geoJSON: jizhou });
  echarts.registerMap("liangzhou", { geoJSON: liangzhou });
  echarts.registerMap("qingzhou", { geoJSON: qingzhou });
  echarts.registerMap("sizhou", { geoJSON: sizhou });
  echarts.registerMap("xuzhou", { geoJSON: xuzhou });
  echarts.registerMap("yangzhou", { geoJSON: yangzhou });
  echarts.registerMap("yanzhou", { geoJSON: yanzhou });
  echarts.registerMap("yizhou", { geoJSON: yizhou });
  echarts.registerMap("yongzhou", { geoJSON: yongzhou });
  echarts.registerMap("youzhou", { geoJSON: youzhou });
  echarts.registerMap("yuzhou", { geoJSON: yuzhou });

  mapChart.value.setOption(option);
  mapChart.value.on("click", function (params) {
    console.log("点击了地图", params);
    getAddrData(params.name);
  });
};

const getAddrData = (name) => {
  getAddrByNameService(name).then((res) => {
    console.log(res);
    addrData.value = res.data;
    addrDialog.value = true;
  });
};

const mapName = computed(() => {
  return stores.getters.getMapName;
});
watch(
  () => stores.getters.getMapName,
  (nv, ov) => {
    console.log(stores.getters.getMapIndex);
    switch (stores.getters.getMapIndex) {
      case 0:
        option.geo.zoom = 2;
        option.geo.center = [110, 35];
        option.geo.scaleLimit = {
          min: 1,
          max: 10,
        };
        break;
      case 1:
        option.geo.zoom = 2;
        option.geo.center = [113, 30];
        option.geo.scaleLimit = {
          min: 1,
          max: 10,
        };
        break;
      case 2:
        option.geo.zoom = 20;
        option.geo.center = [113, 37];
        option.geo.scaleLimit = {
          min: 8,
          max: 20,
        };
        break;
      case 3:
        option.geo.zoom = 10;
        option.geo.center = [110, 20];
        option.geo.scaleLimit = {
          min: 8,
          max: 20,
        };
        break;
      case 4:
        option.geo.zoom = 10;
        option.geo.center = [112, 28];
        option.geo.scaleLimit = {
          min: 8,
          max: 20,
        };
        break;
      case 5:
        option.geo.zoom = 15;
        option.geo.center = [116, 37];
        option.geo.scaleLimit = {
          min: 12,
          max: 25,
        };
        break;
      case 6:
        option.geo.zoom = 10;
        option.geo.center = [100, 39];
        option.geo.scaleLimit = {
          min: 8,
          max: 20,
        };
        break;
      case 7:
        option.geo.zoom = 15;
        option.geo.center = [120, 35];
        option.geo.scaleLimit = {
          min: 12,
          max: 25,
        };
        break;
      case 8:
        option.geo.zoom = 15;
        option.geo.center = [112, 34];
        option.geo.scaleLimit = {
          min: 12,
          max: 25,
        };
        break;
      case 9:
        option.geo.zoom = 15;
        option.geo.center = [118, 34];
        option.geo.scaleLimit = {
          min: 12,
          max: 25,
        };
        break;
      case 10:
        option.geo.zoom = 10;
        option.geo.center = [117, 28];
        option.geo.scaleLimit = {
          min: 8,
          max: 20,
        };
        break;
      case 11:
        option.geo.zoom = 20;
        option.geo.center = [116, 35];
        option.geo.scaleLimit = {
          min: 15,
          max: 35,
        };
        break;
      case 12:
        option.geo.zoom = 6;
        option.geo.center = [107, 28];
        option.geo.scaleLimit = {
          min: 5,
          max: 15,
        };
        break;
      case 13:
        option.geo.zoom = 15;
        option.geo.center = [109, 35];
        option.geo.scaleLimit = {
          min: 12,
          max: 25,
        };
        break;
      case 14:
        option.geo.zoom = 15;
        option.geo.center = [120, 39];
        option.geo.scaleLimit = {
          min: 12,
          max: 25,
        };
        break;
      case 15:
        option.geo.zoom = 18;
        option.geo.center = [115, 33];
        option.geo.scaleLimit = {
          min: 15,
          max: 30,
        };
        break;
    }
    option.geo.map = mapName.value;
    mapChart.value.setOption(option);
  }
);

let option = {
  geo: {
    type: "map",
    map: mapName.value,
    roam: true,
    label: {
      show: true,
    },
    center: [110, 35],
    layoutCenter: ["50%", "50%"],
    layoutSize: "80%",
    zoom: 2,
    scaleLimit: {
      min: 1,
      max: 10,
    },
  },
  series: [
    {
      type: "scatter",
      coordinateSystem: "geo",
      label: {
        show: true,
        formatter: "{b}",
        textStyle: {
          color: "#666",
        },
      },
      symbolSize: 1,
    },
  ],
  tooltip: {
    show: true,
    trigger: "item",
    triggerOn: "mousemove",
    formatter: "郡名：{b}",
  },
};
</script>

<style scoped>
.map {
  width: 100%;
  height: 100%;
  margin-top: 0px;
  background-color: white;
}
</style>