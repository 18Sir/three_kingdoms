
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
            :disabled="!selectedActors || !selectedActors.length"
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
        :value="actorDatas"
        ref="dt"
        paginator
        :rows="10"
        :rowsPerPageOptions="[5, 10, 20]"
        removableSort
        tableStyle="min-width: 60rem"
        v-model:selection="selectedActors"
        scrollable
        scrollHeight="29rem"
        :loading="loading"
        paginatorTemplate="RowsPerPageDropdown FirstPageLink PrevPageLink CurrentPageReport NextPageLink LastPageLink"
        currentPageReportTemplate="显示 {first} 到 {last} 条，一共有 {totalRecords} 条人物信息"
        :globalFilterFields="[
          'aid',
          'afname',
          'atname',
          'btime',
          'dtime',
          'addrId',
          'events',
          'story',
          'power',
        ]"
      >
        <template #paginatorstart>
          <Button type="button" icon="pi pi-refresh" text label="刷新" @click="refresh" />
        </template>
        <template #header>
          <div class="flex justify-content-between">
            <h4 class="m-0">管理历史人物</h4>
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
        <Column field="aid" header="人物ID" sortable style="min-width: 8rem">
          <template #body="{ data }">
            {{ data.aid }}
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
        <Column field="afname" header="名" sortable style="min-width: 8rem">
          <template #body="{ data }">
            {{ data.afname }}
          </template>
          <template #filter="{ filterModel, filterCallback }">
            <InputText
              v-model="filterModel.value"
              type="text"
              @input="filterCallback()"
              class="p-column-filter"
              placeholder="按名查找"
            /> </template
        ></Column>
        <Column field="atname" header="字" sortable style="min-width: 8rem">
          <template #body="{ data }">
            {{ data.atname }}
          </template>
          <template #filter="{ filterModel, filterCallback }">
            <InputText
              v-model="filterModel.value"
              type="text"
              @input="filterCallback()"
              class="p-column-filter"
              placeholder="按字查找"
            />
          </template>
        </Column>
        <Column
          field="btime"
          header="出生时间"
          sortable
          style="min-width: 8rem"
        >
          <template #body="{ data }">
            {{ data.btime }}
          </template>
          <template #filter="{ filterModel, filterCallback }">
            <InputText
              v-model="filterModel.value"
              type="text"
              @input="filterCallback()"
              class="p-column-filter"
              placeholder="按出生时间查找"
            />
          </template>
        </Column>
        <Column
          field="dtime"
          header="死亡时间"
          sortable
          style="min-width: 8rem"
        >
          <template #body="{ data }">
            {{ data.dtime }}
          </template>
          <template #filter="{ filterModel, filterCallback }">
            <InputText
              v-model="filterModel.value"
              type="text"
              @input="filterCallback()"
              class="p-column-filter"
              placeholder="按死亡时间查找"
            />
          </template>
        </Column>
        <Column
          field="addrId"
          header="出生地点ID"
          sortable
          style="min-width: 8rem"
        >
          <template #body="{ data }">
            {{ data.addrId }}
          </template>
          <template #filter="{ filterModel, filterCallback }">
            <InputText
              v-model="filterModel.value"
              type="text"
              @input="filterCallback()"
              class="p-column-filter"
              placeholder="按出生地点ID查找"
            />
          </template>
        </Column>
        <Column
          field="events"
          header="参与事件ID组"
          sortable
          style="min-width: 10rem"
        >
          <template #body="{ data }">
            <el-text line-clamp="2">
              {{ data.events }}
            </el-text>
          </template>
          <template #filter="{ filterModel, filterCallback }">
            <InputText
              v-model="filterModel.value"
              type="text"
              @input="filterCallback()"
              class="p-column-filter"
              placeholder="按参与事件查找"
            />
          </template>
        </Column>
        <Column
          field="story"
          header="传记内容"
          sortable
          style="min-width: 12rem; max-width: 20rem"
        >
          <template #body="{ data }">
            <el-text line-clamp="2">
              {{ data.story }}
            </el-text>
          </template>
          <template #filter="{ filterModel, filterCallback }">
            <InputText
              v-model="filterModel.value"
              type="text"
              @input="filterCallback()"
              class="p-column-filter"
              placeholder="按传记内容查找"
            />
          </template>
        </Column>
        <Column field="power" header="势力" sortable style="min-width: 8rem">
          <template #body="{ data }">
            {{ data.power }}
          </template>
          <template #filter="{ filterModel, filterCallback }">
            <InputText
              v-model="filterModel.value"
              type="text"
              @input="filterCallback()"
              class="p-column-filter"
              placeholder="按势力查找"
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
              @click="editActor(slotProps.data)"
            />
            <Button
              icon="pi pi-trash"
              outlined
              rounded
              severity="danger"
              @click="confirmDeleteActor(slotProps.data)"
            />
          </template>
        </Column>
      </DataTable>
    </div>

    <!-- 新增数据的对话框 -->
    <Dialog
      v-model:visible="actorDialog"
      :style="{ width: '800px' }"
      header="人物信息"
      :modal="true"
      class="p-fluid"
    >
      <div class="field">
        <label for="afname">名</label>
        <InputText
          id="afname"
          v-model.trim="actorData.afname"
          required="true"
          autofocus
          :class="{ 'p-invalid': submitted && !actorData.afname }"
        />
        <small class="p-error" v-if="submitted && !actorData.afname"
          >人物名需要填写</small
        >
      </div>
      <div class="field">
        <label for="atname">字</label>
        <InputText id="atname" v-model.trim="actorData.atname" autofocus />
      </div>
      <div class="field">
        <label for="btime">出生时间</label>
        <InputText id="btime" v-model.trim="actorData.btime" autofocus />
      </div>
      <div class="field">
        <label for="dtime">死亡时间</label>
        <InputText id="dtime" v-model.trim="actorData.dtime" autofocus />
      </div>
      <div class="field">
        <label for="addrId">出生地Id</label>
        <InputText id="addrId" v-model.trim="actorData.addrId" autofocus />
      </div>
      <div class="field">
        <label for="events">参与事件id数组</label>
        <Textarea id="events" v-model="actorData.events" rows="2" cols="20" />
      </div>
      <div class="field">
        <label for="story">传记内容</label>
        <Textarea id="story" v-model="actorData.story" rows="8" cols="25" />
      </div>
      <div class="field">
        <label for="power">势力</label>
        <Dropdown
          id="power"
          v-model="actorData.power"
          :options="powers"
          :placeholder="powers[0]"
        >
        </Dropdown>
      </div>

      <template #footer>
        <Button label="取消" icon="pi pi-times" text @click="hideDialog" />
        <Button label="保存" icon="pi pi-check" text @click="saveActor" />
      </template>
    </Dialog>

    <!-- 删除单个人物的对话框 -->
    <Dialog
      v-model:visible="deleteActorDialog"
      :style="{ width: '450px' }"
      header="确认删除"
      :modal="true"
    >
      <div class="confirmation-content">
        <i class="pi pi-exclamation-triangle mr-3" style="font-size: 2rem" />
        <span v-if="actorData"
          >你确定要删除 <b>{{ actorData.afname }} 这个人物</b>?</span
        >
      </div>
      <template #footer>
        <Button
          label="No"
          icon="pi pi-times"
          text
          @click="deleteActorDialog = false"
        />
        <Button label="Yes" icon="pi pi-check" text @click="deleteActor" />
      </template>
    </Dialog>

    <!-- 删除多个人物的对话框 -->
    <Dialog
      v-model:visible="deleteActorsDialog"
      :style="{ width: '450px' }"
      header="确认删除"
      :modal="true"
    >
      <div class="confirmation-content">
        <i class="pi pi-exclamation-triangle mr-3" style="font-size: 2rem" />
        <span>你确定要删除选择的人物?</span>
      </div>
      <template #footer>
        <Button
          label="No"
          icon="pi pi-times"
          text
          @click="deleteActorsDialog = false"
        />
        <Button
          label="Yes"
          icon="pi pi-check"
          text
          @click="deleteSelectedActors"
        />
      </template>
    </Dialog>
  </div>
</template>

<script setup>
import {
  getAllActorNoPageService,
  saveActorService,
  delActorByIdService,
  updateActorService,
  delActorMoreService,
} from "@/api/actor";
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

const actorDatas = ref([]);
const actorData = ref();
const loading = ref(false);
//筛选
const filters = ref({
  global: { value: null, matchMode: FilterMatchMode.CONTAINS },
  aid: { value: null, matchMode: FilterMatchMode.STARTS_WITH },
  afname: { value: null, matchMode: FilterMatchMode.STARTS_WITH },
  atname: { value: null, matchMode: FilterMatchMode.STARTS_WITH },
  btime: { value: null, matchMode: FilterMatchMode.CONTAINS },
  dtime: { value: null, matchMode: FilterMatchMode.CONTAINS },
  addrId: { value: null, matchMode: FilterMatchMode.STARTS_WITH },
  events: { value: null, matchMode: FilterMatchMode.CONTAINS },
  story: { value: null, matchMode: FilterMatchMode.CONTAINS },
  power: { value: null, matchMode: FilterMatchMode.EQUALS },
});

//获取人物内容
const getActorDatas = () => {
  getAllActorNoPageService().then((res) => {
    actorDatas.value = res.data;
    loading.value = false;
  });
};

onMounted(() => {
  getActorDatas();
});

// const toast = useToast();
const dt = ref();
const actorDialog = ref(false);
const deleteActorDialog = ref(false);
const deleteActorsDialog = ref(false);
const selectedActors = ref();
const powers = ref(["蜀", "魏", "吴", "群雄"]);
const submitted = ref(false);

//创建新数据
const openNew = () => {
  actorData.value = {};
  submitted.value = false;
  actorDialog.value = true;
};

//隐藏对话框
const hideDialog = () => {
  actorDialog.value = false;
  submitted.value = false;
};
//提交新增数据
const saveActor = () => {
  submitted.value = true;
  if (actorData.value.afname.trim()) {
    //新增数据
    if (!actorData.value.aid) {
      saveActorService(actorData.value).then((res) => {
        ElMessage.success("人物添加成功！");
        getActorDatas();
      });
    } else {
      updateActorService(actorData.value).then((res) => {
        ElMessage.success("人物修改成功！");
        getActorDatas();
      });
    }
    actorDialog.value = false;
    actorData.value = {};
  }
};
//编辑数据
const editActor = (prod) => {
  actorData.value = { ...prod };
  actorDialog.value = true;
};
//确认删除单个人物对话框
const confirmDeleteActor = (prod) => {
  actorData.value = prod;
  deleteActorDialog.value = true;
};

//删除单个人物数据
const deleteActor = () => {
  console.log("选中的数据", actorData.value.aid);
  delActorByIdService(actorData.value.aid).then((res) => {
    ElMessage.success(res.msg);
    getActorDatas();
  });
  deleteActorDialog.value = false;
  actorData.value = {};
};
//确认删除多个选中的人物对话框
const confirmDeleteSelected = () => {
  deleteActorsDialog.value = true;
};
//删除多个选中的人物
const deleteSelectedActors = () => {
  //提取对象数组内的属性，组成新数组
  let aids = selectedActors.value.map(({ aid }) => aid);
  delActorMoreService(aids).then((res) => {
    ElMessage.success(res.msg);
    getActorDatas();
  });
  deleteActorsDialog.value = false;
  selectedActors.value = null;
};

const refresh = ()=>{
  loading.value = true;
  setTimeout(()=>{
    getActorDatas()
  },1000)
  // getActorDatas()
}

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
.field label{
  font-weight: bold;
}
</style>
