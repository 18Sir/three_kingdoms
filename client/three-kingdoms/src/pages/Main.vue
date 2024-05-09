<template>
  <el-header><HeaderVue /></el-header>
  <el-main><MapVue /></el-main>
  <div class="card">
    <div class="dock-window dock-advanced">
      <Dock :model="items">
        <template #item="{ item }">
          <a
            v-tooltip.top="item.label"
            href="#"
            class="p-dock-link"
            @click="onDockItemClick($event, item)"
          >
            <img
              :alt="item.label"
              :src="item.icon"
              style="width: 100%"
              class="p-dock-icon"
            />
          </a>
        </template>
      </Dock>
    </div>
  </div>

</template>

<script setup>
import { provide, ref } from "vue";
import HeaderVue from "@/views/header/Header.vue";
import MapVue from "@/views/base/Map.vue";
import Dock from "primevue/dock";

import bingzhou from "../assets/images/bingzhou.png";
import all from "../assets/images/all.png"
import three_kingdoms from "../assets/images/three_kingdoms.png"
import jiaozhou from "../assets/images/jiaozhou.png"
import jingzhou from "../assets/images/jingzhou.png"
import jizhou from "../assets/images/jizhou.png"
import liangzhou from "../assets/images/liangzhou.png"
import qingzhou from "../assets/images/qingzhou.png"
import sizhou from "../assets/images/sizhou.png"
import xuzhou from "../assets/images/xuzhou.png"
import yangzhou from "../assets/images/yangzhou.png"
import yanzhou from "../assets/images/yanzhou.png"
import yizhou from "../assets/images/yizhou.png"
import yongzhou from "../assets/images/yongzhou.png"
import youzhou from "../assets/images/youzhou.png"
import yuzhou from "../assets/images/yuzhou.png"
import stores from "@/stores";

provide("index", "1");

const items = ref([
  {
    label: "全部地图",
    icon: all,
    command: () => {
      stores.commit("setMap",{name:'map',index:0})
    },
  },
  {
    label: "仅三国",
    icon: three_kingdoms,
    command: () => {
      stores.commit("setMap",{name:"three_kingdoms",index:1})
    },
  },
  {
    label: "并州",
    icon: bingzhou,
    command: () => {
      stores.commit("setMap",{name:"bingzhou",index:2})
    },
  },
  {
    label: "交州",
    icon: jiaozhou,
    command: () => {
      stores.commit("setMap",{name:"jiaozhou",index:3})
    },
  },
  {
    label: "荆州",
    icon: jingzhou,
    command: () => {
      stores.commit("setMap",{name:"jingzhou",index:4})
    },
  },
  {
    label: "冀州",
    icon: jizhou,
    command: () => {
      stores.commit("setMap",{name:"jizhou",index:5})
    },
  },
  {
    label: "凉州",
    icon: liangzhou,
    command: () => {
      stores.commit("setMap",{name:"liangzhou",index:6})
    },
  },
  {
    label: "青州",
    icon: qingzhou,
    command: () => {
      stores.commit("setMap",{name:"qingzhou",index:7})
    },
  },
  {
    label: "司州",
    icon: sizhou,
    command: () => {
      stores.commit("setMap",{name:"sizhou",index:8})
    },
  },
  {
    label: "徐州",
    icon: xuzhou,
    command: () => {
      stores.commit("setMap",{name:"xuzhou",index:9})
    },
  },
  {
    label: "扬州",
    icon: yangzhou,
    command: () => {
      stores.commit("setMap",{name:"yangzhou",index:10})
    },
  },
  {
    label: "兖州",
    icon: yanzhou,
    command: () => {
      stores.commit("setMap",{name:"yanzhou",index:11})
    },
  },
  {
    label: "益州",
    icon: yizhou,
    command: () => {
      stores.commit("setMap",{name:"yizhou",index:12})
    },
  },
  {
    label: "雍州",
    icon: yongzhou,
    command: () => {
      stores.commit("setMap",{name:"yongzhou",index:13})
    },
  },
  {
    label: "幽州",
    icon: youzhou,
    command: () => {
      stores.commit("setMap",{name:"youzhou",index:14})
    },
  },
  {
    label: "豫州",
    icon: yuzhou,
    command: () => {
      stores.commit("setMap",{name:"yuzhou",index:15})
    },
  },
]);

const onDockItemClick = (event, item) => {
  if (item.command) {
    item.command();
  }

  event.preventDefault();
};
</script>

<style>
.el-header {
  width: 100%;
  height: 8vh;
}
.el-main {
  width: 100%;
  height: 92vh;
  padding: 0px;
}
.dock-window {
  width: 100%;
  height: 80px;
  position: fixed;
  bottom: 10px;
  z-index: 10;
}
.p-dock .p-dock-list-container{
  padding: 0.5rem;
}
.p-dock .p-dock-list-container:hover{
  margin: 0 1rem;
}
.p-dock-link {
  width: 3rem;
  padding: 0.5rem;
}
.p-dock .p-dock-item{
  padding: 0.5rem;
}

.p-tooltip .p-tooltip-top{
  padding: 0.25rem 0;
}

.p-tooltip .p-tooltip-text{
  padding: 0.5rem 0.75rem;
}
</style>