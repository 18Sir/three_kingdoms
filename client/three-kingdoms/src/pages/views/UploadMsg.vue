<template>
  <div class="card flex justify-content-center flex-wrap gap-3">
    <Button
      type="button"
      label="我要发帖"
      icon="pi pi-pencil"
      severity="info"
      raised
      @click="openNew"
    />
    <Dialog
      v-model:visible="dialog"
      :style="{ width: '800px' }"
      header="填写帖子信息"
      :modal="true"
      class="p-fluid"
    >
      <div class="field">
        <label for="title">标题</label>
        <InputText
          id="title"
          v-model.trim="data.title"
          required="true"
          autofocus
          :class="{ 'p-invalid': submitted && !data.title }"
        />
        <small class="p-error" v-if="submitted && !data.title"
          >标题名需要填写</small
        >
      </div>

      <div class="field">
        <label for="content">帖子内容</label>
        <Editor
          id="content"
          aria-required="true"
          v-model="data.content"
          :class="{ 'p-invalid': submitted && !data.content }"
          editorStyle="height: 320px"
        />
        <!-- <Textarea
          id="content"
          v-model="data.content"
          required="true"
          rows="10"
          cols="30"
          :class="{ 'p-invalid': submitted && !data.content }"
        /> -->
        <small class="p-error" v-if="submitted && !data.content"
          >帖子内容需要填写</small
        >
      </div>
      <div class="field">
        <label for="type">类型</label>
        <SelectButton
          v-model="data.type"
          :options="typeOptions"
          aria-labelledby="basic"
        />
      </div>

      <template #footer>
        <Button label="取消" icon="pi pi-times" text @click="hideDialog" />
        <Button label="保存" icon="pi pi-check" text @click="saveData" />
      </template>
    </Dialog>
  </div>
</template>

<script setup>
import Button from "primevue/button";
import { onMounted, ref } from "vue";
import { uploadMsgService } from "@/api/msg";
import { ElMessage } from "element-plus";
import SelectButton from "primevue/selectbutton";
import "@/assets/dataTable.css";
import { getAllMsgTypeService } from "@/api/msgType";
import Editor from "primevue/editor";

const dialog = ref(false);
const submitted = ref(false);
const data = ref({
  title: null,
  content: null,
  type: 0,
});
const typeOptions = ref();
const typeData = ref();

onMounted(() => {
  getAllMsgType();
});

//获取所有类型
const getAllMsgType = () => {
  getAllMsgTypeService().then((res) => {
    console.log(res.data);
    typeData.value = res.data;
    data.value.type = res.data[0].tname;
    typeOptions.value = typeData.value.map((item) => item.tname);
  });
};

//创建新数据
const openNew = () => {
  // data.value = {};
  submitted.value = false;
  dialog.value = true;
};

//隐藏对话框
const hideDialog = () => {
  dialog.value = false;
  submitted.value = false;
};

//提交新增数据
const saveData = () => {
  submitted.value = true;
  let type = typeData.value.filter((res) => res.tname == data.value.type);
  data.value.type = type[0].tid;
  data.value.content = imgFormat(data.value.content)
  if (data.value.title.trim()) {
    uploadMsgService(data.value).then((res) => {
      ElMessage.success(res.msg);
    });
  }
  dialog.value = false;
  data.value = {};
};

// 更改图片格式
const imgFormat = (text)=>{
  let re = new RegExp("<img", "g");
  if(text.includes('<img')){
    text = text.replace(re,'<Image preview')
  }
  return text;
}

</script>

<style>
.card {
  position: fixed;
  top: 20%;
  left: 19%;
  margin: 1rem 0;
}

.p-button {
  padding: 0.5rem 1rem;
}
.p-button .p-button-icon-left {
  margin-right: 0.5rem;
}
</style>