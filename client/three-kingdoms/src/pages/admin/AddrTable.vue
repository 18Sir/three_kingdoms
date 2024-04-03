
<template>
  <div>
    <div class="card">
      <Toolbar class="mb-4">
        <template #start>
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
        currentPageReportTemplate="显示 {first} 到 {last} 条，一共有 {totalRecords} 条地点信息"
        :globalFilterFields="[
          'addrId',
          'addrName',
          'state',
          'country',
          'eids',
          'aids',
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
            <h4 class="m-0">管理历史地点</h4>
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
        <Column field="addrId" header="地点ID" sortable style="min-width: 8rem">
          <template #body="{ data }">
            {{ data.addrId }}
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
          field="addrName"
          header="地点名"
          sortable
          style="min-width: 8rem"
        >
          <template #body="{ data }">
            {{ data.addrName }}
          </template>
          <template #filter="{ filterModel, filterCallback }">
            <InputText
              v-model="filterModel.value"
              type="text"
              @input="filterCallback()"
              class="p-column-filter"
              placeholder="按地点名查找"
            /> </template
        ></Column>
        <Column field="state" header="所属州" sortable style="min-width: 8rem">
          <template #body="{ data }">
            <el-text line-clamp="2">
              {{ data.state }}
            </el-text>
          </template>
          <template #filter="{ filterModel, filterCallback }">
            <InputText
              v-model="filterModel.value"
              type="text"
              @input="filterCallback()"
              class="p-column-filter"
              placeholder="按州内容查找"
            />
          </template>
        </Column>
        <Column
          field="country"
          header="所属郡"
          sortable
          style="min-width: 8rem"
        >
          <template #body="{ data }">
            {{ data.country }}
          </template>
          <template #filter="{ filterModel, filterCallback }">
            <InputText
              v-model="filterModel.value"
              type="text"
              @input="filterCallback()"
              class="p-column-filter"
              placeholder="按所属郡查找"
            />
          </template>
        </Column>
        <Column
          field="eids"
          header="涉及事件"
          sortable
          style="min-width: 8rem; max-width: 15rem"
        >
          <template #body="{ data }">
            <el-text line-clamp="2">
              <span v-for="(item, index) in data.eventList" :key="index">
                {{ item.ename }}
                {{ index < data.eventList.length - 1 ? " , " : "" }}
              </span>
              {{ data.eventList.length > 0 ? "" : "暂无数据" }}
            </el-text>
          </template>
          <template #filter="{ filterModel, filterCallback }">
            <InputText
              v-model="filterModel.value"
              type="text"
              @input="filterCallback()"
              class="p-column-filter"
              placeholder="按涉及事件查找"
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

    <!-- 新增数据的对话框 -->
    <Dialog
      v-model:visible="dialog"
      :style="{ width: '800px' }"
      header="地点信息"
      :modal="true"
      class="p-fluid"
    >
      <div class="field">
        <label for="addrName">地点名</label>
        <InputText
          id="addrName"
          v-model.trim="data.addrName"
          required="true"
          autofocus
          :class="{ 'p-invalid': submitted && !data.addrName }"
        />
        <small class="p-error" v-if="submitted && !data.addrName"
          >地点名需要填写</small
        >
      </div>

      <div class="field">
        <label for="state">所属州</label>
        <InputText id="state" v-model.trim="data.state" autofocus />
      </div>
      <div class="field">
        <label for="country">所属郡</label>
        <InputText id="country" v-model.trim="data.country" autofocus />
      </div>
      <div class="field">
        <label for="eids">涉及事件</label>
        <MultiSelect
          id="aids"
          v-model="data.eids"
          :options="events"
          filter
          optionLabel="name"
          display="chip"
          placeholder="请选择事件"
          class="w-full md:w-20rem"
          :maxSelectedLabels="3"
        />
      </div>

      <template #footer>
        <Button label="取消" icon="pi pi-times" text @click="hideDialog" />
        <Button label="保存" icon="pi pi-check" text @click="saveData" />
      </template>
    </Dialog>

    <!-- 删除单个地点的对话框 -->
    <Dialog
      v-model:visible="deleteDataDialog"
      :style="{ width: '450px' }"
      header="确认删除"
      :modal="true"
    >
      <div class="confirmation-content">
        <i class="pi pi-exclamation-triangle mr-3" style="font-size: 2rem" />
        <span v-if="data"
          >你确定要删除 <b>{{ data.addrName }} </b>这个地点?</span
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

    <!-- 删除多个地点的对话框 -->
    <Dialog
      v-model:visible="deleteDatasDialog"
      :style="{ width: '450px' }"
      header="确认删除"
      :modal="true"
    >
      <div class="confirmation-content">
        <i class="pi pi-exclamation-triangle mr-3" style="font-size: 2rem" />
        <span>你确定要删除选择的地点?</span>
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
  delAddrService,
  delAddrsService,
  getAllAddrsNoPageService,
  saveAddrService,
  updateAddrService,
} from "@/api/addr";
import Column from "primevue/column";
import DataTable from "primevue/datatable";
import IconField from "primevue/iconfield";
import InputIcon from "primevue/inputicon";
import Toolbar from "primevue/toolbar";
import FileUpload from "primevue/fileupload";
import Dialog from "primevue/dialog";
import { ref, onMounted } from "vue";
import { FilterMatchMode } from "primevue/api";
import { Delete, Edit } from "@element-plus/icons-vue";
import "@/assets/dataTable.css";
import { ElMessage } from "element-plus";
import { getAllEventsNoPageService } from "@/api/event";

const datas = ref([]);
const data = ref();
const loading = ref(false);
const events = ref([]);
//筛选
const filters = ref({
  global: { value: null, matchMode: FilterMatchMode.CONTAINS },
  addrId: { value: null, matchMode: FilterMatchMode.CONTAINS },
  addrName: { value: null, matchMode: FilterMatchMode.CONTAINS },
  state: { value: null, matchMode: FilterMatchMode.CONTAINS },
  country: { value: null, matchMode: FilterMatchMode.CONTAINS },
  eids: { value: null, matchMode: FilterMatchMode.CONTAINS },
});

//获取地点内容
const getAddrDatas = () => {
  getAllAddrsNoPageService().then((res) => {
    datas.value = res.data;
    loading.value = false;
  });
};

// 获取事件信息
const getEvents = () => {
  getAllEventsNoPageService().then((res) => {
    let arr = res.data.map((event) => ({
      name: event.ename,
      code: event.eid,
    }));
    events.value = arr;
  });
};

onMounted(() => {
  getAddrDatas();
  getEvents();
});

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
//提交新增数据
const saveData = () => {
  submitted.value = true;
  data.value.eids = data.value.eids
    .map((event) => event.code)
    .toString();
  if (data.value.addrName.trim()) {
    //新增数据
    if (!data.value.addrId) {
      saveAddrService(data.value).then((res) => {
        ElMessage.success("地点添加成功！");
        getAddrDatas();
      });
    } else {
      updateAddrService(data.value).then((res) => {
        ElMessage.success("地点修改成功！");
        getAddrDatas();
      });
    }
    dialog.value = false;
    data.value = {};
  }
};
// 转换eventList
const eventListFormat = (eventList) => {
  if (eventList == null) {
    return null;
  } else {
    return eventList.map((event) => ({
      name: event.ename,
      code: event.eid,
    }));
  }
};

//编辑数据
const editData = (prod) => {
  data.value = { ...prod };
  data.value.eventList = eventListFormat(data.value.eventList);
  data.value.eids = data.value.eventList;
  dialog.value = true;
};
//确认删除单个地点对话框
const confirmDeleteData = (prod) => {
  data.value = prod;
  deleteDataDialog.value = true;
};

//删除单个地点数据
const deleteData = () => {
  console.log("选中的数据", data.value.addrId);
  delAddrService(data.value.addrId).then((res) => {
    ElMessage.success(res.msg);
    getAddrDatas();
  });
  deleteDataDialog.value = false;
  data.value = {};
};
//确认删除多个选中的地点对话框
const confirmDeleteSelected = () => {
  deleteDatasDialog.value = true;
};
//删除多个选中的地点
const deleteSelectedDatas = () => {
  //提取对象数组内的属性，组成新数组
  let addrIds = selectedDatas.value.map(({ addrId }) => addrId);
  delAddrsService(addrIds).then((res) => {
    ElMessage.success(res.msg);
    getAddrDatas();
  });
  deleteDatasDialog.value = false;
  selectedDatas.value = null;
};

const refresh = () => {
  loading.value = true;
  setTimeout(() => {
    getAddrDatas();
  }, 1000);
};

const exportCSV = () => {
  dt.value.exportCSV();
};
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
