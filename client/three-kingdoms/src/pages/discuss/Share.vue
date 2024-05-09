<template>
  <div class="share-box">
    <el-text class="share-text">分享到：</el-text>
    <el-tooltip class="box-item" effect="light" content="QQ" placement="bottom">
      <Icon
        class="share-icon qq"
        icon="ant-design:qq-circle-filled"
        @click="goToQQ"
      />
    </el-tooltip>
    <el-tooltip
      class="box-item"
      effect="light"
      content="QQ空间"
      placement="bottom"
    >
      <Icon class="share-icon qzone" icon="cib:qzone" @click="goToQZone" />
    </el-tooltip>

    <Icon
      class="share-icon wechat"
      icon="ant-design:wechat-filled"
      @mouseenter="showWeChat"
      @mouseleave="hideWeChat"
    />

    <div
      id="qrcode"
      v-show="isHoverWeChat"
      :style="{ top: qrcodeTop + 'px', left: qrcodeLeft + 'px' }"
    >
      <span>微信扫一扫分享</span>
      <div id="code-box"></div>
    </div>
    <el-tooltip
      class="box-item"
      effect="light"
      content="微博"
      placement="bottom"
    >
      <Icon
        class="share-icon weibo"
        icon="ant-design:weibo-circle-outlined"
        @click="goToWeiBo"
      />
    </el-tooltip>
    <el-tooltip
      class="box-item"
      effect="light"
      content="哔哩哔哩"
      placement="bottom"
    >
      <Icon
        class="share-icon bilibili"
        icon="ant-design:bilibili-filled"
        @click="goToBiliBili"
      />
    </el-tooltip>
    <el-tooltip
      class="box-item"
      effect="light"
      content="贴吧"
      placement="bottom"
    >
      <img
        class="share-icon tieba"
        src="../../assets/images/tieba.png"
        alt="贴吧"
        @click="goToTieba"
      />
    </el-tooltip>
  </div>
</template>

<script setup>
import { Icon } from "@iconify/vue";
import { ref, inject, onMounted, nextTick } from "vue";
import QRCode from "qrcodejs2-fix";
import { ElMessage } from "element-plus";
import {updateMsgSharesService} from "@/api/msg.js"

const msgData = inject("msgData");
const shareUrl = location.href;

const shareInfo =
  "他发表了一篇不错的文章：" + msgData.value.title + " 快戳链接来看看吧";
const emit = defineEmits(['updateShares']);
const props = defineProps({
  mid:String
})

//增加分享量
const updateShare = ()=>{
  updateMsgSharesService(props.mid).then(res=>{
    emit("updateShares",res.data);
  })
}

//跳转到qq分享页
const goToQQ = () => {
  navigator.clipboard
    .writeText(shareInfo + "  " + shareUrl)
    .then(() => {
      ElMessage.success("已成功将内容复制到剪贴板");
      updateShare();
    })
    .catch(() => {
      ElMessage.error("内容复制剪贴板失败，请稍后重试");
    });
};
//跳转到qq空间分享页
const goToQZone = () => {
  window.open(
    "https://sns.qzone.qq.com/cgi-bin/qzshare/cgi_qzshare_onekey?url=" +
      encodeURIComponent(shareUrl) +
      "&title=" +
      encodeURIComponent(msgData.value.title) +
      "&desc=" +
      encodeURIComponent(shareInfo)
  );
  updateShare();
};
onMounted(() => {
  // changePosition()
});
//修改微信扫码的盒子位置
const changePosition = () => {
  const qrcode = document.querySelector("#qrcode");
  const wechat = document.querySelector(".wechat");
  const rect = wechat.getBoundingClientRect();
  const x = rect.x - qrcode.offsetWidth / 2;
  const y = rect.top + rect.height + 10;
  qrcodeTop.value = y;
  qrcodeLeft.value = x;
};
const isHoverWeChat = ref(false);
const qrcodeTop = ref(0);
const qrcodeLeft = ref(0);
//显示微信分享二维码
const showWeChat = () => {
  changePosition();
  isHoverWeChat.value = true;
};
//隐藏微信分享二维码
const hideWeChat = () => {
  isHoverWeChat.value = false;
};

//跳转到微博分享页
const goToWeiBo = () => {
  window.open(
    "https://service.weibo.com/share/share.php?url=" +
      encodeURIComponent(shareUrl) +
      "&title=" +
      shareInfo
  );
  updateShare();
};
//跳转到B站分享页
const goToBiliBili = () => {
  goToQQ();
};
//贴吧
const goToTieba = () => {
  window.open(
    "http://tieba.baidu.com/f/commit/share/openShareApi?title=" +
      msgData.value.title +
      "&url=" +
      encodeURIComponent(shareUrl) +
      "&pic="
  );
  updateShare();
};
//微信二维码相关
const formState = ref({
  weixinUrl: location.href,
});

const weChat = ref(null);
//等待dom渲染完毕后执行
nextTick(() => {
  changePosition();
  let codeView = document.querySelector("#code-box");
  codeView.innerHTML = "";
  new QRCode(codeView, {
    text: formState.value.weixinUrl, // 二维码内容
    width: 120, // 二维码图片宽度
    height: 120, // 二维码图片高度
  });
});
</script>

<style>
.share-box {
  height: 2.5rem;
  display: flex;
  flex-direction: row;
  justify-content: start;
  align-content: center;
  flex-wrap: wrap;
}
.share-text {
  font-size: 1rem;
  /* line-height: 3rem; */
}
.share-box .share-icon {
  margin: 0px 0.3rem;
  font-size: 1.8rem;
  cursor: pointer;
}
.tieba {
  width: 1.8rem;
  height: 1.8rem;
}
#qrcode {
  position: fixed;
  background-color: white;
  box-shadow: 1px 1px 3px 3px #eee;
  padding: 10px;
  z-index: 99;
}
#code-box {
  margin-top: 10px;
}
</style>