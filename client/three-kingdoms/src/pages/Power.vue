<template>
  <el-container>
    <el-header> <HeaderVue /> </el-header>
    <el-main>
      <div class="card">
        <Splitter style="height: auto">
          <SplitterPanel
            class="flex align-items-center justify-content-center"
            :size="20"
            :minSize="10"
          >
            <div class="card flex justify-content-center">
              <Listbox
                v-model="selectedCity"
                :options="powers"
                filter
                optionLabel="name"
                class="w-full md:w-14rem"
                @change="changePower"
              />
            </div>
          </SplitterPanel>
          <SplitterPanel
            class="flex align-items-center justify-content-center"
            :size="80"
            :minSize="60"
          >
            <div class="power-map-box">
              <div style="width: 100%; display: flex;justify-content: space-around;position: fixed;">
                <el-text
                  >白色地区为汉朝或未知势力</el-text
                >
              </div>

              <PowerMap />
            </div>
          </SplitterPanel>
        </Splitter>
      </div>
    </el-main>
  </el-container>
</template>

<script setup>
import HeaderVue from "./views/Header.vue";
import { onMounted, provide, ref } from "vue";
import PowerMap from "./PowerMap.vue";
import { getAllPowerService } from "../api/power";
import stores from "@/stores";
provide("index", "4");

const selectedCity = ref();
const powerList = ref([]);
const powers = ref([]);
//势力列表同步至左侧
const reList = () => {
  for (const key in powerList.value) {
    const data = {
      name: powerList.value[key].pname,
      code: powerList.value[key].pid,
    };
    powers.value.push(data);
  }
};

//获取势力配置列表
const getPowerAll = () => {
  getAllPowerService()
    .then((res) => {
      if (res.data != null) {
        powerList.value = res.data;
      } else {
        ElMessage.error("势力信息访问出错，请稍后重试");
      }
      reList();
    })
    .catch(() => {
      ElMessage.error("势力信息访问出错，请稍后重试");
      console.log("catch");
    });
};

const changePower = (e) => {
  stores.commit("setPid", e.value.code);
};

onMounted(() => {
  getPowerAll();
});
</script>

<style>
.p-listbox {
  padding: 10px;
}
.p-listbox .p-listbox-header {
  height: 2.5rem;
  /* padding: 0.5rem; */
}
.p-listbox-filter {
  height: 2rem;
  display: flex;
  align-items: center;
  padding: 0.5rem 0.75rem;
}
.p-listbox-filter-icon {
  margin-top: -0.5rem;
}
.p-listbox-item {
  padding: 0.5rem 0.3rem;
}
.power-map-box {
  width: 100%;
  height: 90vh;
}
</style>