<template>
  <el-container>
    <el-header> <HeaderVue /> </el-header>
    <el-main v-if="show">
      <div class="power-card" style="width: 100%;">
        <Splitter style="height: auto">
          <SplitterPanel
            class="panel1 flex align-items-center justify-content-center"
            :size="20"
            :minSize="10"
            style="width: 20%;align-items: center;"
          >
            <div
              class="list-card flex justify-content-center"
            >
              <Listbox
              v-if="show"
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
            class="panel2 flex align-items-center justify-content-center"
            :size="80"
            :minSize="60"
            style="width: 80%"
          >
            <div class="power-map-box">
              <div
                style="
                  width: 100%;
                  display: flex;
                  justify-content: space-around;
                  position: fixed;
                "
              >
                <el-text style="z-index: 99">白色地区为汉朝或未知势力</el-text>
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
import HeaderVue from "../../views/header/Header.vue";
import { nextTick, onMounted, provide, ref } from "vue";
import PowerMap from "./PowerMap.vue";
import { getAllPowerService } from "../../api/power";
import stores from "@/stores";
import router from "@/router";
const show = ref(false);
provide("index", "/power");

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
  show.value = false;
  nextTick(()=>{
    show.value = true;
  })
});
</script>

<style scoped>
.power-card :deep(.p-listbox) {
  padding: 10px;
}
.power-card :deep(.p-listbox .p-listbox-header) {
  height: 2.5rem;
}
.power-card :deep(.p-listbox-filter) {
  height: 2rem;
  display: flex;
  align-items: center;
  padding: 0.5rem 0.75rem;
}
.power-card :deep(.p-listbox-filter-icon) {
  margin-top: -0.5rem;
}
.power-card :deep(.p-listbox-item) {
  padding: 0.5rem 0.3rem;
}
.power-card :deep(.power-map-box) {
  width: 100%;
  height: 90vh;
}
</style>