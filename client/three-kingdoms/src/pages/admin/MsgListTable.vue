
<template>
  <div>
    <div class="card">
      <Toolbar class="mb-4">
        <template #start>
          <!-- <Button
            label="添加数据"
            icon="pi pi-plus"
            severity="success"
            class="mr-2"
            @click="openNew"
          /> -->
          <Button
            label="批量删除"
            icon="pi pi-trash"
            severity="danger"
            @click="confirmDeleteSelected"
            :disabled="!selectedDatas || !selectedDatas.length"
          />
        </template>

        <template #end>
          <!-- <FileUpload
            mode="basic"
            accept="image/*"
            :maxFileSize="1000000"
            label="导入"
            chooseLabel="Import"
            class="mr-2 inline-block"
          /> -->
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
                <el-text line-clamp="2">
                  {{ data.content }}
                </el-text>
              </template>
              <template #content>
                <p class="m-0">
                  {{ data.content ? data.content : "暂无数据" }}
                </p>
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
          <template #body="{ data }">
            {{ data.time }}
          </template>
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
          field="status"
          header="审核状态"
          sortable
          style="min-width: 6rem"
        >
          <template #body="{ data }">
            {{ data.share }}
          </template>
          <template #filter="{ filterModel, filterCallback }">
            <InputText
              v-model="filterModel.value"
              type="text"
              @input="filterCallback()"
              class="p-column-filter"
              placeholder="按审核状态查找"
            />
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

    <!-- 删除单个人物的对话框 -->
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

    <!-- 删除多个人物的对话框 -->
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
  </div>
</template>

<script setup>
import Column from "primevue/column";
import DataTable from "primevue/datatable";
import IconField from "primevue/iconfield";
import InputIcon from "primevue/inputicon";
import Toolbar from "primevue/toolbar";
import FileUpload from "primevue/fileupload";
import Dialog from "primevue/dialog";
// import { useToast } from "primevue/usetoast";
import { ref, onMounted } from "vue";
import { FilterMatchMode } from "primevue/api";
import "@/assets/dataTable.css";
import { ElMessage } from "element-plus";
import {
  deleteMsgService,
  deleteMsgsService,
  getMsgsNoPageService,
} from "@/api/msg";

const datas = ref([]);
const data = ref();
const loading = ref(false);
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
  status: { value: null, matchMode: FilterMatchMode.CONTAINS },
});

//获取事件内容
const getMsgDatas = () => {
  getMsgsNoPageService().then((res) => {
    datas.value = res.data;
    loading.value = false;
  });
};

onMounted(() => {
  getMsgDatas();
});

// const toast = useToast();
const dt = ref();
const dialog = ref(false);
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

//编辑数据
const editData = (prod) => {
  data.value = { ...prod };
  dialog.value = true;
};
//确认删除单个事件对话框
const confirmDeleteData = (prod) => {
  data.value = prod;
  deleteDataDialog.value = true;
};

//删除单个事件数据
const deleteData = () => {
  console.log("选中的数据", data.value.mid);
  deleteMsgService(data.value.mid).then((res) => {
    ElMessage.success(res.msg);
    getMsgDatas();
  });
  deleteDataDialog.value = false;
  data.value = {};
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

const refresh = () => {
  loading.value = true;
  setTimeout(() => {
    getMsgDatas();
  }, 1000);
  // getActorDatas()
};

const exportCSV = () => {
  dt.value.exportCSV();
};

// const products = ref();
</script>
<style>
.p-button {
  padding: 0.3rem 0.5rem;
  height: 2rem;
  margin-left: 1rem;
}

.p-button .p-button-label {
  margin-left: 0.3rem;
}
</style>
