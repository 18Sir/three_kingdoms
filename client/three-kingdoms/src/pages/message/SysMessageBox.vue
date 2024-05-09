<template>
    <div class="system-msg-box">
      <div style="margin-bottom:0.6rem;">
        <span style="font-weight: bold;margin-right: 1rem;">{{ title }}</span>
        <span style="color: #999">{{ time }}</span>
      </div>
      <div v-html="content" style="margin-left:1rem;"></div>
    </div>
  </template>
  
  <script setup>
  import { ref } from "vue";
  const props = defineProps({
    title: String,
    content: String,
    time: String,
  });
  </script>
  
  <style scoped>
  .system-msg-box {
    width: 100%;
    margin-bottom: 0.5rem;
    padding: 1.5rem 0.8rem;
    border-radius: 0.3rem;
    background-color: #fff;
    font-size: 14px;
    box-shadow: 2px 2px 8px #aaa;
  }
  </style>