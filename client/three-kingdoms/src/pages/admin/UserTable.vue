
<template>
  <div>
    <div class="card">
      <Toolbar class="mb-4">
        <!-- <template #start>
          <Button
            label="添加数据"
            icon="pi pi-plus"
            severity="success"
            class="mr-2"
            @click="openNew"
          />
          <Button
            label="批量删除"
            icon="pi pi-trash"
            severity="danger"
            @click="confirmDeleteSelected"
            :disabled="!selectedDatas || !selectedDatas.length"
          />
        </template> -->

        <template #end>
          <Button
            label="导出"
            icon="pi pi-upload"
            severity="help"
            @click="exportCSV($Addr)"
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
        currentPageReportTemplate="显示 {first} 到 {last} 条，一共有 {totalRecords} 条用户信息"
        :globalFilterFields="['uid', 'uname', 'email', 'statusLabel']"
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
            <h4 class="m-0">管理用户信息</h4>
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
        <Column field="uid" header="用户ID" sortable style="min-width: 8rem">
          <template #body="{ data }">
            {{ data.uid }}
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
        <Column field="uname" header="用户名" sortable style="min-width: 8rem">
          <template #body="{ data }">
            {{ data.uname }}
          </template>
          <template #filter="{ filterModel, filterCallback }">
            <InputText
              v-model="filterModel.value"
              type="text"
              @input="filterCallback()"
              class="p-column-filter"
              placeholder="按用户名查找"
            /> </template
        ></Column>
        <Column
          field="avatar"
          header="用户头像"
          sortable
          style="min-width: 8rem"
        >
          <template #body="{ data }">
            <el-avatar
              :size="40"
              :src="
                data.avatar
                  ? data.avatar
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
        <Column
          field="email"
          header="用户邮箱"
          sortable
          style="min-width: 8rem"
        >
          <template #body="{ data }">
            {{ data.email }}
          </template>
          <template #filter="{ filterModel, filterCallback }">
            <InputText
              v-model="filterModel.value"
              type="text"
              @input="filterCallback()"
              class="p-column-filter"
              placeholder="按用户邮箱查找"
            />
          </template>
        </Column>
        <Column
          field="statusLabel"
          header="状态"
          style="min-width: 6rem; max-width: 10rem"
          :showFilterMenu="false"
        >
          <template #body="{ data }">
            <el-tag :type="getStatusTag(data.status)[0].type" effect="dark">
              {{ data.statusLabel }}
            </el-tag>
            <span v-if="data.status > 0">
              <br />解封日期<br />{{ data.unBlockTime }}</span
            >
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
        <Column :exportable="false" header="操作" style="min-width: 10rem">
          <template #body="slotProps">
            <Button
              icon="pi pi-pencil"
              outlined
              rounded
              class="mr-2"
              @click="editData(slotProps.data)"
            />
            <Button
              icon="pi pi-send"
              outlined
              rounded
              severity="success"
              @click="sendMessageData(slotProps.data)"
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

    <!-- 编辑用户状态 -->
    <Dialog
      v-model:visible="dialog"
      :style="{ width: '800px' }"
      header="编辑用户状态"
      :modal="true"
      class="p-fluid"
    >
      <div class="field">
        <label for="uid">用户id</label>
        <InputText id="uid" v-model="data.uid" disabled />
      </div>
      <div class="field">
        <label for="uname">用户名</label>
        <InputText id="uname" v-model="data.uname" disabled />
      </div>
      <!-- 封禁 -->
      <div class="field">
        <label for="result">处理</label>
        <br />
        <el-radio-group
          v-model.trim="data.result"
          @change="changeBlock"
          size="large"
        >
          <el-radio-button value="封禁" :disabled="!data.status < 1"
            >封禁</el-radio-button
          >
          <el-radio-button value="解封" :disabled="!data.status > 0"
            >解封</el-radio-button
          >
        </el-radio-group>
        <div class="field" v-if="data.result == '封禁'">
          <label for="blockTime">时长</label>
          <SelectButton
            v-model="data.blockTime"
            :options="timeOptions"
            optionLabel="name"
            aria-labelledby="basic"
          />
        </div>
        <div class="field" v-if="data.result == '封禁'">
          <label for="blockReason">封禁原因</label>
          <InputText id="blockReason" v-model="data.blockReason" />
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

    <!-- 发送系统信息对话框 -->
    <!-- 编辑用户状态 -->
    <Dialog
      v-model:visible="sendDialog"
      :style="{ width: '800px' }"
      header="向用户发送信息"
      :modal="true"
      class="p-fluid"
    >
      <div class="field">
        <label for="uid">用户id</label>
        <InputText id="uid" v-model="data.uid" disabled />
      </div>
      <div class="field">
        <label for="uname">用户名</label>
        <InputText id="uname" v-model="data.uname" disabled />
      </div>
      <!-- 发送消息 -->
      <div>
        <div class="field">
          <label for="messageTitle">消息标题</label>
          <InputText id="messageTitle" v-model="data.messageTitle" />
        </div>
        <div class="field">
          <label for="messageContent">消息内容</label>
          <InputText id="messageContent" v-model="data.messageContent" />
        </div>
      </div>

      <template #footer>
        <Button label="取消" icon="pi pi-times" text @click="hideSendDialog" />
        <Button
          label="保存"
          icon="pi pi-check"
          text
          @click="saveSendData"
        />
      </template>
    </Dialog>

    <!-- 删除单个用户的对话框 -->
    <Dialog
      v-model:visible="deleteDataDialog"
      :style="{ width: '450px' }"
      header="确认删除"
      :modal="true"
    >
      <div class="confirmation-content">
        <i class="pi pi-exclamation-triangle mr-3" style="font-size: 2rem" />
        <span v-if="data"
          >你确定要删除 <b>{{ data.uname }} </b>这个用户?</span
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

    <!-- 删除多个用户的对话框 -->
    <Dialog
      v-model:visible="deleteDatasDialog"
      :style="{ width: '450px' }"
      header="确认删除"
      :modal="true"
    >
      <div class="confirmation-content">
        <i class="pi pi-exclamation-triangle mr-3" style="font-size: 2rem" />
        <span>你确定要删除选择的用户?</span>
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
import {
  getAllUsersServices,
  delAnotherUserService,
  blockUserService,
  unBlockUserService,
  sendUserMessageService,
} from "@/api/user";
import Column from "primevue/column";
import DataTable from "primevue/datatable";
import IconField from "primevue/iconfield";
import InputIcon from "primevue/inputicon";
import Toolbar from "primevue/toolbar";
import FileUpload from "primevue/fileupload";
import Dialog from "primevue/dialog";
import Textarea from "primevue/textarea";
// import { useToast } from "primevue/usetoast";
import { ref, onMounted } from "vue";
import { FilterMatchMode } from "primevue/api";
import { Delete, Edit } from "@element-plus/icons-vue";
import "@/assets/dataTable.css";
import { ElMessage } from "element-plus";

const datas = ref([]);
const data = ref();
const loading = ref(false);
const isSave = ref(false);
const isSendSave = ref(false);
//封禁时长
const timeOptions = ref([
  { name: "10分钟", value: 60 * 10 },
  { name: "30分钟", value: 60 * 30 },
  { name: "1小时", value: 60 * 60 },
  { name: "1天", value: 60 * 60 * 24 },
  { name: "7天", value: 60 * 60 * 24 * 7 },
  { name: "30天", value: 60 * 60 * 24 * 30 },
]);

const statusOptions = [
  { label: "正常", type: "success", status: 0 },
  { label: "封禁中", type: "danger", status: 1 },
];

const statusLabels = ["正常", "封禁中"];

const getStatusByLabel = (label) => {
  return statusOptions.filter((data) => {
    return data.label.startsWith(label);
  });
};

// 返回对应状态的标签
const getStatusTag = (status) => {
  return statusOptions.filter((tag) => {
    return tag.status == status;
  });
};

//筛选
const filters = ref({
  global: { value: null, matchMode: FilterMatchMode.CONTAINS },
  uid: { value: null, matchMode: FilterMatchMode.CONTAINS },
  uname: { value: null, matchMode: FilterMatchMode.CONTAINS },
  avatar: { value: null, matchMode: FilterMatchMode.CONTAINS },
  email: { value: null, matchMode: FilterMatchMode.CONTAINS },
  statusLabel: { value: null, matchMode: FilterMatchMode.EQUALS },
});

//获取用户内容
const getUserDatas = () => {
  getAllUsersServices().then((res) => {
    let newData = res.data;
    newData = newData.map((obj) => {
      return { ...obj, statusLabel: getStatusTag(obj.status)[0].label };
    });
    datas.value = newData;
    loading.value = false;
  });
};

onMounted(() => {
  getUserDatas();
});

const dt = ref();
const dialog = ref(false);
const sendDialog = ref(false);
const deleteDataDialog = ref(false);
const deleteDatasDialog = ref(false);
const selectedDatas = ref();
const submitted = ref(false);

//创建新数据
const openNew = () => {
  data.value = {};
  submitted.value = false;
  dialog.value = true;
};

//隐藏对话框
const hideDialog = () => {
  dialog.value = false;
  submitted.value = false;
};
//隐藏发送信息对话框
const hideSendDialog = () => {
  sendDialog.value = false;
  submitted.value = false;
};

//更改用户状态
const changeBlock = (value) => {
  if (!isSave.value) {
    isSave.value = true;
  }
};

//提交用户状态的更改
const saveData = () => {
  //先校验信息是否完整
  if (data.value.result === "封禁") {
    if (!data.value.blockTime || !data.value.blockReason) {
      ElMessage.error("封禁时间或原因未填写");
      return;
    }
  }

  submitted.value = true;
  if (isSave.value) {
    //封禁
    if (data.value.result === "封禁") {
      let params = {
        uid: data.value.uid,
        time: data.value.blockTime.value,
        reason: data.value.blockReason,
      };
      blockUserService(params).then((res) => {
        if (res.code.endsWith("OK")) {
          ElMessage.success(res.msg);
          getUserDatas();
          isSave.value = false;
        } else {
          ElMessage.error(res.msg);
        }
      });
    } else {
      //解封
      unBlockUserService(data.value.uid).then((res) => {
        if (res.code.endsWith("OK")) {
          ElMessage.success(res.msg);
          getUserDatas();
          isSave.value = false;
        } else {
          ElMessage.error(res.msg);
        }
      });
    }
  }
  dialog.value = false;
  data.value = {};
};

//发送信息
const saveSendData = () => {
  console.log();
  if(!data.value.messageTitle || !data.value.messageContent ){
    ElMessage.error('消息标题或内容未填写')
    return;
  }
  let params = {
    uid: data.value.uid,
    title: data.value.messageTitle,
    content: data.value.messageContent,
    type: "SYSTEM",
  };
  sendUserMessageService(params).then((res) => {
    if (res.code.endsWith("OK")) {
          ElMessage.success(res.msg);
        } else {
          ElMessage.error(res.msg);
        }
  });
  sendDialog.value = false;
};

//编辑数据
const editData = (prod) => {
  data.value = { ...prod };
  dialog.value = true;
};

//编辑数据
const sendMessageData = (prod) => {
  data.value = { ...prod };
  sendDialog.value = true;
};

//确认删除单个用户对话框
const confirmDeleteData = (prod) => {
  data.value = prod;
  deleteDataDialog.value = true;
};

//删除单个用户数据
const deleteData = () => {
  console.log("选中的数据", data.value.uid);
  delAnotherUserService(data.value.uid).then((res) => {
    ElMessage.success(res.msg);
    getUserDatas();
  });
  deleteDataDialog.value = false;
  data.value = {};
};

const refresh = () => {
  loading.value = true;
  setTimeout(() => {
    getUserDatas();
  }, 1000);
};

const exportCSV = () => {
  dt.value.exportCSV();
};
</script>
<style scoped>
:deep(.p-button) {
  padding: 0.2rem 0.2rem;
  height: 1rem;
  margin-left: 0.3rem;
}

.p-button .p-button-label {
  /* margin-left: 0.3rem; */
}
</style>
