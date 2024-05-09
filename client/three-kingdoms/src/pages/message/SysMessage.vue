<template>
  <div class="sys-layout" style="overflow-y: auto;">
    <div v-for="(item) of newData" :key="item">
        <msgBox :title="item.title" :time="item.time" :content="item.content" />
    </div>
    <div v-for="(item) of historyData" :key="item">
        <msgBox :title="item.title" :time="item.time" :content="item.content" />
    </div>
  </div>
</template>

<script setup>
import { onMounted, ref } from "vue";
import msgBox from "./SysMessageBox.vue"
import {getUserMessageService} from "@/api/user"

const newData = ref();
const historyData = ref();

const getSysMsgData = ()=>{
    getUserMessageService('SYSTEM').then(res=>{
        console.log(res.data)
        newData.value = res.data.new;
        historyData.value = res.data.history;
    })
}

onMounted(()=>{
    getSysMsgData();
})

</script>