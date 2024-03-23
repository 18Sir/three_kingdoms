<template>
    <el-container>
    <el-header> <HeaderVue /> </el-header>
    <el-main class="route-box"> <router-view></router-view> </el-main>
  </el-container>
  </template>
  
  <script setup>
  import HeaderVue from './views/Header.vue';
  import { provide } from 'vue';

  provide("index","5");
  </script>
  
  <style>
    .route-box{
      height: 90vh;
      display: flex;
      justify-content: center;
      /* align-items: start; */
    }
  </style>