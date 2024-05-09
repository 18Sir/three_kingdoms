
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
        currentPageReportTemplate="显示 {first} 到 {last} 条，一共有 {totalRecords} 条举报信息"
        :globalFilterFields="[
          'jid',
          'cid',
          'uname',
          'beUname',
          'title',
          'content',
          'msgContent',
          'reason',
          'detail',
          'result',
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
            <h4 class="m-0">管理被举报的评论</h4>
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
        <Column selectionMode="multiple" headerStyle="width: 3rem"></Column>
        <Column field="jid" header="举报ID" sortable style="min-width: 8rem">
          <template #body="{ data }">
            {{ data.jid }}
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
        <Column
          field="uname"
          header="举报用户名"
          sortable
          style="min-width: 8rem"
        >
          <template #body="{ data }">
            {{ data.user.uname }}
          </template>
          <template #filter="{ filterModel, filterCallback }">
            <InputText
              v-model="filterModel.value"
              type="text"
              @input="filterCallback()"
              class="p-column-filter"
              placeholder="按举报用户名查找"
            /> </template
        ></Column>
        <Column
          field="beUname"
          header="被举报用户名"
          sortable
          style="min-width: 8rem"
        >
          <template #body="{ data }">
            {{ data.beUser.uname }}
          </template>
          <template #filter="{ filterModel, filterCallback }">
            <InputText
              v-model="filterModel.value"
              type="text"
              @input="filterCallback()"
              class="p-column-filter"
              placeholder="按被举报用户名查找"
            />
          </template>
        </Column>
        <Column field="cid" header="评论id" sortable style="min-width: 10rem">
          <template #body="{ data }">
            {{ data.comment.cid }}
          </template>
          <template #filter="{ filterModel, filterCallback }">
            <InputText
              v-model="filterModel.value"
              type="text"
              @input="filterCallback()"
              class="p-column-filter"
              placeholder="按评论id查找"
            />
          </template>
        </Column>
        <Column
          field="content"
          header="评论内容"
          sortable
          style="min-width: 25rem; max-width: 30rem"
        >
          <template #body="{ data }">
            <div v-html="data.comment.content"></div>
          </template>
          <template #filter="{ filterModel, filterCallback }">
            <InputText
              v-model="filterModel.value"
              type="text"
              @input="filterCallback()"
              class="p-column-filter"
              placeholder="按评论内容查找"
            />
          </template>
        </Column>
        <Column
          field="title"
          header="所属帖标题"
          sortable
          style="min-width: 10rem"
        >
          <template #body="{ data }">
            <div v-html="data.msg.title"></div>
          </template>
          <template #filter="{ filterModel, filterCallback }">
            <InputText
              v-model="filterModel.value"
              type="text"
              @input="filterCallback()"
              class="p-column-filter"
              placeholder="按帖标题查找"
            />
          </template>
        </Column>
        <Column
          field="msgContent"
          header="所属帖内容"
          sortable
          style="min-width: 25rem; max-width: 30rem"
        >
          <template #body="{ data }">
            <div v-html="data.msg.content"></div>
          </template>
          <template #filter="{ filterModel, filterCallback }">
            <InputText
              v-model="filterModel.value"
              type="text"
              @input="filterCallback()"
              class="p-column-filter"
              placeholder="按帖内容查找"
            />
          </template>
        </Column>
        <Column
          field="reason"
          header="举报理由"
          sortable
          style="min-width: 8rem; max-width: 15rem"
        >
          <template #body="{ data }">
            {{ data.reason }}
          </template>
          <template #filter="{ filterModel, filterCallback }">
            <InputText
              v-model="filterModel.value"
              type="text"
              @input="filterCallback()"
              class="p-column-filter"
              placeholder="按举报理由查找"
            />
          </template>
        </Column>
        <Column
          field="result"
          header="处理结果"
          style="min-width: 6rem; max-width: 10rem"
          :showFilterMenu="false"
        >
          <template #body="{ data }">
            <el-tag
              :type="getResultsByLabel(data.result)[0].type"
              effect="dark"
            >
              {{ data.result }}
            </el-tag>
          </template>
          <template #filter="{ filterModel, filterCallback }">
            <div class="dropdown-box">
              <Dropdown
                v-model="filterModel.value"
                @change="filterCallback()"
                :options="resultLabels"
                placeholder="请选择处理结果"
                class="p-column-filter"
                style="min-width: 6rem"
              >
                <template #option="slotProps">
                  <el-tag
                    :type="getResultsByLabel(slotProps.option)[0].type"
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
      </DataTable>
    </div>
    <!-- 审核评论 -->
    <Dialog
      v-model:visible="dialog"
      :style="{ width: '800px' }"
      header="举报信息"
      :modal="true"
      class="p-fluid"
    >
      <div class="field">
        <label for="title">所属帖子标题</label>
        <InputText id="title" v-model="data.msg.title" disabled />
      </div>
      <div class="field">
        <label for="content">所属帖子内容</label>
        <Editor v-model="data.msg.content" readonly editorStyle="height: 300px">
          <template v-slot:toolbar>
            <span class="ql-formats">
              <button v-tooltip.bottom="'Bold'" class="ql-bold"></button>
              <button v-tooltip.bottom="'Italic'" class="ql-italic"></button>
            </span>
          </template>
        </Editor>
      </div>
      <div class="field">
        <label for="title">评论内容</label>
        <InputText id="title" v-model="data.comment.content" disabled />
      </div>
      <div class="field">
        <label for="reason">理由</label>
        <InputText id="reason" v-model="data.reason" disabled />
      </div>
      <!-- 审核 -->
      <div class="field">
        <label for="status">处理结果</label>
        <br />
        <el-radio-group
          v-model.trim="data.result"
          @change="changeStatus"
          size="large"
          :disabled="data.result.includes('删除') && !isSave"
        >
          <el-radio-button value="不违规">不违规</el-radio-button>
          <el-radio-button value="删除评论">删除</el-radio-button>
          <el-radio-button value="删除评论并封禁用户"
            >删除评论并封禁用户</el-radio-button
          >
        </el-radio-group>
        <div class="field" v-if="data.result == '删除评论并封禁用户' && isSave">
          <label for="blockTime">时长</label>
          <SelectButton
            v-model="data.blockTime"
            :options="timeOptions"
            optionLabel="name"
            aria-labelledby="basic"
          />
        </div>
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

    <!-- 删除单个举报信息的对话框 -->
    <Dialog
      v-model:visible="deleteDataDialog"
      :style="{ width: '450px' }"
      header="确认删除"
      :modal="true"
    >
      <div class="confirmation-content">
        <i class="pi pi-exclamation-triangle mr-3" style="font-size: 2rem" />
        <span v-if="data">你确定要删除这个举报信息?</span>
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

    <!-- 删除多个举报信息的对话框 -->
    <Dialog
      v-model:visible="deleteDatasDialog"
      :style="{ width: '450px' }"
      header="确认删除"
      :modal="true"
    >
      <div class="confirmation-content">
        <i class="pi pi-exclamation-triangle mr-3" style="font-size: 2rem" />
        <span>你确定要删除选择的举报信息?</span>
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
  getAlljuBaoCommentService,
  deletejuBaoCommentsService,
  deletejuBaoCommentService,
  updatejuBaoCommentService,
} from "@/api/juBao";
import { blockUserService, sendUserMessageService } from "@/api/user";

const datas = ref([]);
const data = ref();
const loading = ref(false);
const isSave = ref(false);
const showJuBao = ref(true);
const showDel = ref(true);
//筛选
const filters = ref({
  global: { value: null, matchMode: FilterMatchMode.CONTAINS },
  jid: { value: null, matchMode: FilterMatchMode.CONTAINS },
  cid: { value: null, matchMode: FilterMatchMode.CONTAINS },
  uname: { value: null, matchMode: FilterMatchMode.CONTAINS },
  beUname: { value: null, matchMode: FilterMatchMode.CONTAINS },
  title: { value: null, matchMode: FilterMatchMode.CONTAINS },
  content: { value: null, matchMode: FilterMatchMode.CONTAINS },
  msgContent: { value: null, matchMode: FilterMatchMode.CONTAINS },
  reason: { value: null, matchMode: FilterMatchMode.CONTAINS },
  detail: { value: null, matchMode: FilterMatchMode.CONTAINS },
  result: { value: null, matchMode: FilterMatchMode.CONTAINS },
});

const resultsOptions = [
  { label: "未处理", type: "info", status: -1 },
  { label: "不违规", type: "success", status: 0 },
  { label: "删除评论", type: "warning", status: 1 },
  { label: "删除评论并封禁用户", type: "danger", status: 2 },
];

const resultLabels = ["未处理", "不违规", "删除评论", "删除评论并封禁用户"];

//封禁时长
const timeOptions = ref([
  { name: "10分钟", value: 60 * 10 },
  { name: "30分钟", value: 60 * 30 },
  { name: "1小时", value: 60 * 60 },
  { name: "1天", value: 60 * 60 * 24 },
  { name: "7天", value: 60 * 60 * 24 * 7 },
  { name: "30天", value: 60 * 60 * 24 * 30 },
]);

const getResultsByLabel = (label) => {
  return resultsOptions.filter((data) => {
    return data.label.startsWith(label);
  });
};

// 返回对应状态的标签
const getResultsTag = (status) => {
  return resultsOptions.filter((tag) => {
    return tag.status == status;
  });
};

//获取帖审核内容
const getCommentDatas = () => {
  getAlljuBaoCommentService().then((res) => {
    datas.value = res.data;
    loading.value = false;
  });
};

onMounted(() => {
  getCommentDatas();
});

const dt = ref();
const dialog = ref(false);
const deleteDataDialog = ref(false);
const deleteDatasDialog = ref(false);
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
  let params = data.value;
  let blockParams = {};
  if (data.value.result.includes("封禁")) {
    if (!data.value.blockTime) {
      ElMessage.error("封禁时间未填写");
      return;
    }

    blockParams.time = data.value.blockTime.value;
    blockParams.reason =
      '你好，你在帖子<span style="color:blue;">【' +
      data.value.msg.title +
      "】</span>下发送的评论【" +data.value.comment.content+"】，经举报涉嫌违规，理由："+
      data.value.reason;
    blockParams.uid = data.value.beUid;
  }
  if (isSave.value) {
    updatejuBaoCommentService(params).then((res) => {
      if (res.code.endsWith("OK")) {
        ElMessage.success(res.msg);
        //如果处理结果是封禁则再执行一遍封禁用户请求
        if (params.result.includes("封禁")) {
          blockUserService(blockParams).then((res) => {
            if (res.code.endsWith("OK")) {
              //封禁成功后向举报用户反馈
              let content =
                '你好，你在帖子<span style="color:blue;">【' +
                params.msg.title +
                "】</span>,下举报的评论【"+params.comment.content+"】，理由：" +
                params.reason +
                "，经核实确认违规，该评论现已删除，并对帖主进行封禁" +
                params.blockTime.name +
                "的处罚，感谢你对社区做出的贡献";
              sendSysMsg(params.uid, content, "举报处理结果通知");
              ElMessage.success(res.msg);
            } else {
              ElMessage.error(res.msg);
            }
          });
        }
        getCommentDatas();
        isSave.value = false;
      } else {
        ElMessage.error(res.msg);
      }
    });
  }
  dialog.value = false;
  data.value = {};
};

//向用户发送系统信息
const sendSysMsg = (uid, content, title) => {
  let params = {
    uid,
    title,
    content,
    type: "SYSTEM",
  };
  sendUserMessageService(params).then((res) => {
    if (res.code.endsWith("OK")) {
      ElMessage.success("已向举报用户发送反馈");
    } else {
      ElMessage.error("尝试向举报用户发送反馈失败");
    }
  });
};

//编辑数据
const editData = (prod) => {
  data.value = { ...prod };
  dialog.value = true;
};

//确认删除单个评论对话框
const confirmDeleteData = (prod) => {
  data.value = prod;
  deleteDataDialog.value = true;
};

//删除单个事件数据
const deleteData = () => {
  console.log("选中的数据", data.value.jid);
  deletejuBaoCommentService(data.value.jid).then((res) => {
    ElMessage.success(res.msg);
    getCommentDatas();
  });
  deleteDataDialog.value = false;
  data.value = {};
};
//确认删除多个选中的举报评论对话框
const confirmDeleteSelected = () => {
  deleteDatasDialog.value = true;
};
//删除多个选中的举报评论
const deleteSelectedDatas = () => {
  //提取对象数组内的属性，组成新数组
  let jids = selectedDatas.value.map(({ jid }) => jid);
  deletejuBaoCommentsService(jids).then((res) => {
    ElMessage.success(res.msg);
    getCommentDatas();
  });
  deleteDatasDialog.value = false;
  selectedDatas.value = null;
};

const refresh = () => {
  loading.value = true;
  setTimeout(() => {
    getCommentDatas();
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
    