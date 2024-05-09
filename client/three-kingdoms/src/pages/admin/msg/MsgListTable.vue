
<template>
  <div>
    <div class="card">
      <Toolbar class="mb-4">
        <template #start>
          <Button
            label="批量删除"
            icon="pi pi-trash"
            severity="danger"
            @click="confirmDeleteSelected"
            :disabled="!selectedDatas || !selectedDatas.length"
          />
        </template>
        <template #end>
          <Button
            label="导出"
            icon="pi pi-upload"
            severity="help"
            @click="exportCSV($event)"
          />
        </template>
      </Toolbar>

      <DataTable
        v-model:filters="filters"
        v-model:expandedRows="expandedRows"
        filterDisplay="row"
        :value="datas"
        ref="dt"
        paginator
        :rows="10"
        :rowsPerPageOptions="[5, 10, 20]"
        removableSort
        tableStyle="min-width: 60rem"
        v-model:selection="selectedDatas"
        scrollable
        scrollHeight="29rem"
        :loading="loading"
        paginatorTemplate="RowsPerPageDropdown FirstPageLink PrevPageLink CurrentPageReport NextPageLink LastPageLink"
        currentPageReportTemplate="显示 {first} 到 {last} 条，一共有 {totalRecords} 条帖子信息"
        :globalFilterFields="[
          'mid',
          'title',
          'content',
          'time',
          'uid',
          'userName',
          'userAvatar',
          'visits',
          'likes',
          'type',
          'share',
          'status',
        ]"
      >
        <template #paginatorstart>
          <Button
            type="button"
            icon="pi pi-refresh"
            text
            label="刷新"
            @click="refresh"
          />
        </template>
        <template #header>
          <div class="flex justify-content-between">
            <h4 class="m-0">管理帖子</h4>
            <IconField iconPosition="left">
              <InputIcon>
                <i class="pi pi-search" />
              </InputIcon>
              <InputText
                v-model="filters['global'].value"
                placeholder="请输入关键字搜索"
              />
            </IconField>
          </div>
        </template>
        <template #empty> 暂无数据 </template>
        <template #loading> 数据加载中，请稍后 </template>
        <Column expander style="width: 3rem" />
        <Column selectionMode="multiple" headerStyle="width: 3rem"></Column>
        <Column field="mid" header="帖子ID" sortable style="min-width: 8rem">
          <template #body="{ data }">
            {{ data.mid }}
          </template>
          <template #filter="{ filterModel, filterCallback }">
            <InputText
              v-model="filterModel.value"
              type="text"
              @input="filterCallback()"
              class="p-column-filter"
              placeholder="按id查找"
            />
          </template>
        </Column>
        <Column field="title" header="标题" sortable style="min-width: 8rem">
          <template #body="{ data }">
            {{ data.title }}
          </template>
          <template #filter="{ filterModel, filterCallback }">
            <InputText
              v-model="filterModel.value"
              type="text"
              @input="filterCallback()"
              class="p-column-filter"
              placeholder="按标题查找"
            /> </template
        ></Column>
        <Column
          field="content"
          header="帖子内容"
          sortable
          style="min-width: 25rem; max-width: 30rem"
        >
          <template #body="{ data }">
            <Inplace :closable="true">
              <template #closeicon>
                <span class="pi pi-eye-slash"></span>
              </template>
              <template #display>
                <el-text line-clamp="2"> 点击查看全部内容 </el-text>
              </template>
              <template #content>
                <div v-html="data.content ? data.content : '暂无数据'"></div>
              </template>
            </Inplace>
          </template>
          <template #filter="{ filterModel, filterCallback }">
            <InputText
              v-model="filterModel.value"
              type="text"
              @input="filterCallback()"
              class="p-column-filter"
              placeholder="按帖子内容查找"
            />
          </template>
        </Column>
        <Column field="time" header="发帖时间" sortable style="min-width: 8rem">
          <template #filter="{ filterModel, filterCallback }">
            <InputText
              v-model="filterModel.value"
              type="text"
              @input="filterCallback()"
              class="p-column-filter"
              placeholder="按发帖时间查找"
            />
          </template>
        </Column>
        <Column
          field="uid"
          header="用户id"
          sortable
          style="min-width: 8rem; max-width: 15rem"
        >
          <template #body="{ data }">
            {{ data.uid }}
          </template>
          <template #filter="{ filterModel, filterCallback }">
            <InputText
              v-model="filterModel.value"
              type="text"
              @input="filterCallback()"
              class="p-column-filter"
              placeholder="按用户id查找"
            />
          </template>
        </Column>
        <Column
          field="userName"
          header="发帖用户名"
          sortable
          style="min-width: 8rem; max-width: 15rem"
        >
          <template #body="{ data }">
            {{ data.userName }}
          </template>
          <template #filter="{ filterModel, filterCallback }">
            <InputText
              v-model="filterModel.value"
              type="text"
              @input="filterCallback()"
              class="p-column-filter"
              placeholder="按用户名查找"
            />
          </template>
        </Column>
        <Column
          field="userAvatar"
          header="发帖用户头像"
          sortable
          style="min-width: 8rem"
        >
          <template #body="{ data }">
            <el-avatar
              :size="40"
              :src="
                data.userAvatar
                  ? data.userAvatar
                  : 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png'
              "
            />
          </template>
          <template #filter="{ filterModel, filterCallback }">
            <InputText
              v-model="filterModel.value"
              type="text"
              @input="filterCallback()"
              class="p-column-filter"
              placeholder="按用户头像查找"
            />
          </template>
        </Column>
        <Column field="visits" header="访问量" sortable style="min-width: 6rem">
          <template #body="{ data }">
            {{ data.visits }}
          </template>
          <template #filter="{ filterModel, filterCallback }">
            <InputText
              v-model="filterModel.value"
              type="text"
              @input="filterCallback()"
              class="p-column-filter"
              placeholder="按访问量查找"
            />
          </template>
        </Column>
        <Column field="likes" header="点赞量" sortable style="min-width: 6rem">
          <template #body="{ data }">
            {{ data.likes }}
          </template>
          <template #filter="{ filterModel, filterCallback }">
            <InputText
              v-model="filterModel.value"
              type="text"
              @input="filterCallback()"
              class="p-column-filter"
              placeholder="按点赞量查找"
            />
          </template>
        </Column>
        <Column field="share" header="分享量" sortable style="min-width: 6rem">
          <template #body="{ data }">
            {{ data.share }}
          </template>
          <template #filter="{ filterModel, filterCallback }">
            <InputText
              v-model="filterModel.value"
              type="text"
              @input="filterCallback()"
              class="p-column-filter"
              placeholder="按分享量查找"
            />
          </template>
        </Column>
        <Column
          field="statusLabel"
          header="审核状态"
          style="min-width: 6rem"
          :showFilterMenu="false"
        >
          <template #body="{ data }">
            <el-tag :type="getStatusTag(data.status)[0].type" effect="dark">
              {{ data.statusLabel }}
            </el-tag>
          </template>
          <template #filter="{ filterModel, filterCallback }">
            <div class="dropdown-box">
              <Dropdown
                v-model="filterModel.value"
                @change="filterCallback()"
                :options="statusLabels"
                placeholder="请选择状态"
                class="p-column-filter"
                style="min-width: 6rem"
              >
                <template #option="slotProps">
                  <el-tag
                    :type="getStatusByLabel(slotProps.option)[0].type"
                    effect="dark"
                  >
                    {{ slotProps.option }}
                  </el-tag>
                </template>
              </Dropdown>
            </div>
          </template>
        </Column>
        <Column :exportable="false" header="操作" style="min-width: 8rem">
          <template #body="slotProps">
            <Button
              icon="pi pi-pencil"
              outlined
              rounded
              class="mr-2"
              @click="editData(slotProps.data)"
            />
            <Button
              icon="pi pi-trash"
              outlined
              rounded
              severity="danger"
              @click="confirmDeleteData(slotProps.data)"
            />
          </template>
        </Column>
        <template #expansion="slotProps">
          <div class="p-3">
            <h3 style="text-align: left;">{{ slotProps.data.mid }}{{ slotProps.data.title }}的评论</h3>
            <DataTable
              :value="slotProps.data.commentList"
              v-model:filters="filters2"
              filterDisplay="row"
              removableSort
              paginator
              :rows="10"
              :rowsPerPageOptions="[5, 10, 20]"
              :globalFilterFields="['cid', 'content', 'uname']"
            >
              <Column field="cid" header="评论Id" sortable  style="max-width: 4rem;width: 4rem;">
                <template #filter="{ filterModel, filterCallback }">
                  <InputText
                    v-model="filterModel.value"
                    type="text"
                    @input="filterCallback()"
                    class="p-column-filter"
                    placeholder="按id查找"
                  />
                </template>
              </Column>
              <Column field="content" header="评论内容" sortable style="max-width: 12rem;width: 8rem;">
                <template #filter="{ filterModel, filterCallback }">
                  <InputText
                    v-model="filterModel.value"
                    type="text"
                    @input="filterCallback()"
                    class="p-column-filter"
                    placeholder="按内容查找"
                  />
                </template>
              </Column>
              <Column field="uname" header="评论用户名" sortable style="max-width: 4rem;width: 4rem;">
                <template #filter="{ filterModel, filterCallback }">
                  <InputText
                    v-model="filterModel.value"
                    type="text"
                    @input="filterCallback()"
                    class="p-column-filter"
                    placeholder="按用户名查找"
                  /> </template
                >f
              </Column>
              <Column field="likes" header="点赞量" sortable style="max-width: 3rem;width: 3rem;"> </Column>
              <Column field="createTime" header="发表时间" sortable style="max-width: 4rem;width: 4rem;"></Column>
              <Column :exportable="false" header="操作" style="max-width: 4rem;width: 4rem;">
                <template #body="slotProps">
                  <Button
                    icon="pi pi-trash"
                    outlined
                    rounded
                    severity="danger"
                    @click="confirmDeleteCommentData(slotProps.data)"
                  />
                </template>
              </Column>
            </DataTable>
          </div>
        </template>
      </DataTable>
    </div>
    <!-- 审核帖子 -->
    <Dialog
      v-model:visible="dialog"
      :style="{ width: '800px' }"
      header="帖子信息"
      :modal="true"
      class="p-fluid"
    >
      <div class="field">
        <label for="title">帖子名</label>
        <InputText id="title" v-model="data.title" disabled />
      </div>
      <div class="field">
        <label for="content">帖子内容</label>
        <Editor v-model="data.content" readonly editorStyle="height: 300px">
          <template v-slot:toolbar>
            <span class="ql-formats">
              <button v-tooltip.bottom="'Bold'" class="ql-bold"></button>
              <button v-tooltip.bottom="'Italic'" class="ql-italic"></button>
            </span>
          </template>
        </Editor>
      </div>
      <!-- 审核 -->
      <div class="field">
        <label for="status">审核状态</label>
        <br />
        <el-radio-group
          v-model.trim="data.status"
          @change="changeStatus"
          size="large"
        >
          <el-radio-button :label="1" :value="1">通过</el-radio-button>
          <el-radio-button :label="-1" :value="-1">打回</el-radio-button>
        </el-radio-group>
      </div>
      <div class="field" v-if="data.status == -1">
        <label for="reason">原因</label>
        <InputText id="reason" v-model="data.reason" />
      </div>

      <template #footer>
        <Button label="取消" icon="pi pi-times" text @click="hideDialog" />
        <Button
          label="保存"
          :disabled="!isSave"
          icon="pi pi-check"
          text
          @click="saveData"
        />
      </template>
    </Dialog>

    <!-- 删除单个帖子的对话框 -->
    <Dialog
      v-model:visible="deleteDataDialog"
      :style="{ width: '450px' }"
      header="确认删除"
      :modal="true"
    >
      <div class="confirmation-content">
        <i class="pi pi-exclamation-triangle mr-3" style="font-size: 2rem" />
        <span v-if="data"
          >你确定要删除 <b>{{ data.title }} </b>这个帖子?</span
        >
      </div>
      <template #footer>
        <Button
          label="No"
          icon="pi pi-times"
          text
          @click="deleteDataDialog = false"
        />
        <Button label="Yes" icon="pi pi-check" text @click="deleteData" />
      </template>
    </Dialog>

    <!-- 删除多个帖子的对话框 -->
    <Dialog
      v-model:visible="deleteDatasDialog"
      :style="{ width: '450px' }"
      header="确认删除"
      :modal="true"
    >
      <div class="confirmation-content">
        <i class="pi pi-exclamation-triangle mr-3" style="font-size: 2rem" />
        <span>你确定要删除选择的帖子?</span>
      </div>
      <template #footer>
        <Button
          label="No"
          icon="pi pi-times"
          text
          @click="deleteDatasDialog = false"
        />
        <Button
          label="Yes"
          icon="pi pi-check"
          text
          @click="deleteSelectedDatas"
        />
      </template>
    </Dialog>

    <!-- 删除单个评论的对话框 -->
    <Dialog
      v-model:visible="deleteCommentDialog"
      :style="{ width: '450px' }"
      header="确认删除"
      :modal="true"
    >
      <div class="confirmation-content">
        <i class="pi pi-exclamation-triangle mr-3" style="font-size: 2rem" />
        <span v-if="commentData"
          >你确定要删除 <b>{{ commentData.content }} </b>这个帖子?</span
        >
      </div>
      <template #footer>
        <Button
          label="不"
          icon="pi pi-times"
          text
          @click="deleteCommentDialog = false"
        />
        <Button label="是的" icon="pi pi-check" text @click="deleteComment" />
      </template>
    </Dialog>
  </div>
</template>

<script setup>
import Column from "primevue/column";
import DataTable from "primevue/datatable";
import IconField from "primevue/iconfield";
import InputIcon from "primevue/inputicon";
import Toolbar from "primevue/toolbar";
import Dialog from "primevue/dialog";
import { ref, onMounted } from "vue";
import { FilterMatchMode } from "primevue/api";
import "@/assets/dataTable.css";
import { ElMessage } from "element-plus";
import {
  deleteMsgService,
  deleteMsgsService,
  getMsgsNoPageService,
  updateMsgStatusService,
} from "@/api/msg";

import { delCommentService } from "@/api/comment";
import { sendUserMessageService } from "@/api/user";

const datas = ref([]);
const data = ref();
const commentData = ref();
const expandedRows = ref(null);
const loading = ref(false);
const isSave = ref(false);
//筛选
const filters = ref({
  global: { value: null, matchMode: FilterMatchMode.CONTAINS },
  mid: { value: null, matchMode: FilterMatchMode.CONTAINS },
  title: { value: null, matchMode: FilterMatchMode.CONTAINS },
  content: { value: null, matchMode: FilterMatchMode.CONTAINS },
  time: { value: null, matchMode: FilterMatchMode.CONTAINS },
  uid: { value: null, matchMode: FilterMatchMode.CONTAINS },
  userName: { value: null, matchMode: FilterMatchMode.CONTAINS },
  userAvatar: { value: null, matchMode: FilterMatchMode.CONTAINS },
  visits: { value: null, matchMode: FilterMatchMode.CONTAINS },
  likes: { value: null, matchMode: FilterMatchMode.CONTAINS },
  type: { value: null, matchMode: FilterMatchMode.CONTAINS },
  share: { value: null, matchMode: FilterMatchMode.CONTAINS },
  statusLabel: { value: null, matchMode: FilterMatchMode.EQUALS },
});

const filters2 = ref({
  cid: { value: null, matchMode: FilterMatchMode.CONTAINS },
  uname: { value: null, matchMode: FilterMatchMode.CONTAINS },
  content: { value: null, matchMode: FilterMatchMode.CONTAINS },
});

const statusOptions = [
  { label: "未审核", type: "info", status: 0 },
  { label: "已通过", type: "success", status: 1 },
  { label: "已打回", type: "danger", status: -1 },
];

const statusLabels = ["未审核", "已通过", "已打回"];

const getStatusByLabel = (label) => {
  return statusOptions.filter((data) => {
    return data.label == label;
  });
};

// 返回对应状态的标签
const getStatusTag = (status) => {
  return statusOptions.filter((tag) => {
    return tag.status == status;
  });
};

//获取帖子内容
const getMsgDatas = () => {
  getMsgsNoPageService().then((res) => {
    let newData = res.data;
    newData = newData.map((obj) => {
      return { ...obj, statusLabel: getStatusTag(obj.status)[0].label };
    });
    datas.value = newData;
    loading.value = false;
  });
};

onMounted(() => {
  getMsgDatas();
});

const dt = ref();
const dialog = ref(false);
const deleteDataDialog = ref(false);
const deleteDatasDialog = ref(false);
const deleteCommentDialog = ref(false);
const selectedDatas = ref();
const submitted = ref(false);

//隐藏对话框
const hideDialog = () => {
  dialog.value = false;
  submitted.value = false;
  isSave.value = false;
};

//更改审核状态
const changeStatus = (value) => {
  if (!isSave.value) {
    isSave.value = true;
  }
};

//提交审核
const saveData = () => {
  submitted.value = true;
  if(data.value.status == -1 && !data.value.reason){
    ElMessage.error('请填写打回原因')
    return;
  }
  if (isSave.value) {
    let params = {
      mid: data.value.mid,
      status: data.value.status,
      uid:data.value.uid,
      title:data.value.title,
      reason:data.value.reason
    };
    updateMsgStatusService(params).then((res) => {
      if (res.code.endsWith("OK")) {
        ElMessage.success(res.msg);
        getMsgDatas();
        //发送审核通过消息
        if(params.status == 1){
          let content = '您的帖子<span style="color:blue;">【' +
                params.title +
                '】</span>审核已通过，快去分享给小伙伴吧~'
          sendSysMsg(params.uid,content,'帖子审核通过通知')
        }
        //发送审核不通过消息
        if(params.status == -1){
          let content = '您的帖子<span style="color:blue;">【' +
            params.title +
                '】</span>未通过审核，原因：'+params.reason
          sendSysMsg(params.uid,content,'帖子审核结果通知')
        }
        isSave.value = false;
      } else {
        ElMessage.error(res.msg);
      }
    });
  }
  dialog.value = false;
  data.value = {};
};

//编辑数据
const editData = (prod) => {
  data.value = { ...prod };
  dialog.value = true;
};
//确认删除单个帖子对话框
const confirmDeleteData = (prod) => {
  data.value = prod;
  deleteDataDialog.value = true;
};

//删除单个帖子数据
const deleteData = () => {
  console.log("选中的数据", data.value.mid);
  deleteMsgService(data.value.mid).then((res) => {
    ElMessage.success(res.msg);
    getMsgDatas();
  });
  deleteDataDialog.value = false;
  data.value = {};
};

//确认删除单个评论对话框
const confirmDeleteCommentData = (prod) => {
  commentData.value = prod;
  console.log(prod);
  deleteCommentDialog.value = true;
};

//删除单个评论数据
const deleteComment = () => {
  console.log("选中的数据", commentData.value.cid);
  delCommentService(commentData.value.cid).then((res) => {
    ElMessage.success(res.msg);
    getMsgDatas();
  });
  deleteCommentDialog.value = false;
  commentData.value = {};
};

//确认删除多个选中的帖子对话框
const confirmDeleteSelected = () => {
  deleteDatasDialog.value = true;
};
//删除多个选中的帖子
const deleteSelectedDatas = () => {
  //提取对象数组内的属性，组成新数组
  let mids = selectedDatas.value.map(({ mid }) => mid);
  deleteMsgsService(mids).then((res) => {
    ElMessage.success(res.msg);
    getMsgDatas();
  });
  deleteDatasDialog.value = false;
  selectedDatas.value = null;
};

//发送系统消息
const sendSysMsg = (uid, content, title)=>{
  let params = {
    uid,
    title,
    content,
    type: "SYSTEM",
  };
  sendUserMessageService(params).then(res=>{
    if (res.code.endsWith("OK")) {
      ElMessage.success('已向用户发送反馈');
    } else {
      ElMessage.error('尝试向用户发送反馈失败');
    }
  })
}


const refresh = () => {
  loading.value = true;
  setTimeout(() => {
    getMsgDatas();
  }, 1000);
};

const exportCSV = () => {
  dt.value.exportCSV();
};
</script>
<style scoped>
.p-button {
  padding: 0.3rem 0.5rem;
  height: 2rem;
  margin-left: 1rem;
}

.p-button .p-button-label {
  margin-left: 0.3rem;
}

.dropdown-box :deep(.p-dropdown-panel .p-dropdown-items .p-dropdown-item) {
  justify-content: center;
}
</style>
