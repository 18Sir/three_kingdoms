<template>
  <div class="card" style="position:fixed;top: 6%;left: 5%;z-index: 10;">
    <el-breadcrumb class="breadcrumb" separator="/">
      <transition-group name="breadcrumb">
        <el-breadcrumb-item
          v-for="(item, index) in breadcrumbData"
          :key="item.path"
        >
          <!-- 不可以点击项 -->
          <span
            v-if="index === breadcrumbData.length - 1"
            class="no-redirect"
            >{{ item.meta.title }}</span
          >
          <!-- 可以点击项 -->
          <a v-else class="redirect" @click.prevent="onLinkClick(item)">{{
            item.meta.title
          }}</a>
        </el-breadcrumb-item>
      </transition-group>
    </el-breadcrumb>
  </div>
</template>

<script setup>
import { ref, watch } from "vue";
import { useRoute, useRouter } from "vue-router";

const route = useRoute();
const router = useRouter();
// 生成数组数据
const breadcrumbData = ref([]);
const getBreadcrumbData = () => {
  breadcrumbData.value = route.matched.filter((item) => {
    return item.meta && item.meta.title;
  });
};
// 监听路由发生改变时触发
watch(
  route,
  () => {
    getBreadcrumbData();
  },
  {
    immediate: true,
  }
);
// 处理点击事件
const onLinkClick = (item) => {
  router.push({ path: item.path });
};
</script>

<style scoped>
.breadcrumb {
  display: flex;
  font-size: 14px;
  line-height: 50px;
  margin-left: 8px;
}
.no-redirect {
  color: #97a8be;
  cursor: text;
}
.redirect {
  color: #666;
  font-weight: 600;
}
.redirect:hover {
  color: #00a0d8;
}
</style>
