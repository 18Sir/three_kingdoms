<template>
  <el-container>
    <el-header> <HeaderVue /> </el-header>
    <el-main> <router-view></router-view> </el-main>
  </el-container>
  </template>
  
  <script setup>
  import HeaderVue from '@/views/header/Header.vue';
  import { provide } from 'vue';

  provide("index","/event");
  </script>