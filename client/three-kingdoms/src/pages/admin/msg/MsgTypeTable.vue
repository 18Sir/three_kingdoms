
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
        :globalFilterFields="['tid', 'tname']"
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
            <h4 class="m-0">管理帖子类型</h4>
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
        <Column
          field="tid"
          header="帖子类型ID"
          sortable
          style="min-width: 8rem"
        >
          <template #body="{ data }">
            {{ data.tid }}
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
          field="tname"
          header="帖子类型名"
          sortable
          style="min-width: 8rem"
        >
          <template #body="{ data }">
            {{ data.tname }}
          </template>
          <template #filter="{ filterModel, filterCallback }">
            <InputText
              v-model="filterModel.value"
              type="text"
              @input="filterCallback()"
              class="p-column-filter"
              placeholder="按类型名查找"
            /> </template
        ></Column>
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
      header="帖子类型信息"
      :modal="true"
      class="p-fluid"
    >
      <div class="field">
        <label for="tname">帖子类型名</label>
        <InputText
          id="tname"
          v-model.trim="data.tname"
          required="true"
          autofocus
          :class="{ 'p-invalid': submitted && !data.tname }"
        />
        <small class="p-error" v-if="submitted && !data.tname"
          >类型名需要填写</small
        >
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
          >你确定要删除 <b>{{ data.tname }} </b>这个类型?</span
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
        <span>你确定要删除选择的类型?</span>
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
  getAllMsgTypeService,
  saveMsgTypeService,
  updateMsgTypeService,
  delMsgTypeService,
  delMsgTypesService,
} from "@/api/msgType";
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

const datas = ref([]);
const data = ref();
const loading = ref(false);
//筛选
const filters = ref({
  global: { value: null, matchMode: FilterMatchMode.CONTAINS },
  tid: { value: null, matchMode: FilterMatchMode.CONTAINS },
  tname: { value: null, matchMode: FilterMatchMode.CONTAINS },
});

//获取地点内容
const getMsgTypeDatas = () => {
  getAllMsgTypeService().then((res) => {
    datas.value = res.data;
    loading.value = false;
  });
};

onMounted(() => {
  getMsgTypeDatas();
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
  if (data.value.tname.trim()) {
    //新增数据
    if (!data.value.tid) {
      saveMsgTypeService(data.value).then((res) => {
        ElMessage.success(res.msg);
        getMsgTypeDatas();
      });
    } else {
      updateMsgTypeService(data.value).then((res) => {
        ElMessage.success(res.msg);
        getMsgTypeDatas();
      });
    }
    dialog.value = false;
    data.value = {};
  }
};
//编辑数据
const editData = (prod) => {
  data.value = { ...prod };
  dialog.value = true;
};
//确认删除单个地点对话框
const confirmDeleteData = (prod) => {
  data.value = prod;
  deleteDataDialog.value = true;
};

//删除单个地点数据
const deleteData = () => {
  console.log("选中的数据", data.value.tid);
  delMsgTypeService(data.value.tid).then((res) => {
    ElMessage.success(res.msg);
    getMsgTypeDatas()
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
  let tids = selectedDatas.value.map(({ tid }) => tid);
  delMsgTypesService(tids).then((res) => {
    ElMessage.success(res.msg);
    getMsgTypeDatas();
  });
  deleteDatasDialog.value = false;
  selectedDatas.value = null;
};

const refresh = () => {
  loading.value = true;
  setTimeout(() => {
    getMsgTypeDatas();
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
