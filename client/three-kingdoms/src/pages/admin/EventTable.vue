
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
        currentPageReportTemplate="显示 {first} 到 {last} 条，一共有 {totalRecords} 条事件信息"
        :globalFilterFields="[
          'eid',
          'ename',
          'econtent',
          'etime',
          'addrs',
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
            <h4 class="m-0">管理历史事件</h4>
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
        <Column field="eid" header="事件ID" sortable style="min-width: 8rem">
          <template #body="{ data }">
            {{ data.eid }}
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
        <Column field="ename" header="事件名" sortable style="min-width: 8rem">
          <template #body="{ data }">
            {{ data.ename }}
          </template>
          <template #filter="{ filterModel, filterCallback }">
            <InputText
              v-model="filterModel.value"
              type="text"
              @input="filterCallback()"
              class="p-column-filter"
              placeholder="按事件名查找"
            /> </template
        ></Column>
        <Column
          field="econtent"
          header="事件内容"
          sortable
          style="min-width: 15rem"
        >
          <template #body="{ data }">
            <Inplace :closable="true">
              <template #closeicon>
                <span class="pi pi-eye-slash"></span>
              </template>
              <template #display>
                <el-text line-clamp="2">
                  {{ data.econtent }}
                </el-text>
              </template>
              <template #content>
                <p class="m-0">
                  {{ data.econtent ? data.econtent : "暂无数据" }}
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
              placeholder="按事件内容查找"
            />
          </template>
        </Column>
        <Column
          field="etime"
          header="事件时间"
          sortable
          style="min-width: 8rem"
        >
          <template #body="{ data }">
            {{ data.etime }}
          </template>
          <template #filter="{ filterModel, filterCallback }">
            <InputText
              v-model="filterModel.value"
              type="text"
              @input="filterCallback()"
              class="p-column-filter"
              placeholder="按事件时间查找"
            />
          </template>
        </Column>
        <Column
          field="addrs"
          header="涉及地点"
          sortable
          style="min-width: 8rem; max-width: 15rem"
        >
          <template #body="{ data }">
            <Inplace :closable="true">
              <template #closeicon>
                <span class="pi pi-eye-slash"></span>
              </template>
              <template #display>
                <el-text line-clamp="2">
                  <span v-for="(item, index) in data.addrList" :key="index">
                    {{ item.addrName }}
                    {{ index < data.addrList.length - 1 ? " , " : "" }}
                  </span>
                  {{ data.addrList.length > 0 ? "" : "暂无数据" }}
                </el-text>
              </template>
              <template #content>
                <p class="m-0">
                  <span v-for="(item, index) in data.addrList" :key="index">
                    {{ item.addrName }}
                    {{ index < data.addrList.length - 1 ? " , " : "" }}
                  </span>
                  {{ data.addrList.length > 0 ? "" : "暂无数据" }}
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
              placeholder="按涉及地点查找"
            />
          </template>
        </Column>
        <Column
          field="aids"
          header="涉及人物"
          sortable
          style="min-width: 8rem; max-width: 15rem"
        >
          <template #body="{ data }">
            <Inplace :closable="true">
              <template #closeicon>
                <span class="pi pi-eye-slash"></span>
              </template>
              <template #display>
                <el-text line-clamp="2">
                  <span v-for="(item, index) in data.actorList" :key="index">
                    {{ item.afname }}
                    {{ index < data.actorList.length - 1 ? " , " : "" }}
                  </span>
                  {{ data.actorList.length > 0 ? "" : "暂无数据" }}
                </el-text>
              </template>
              <template #content>
                <p class="m-0">
                  <span v-for="(item, index) in data.actorList" :key="index">
                    {{ item.afname }}
                    {{ index < data.actorList.length - 1 ? " , " : "" }}
                  </span>
                  {{ data.actorList.length > 0 ? "" : "暂无数据" }}
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
              placeholder="按涉及人物查找"
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
      header="事件信息"
      :modal="true"
      class="p-fluid"
    >
      <div class="field">
        <label for="ename">事件名</label>
        <InputText
          id="ename"
          v-model.trim="data.ename"
          required="true"
          autofocus
          :class="{ 'p-invalid': submitted && !data.ename }"
        />
        <small class="p-error" v-if="submitted && !data.ename"
          >事件名需要填写</small
        >
      </div>
      <div class="field">
        <label for="econtent">事件内容</label>
        <Textarea
          id="econtent"
          v-model.trim="data.econtent"
          required="true"
          rows="8"
          cols="25"
        />
        <small class="p-error" v-if="submitted && !data.econtent"
          >事件内容需要填写</small
        >
      </div>

      <div class="field">
        <label for="etime">事件发生时间</label>
        <InputText id="etime" v-model.trim="data.etime" autofocus />
      </div>
      <div class="field">
        <label for="addrs">涉及地点</label><br />
        <el-cascader
          id="addrId"
          ref="addrRef"
          placeholder="请选择地点"
          v-model="data.addrs"
          :options="addrs"
          :props="addrProps"
          filterable
          collapse-tags
          collapse-tags-tooltip
          :max-collapse-tags="3"
          @change="addrSelectChange"
        />
      </div>
      <div class="field">
        <label for="aids">涉及人物</label>
        <MultiSelect
          v-model="data.aids"
          :options="actors"
          filter
          display="chip"
          optionLabel="name"
          placeholder="请选择人物"
          :maxSelectedLabels="4"
          class="w-full md:w-20rem"
        />
      </div>

      <template #footer>
        <Button label="取消" icon="pi pi-times" text @click="hideDialog" />
        <Button label="保存" icon="pi pi-check" text @click="saveData" />
      </template>
    </Dialog>

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
          >你确定要删除 <b>{{ data.ename }} </b>这个事件?</span
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
        <span>你确定要删除选择的人物?</span>
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
  getAllEventsNoPageService,
  saveEventService,
  delEventService,
  delEventsService,
  updateEventService,
} from "@/api/event";
import { getAllAddrStateToCascadeService } from "@/api/addr";
import Column from "primevue/column";
import DataTable from "primevue/datatable";
import IconField from "primevue/iconfield";
import InputIcon from "primevue/inputicon";
import Toolbar from "primevue/toolbar";
import Dialog from "primevue/dialog";
import Textarea from "primevue/textarea";
import { ref, onMounted } from "vue";
import { FilterMatchMode } from "primevue/api";
import "@/assets/dataTable.css";
import { ElMessage } from "element-plus";
import { getAllActorNoPageService } from "@/api/actor";

const datas = ref([]);
const data = ref();
const loading = ref(false);
const addrs = ref();
const actors = ref();
//筛选
const filters = ref({
  global: { value: null, matchMode: FilterMatchMode.CONTAINS },
  eid: { value: null, matchMode: FilterMatchMode.CONTAINS },
  ename: { value: null, matchMode: FilterMatchMode.CONTAINS },
  econtent: { value: null, matchMode: FilterMatchMode.CONTAINS },
  etime: { value: null, matchMode: FilterMatchMode.CONTAINS },
  addrs: { value: null, matchMode: FilterMatchMode.CONTAINS },
  aids: { value: null, matchMode: FilterMatchMode.CONTAINS },
});

//获取事件内容
const getEventDatas = () => {
  getAllEventsNoPageService().then((res) => {
    datas.value = res.data;
    loading.value = false;
  });
};

// 获取地点信息
const getAddrs = () => {
  getAllAddrStateToCascadeService().then((res) => {
    let arr = res.data.map((addr) => ({
      name: addr.addrName,
      code: addr.addrId,
      children: addr.children.map((c) => ({
        name: c.addrName,
        code: c.addrId,
      })),
    }));
    addrs.value = arr;
    return arr;
  });
};

// 获取人物信息
const getActors = () => {
  getAllActorNoPageService().then((res) => {
    let arr = res.data.map((actor) => ({
      name: actor.afname,
      code: actor.aid,
    }));
    actors.value = arr;
  });
};

onMounted(() => {
  getEventDatas();
  getAddrs();
  getActors();
});

const addrProps = {
  multiple: true,
  value: "code",
  label: "name",
  checkStrictly: true,
};

const dt = ref();
const dialog = ref(false);
const deleteDataDialog = ref(false);
const deleteDatasDialog = ref(false);
const selectedDatas = ref();
const submitted = ref(false);
//地点选择器绑定的ref
const addrRef = ref();

// 选中的地点更改
const addrSelectChange = (value) => {
  // let checkdNodeList = addrRef.value.getCheckedNodes();
  // checkdNodeList = checkdNodeList.filter(
  //   (item) => !(item.parent && item.parent.checked)
  // );
  // console.log(checkdNodeList);
  // // addrRef.value = checkdNodeList
  // data.value.addrs = checkdNodeList.map((item) => item.pathValues);
  // console.log(data.value.addrs);
};

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
  data.value.addrs = data.value.addrs
    .map((item) => (item[1] ? item[1] : item[0]))
    .toString();
  data.value.aids = data.value.aids.map((actor) => actor.code).toString();
  if (data.value.ename.trim() && data.value.econtent.trim()) {
    //新增数据
    if (!data.value.eid) {
      saveEventService(data.value).then((res) => {
        ElMessage.success(res.msg);
        getEventDatas();
      });
    } else {
      updateEventService(data.value).then((res) => {
        ElMessage.success(res.msg);
        getEventDatas();
      });
    }
    dialog.value = false;
    data.value = {};
  }
};
//编辑数据
const editData = (prod) => {
  data.value = { ...prod };
  data.value.addrList = data.value.addrList.map((addr) => ({
    name: addr.addrName,
    code: addr.addrId,
  }));
  data.value.actorList = data.value.actorList.map((actor) => ({
    name: actor.afname,
    code: actor.aid,
  }));
  data.value.addrs = data.value.addrList.map((addr) => addr.code);
  data.value.aids = data.value.actorList;
  dialog.value = true;
};
//确认删除单个事件对话框
const confirmDeleteData = (prod) => {
  data.value = prod;
  deleteDataDialog.value = true;
};

//删除单个事件数据
const deleteData = () => {
  console.log("选中的数据", data.value.eid);
  delEventService(data.value.eid).then((res) => {
    ElMessage.success(res.msg);
    getEventDatas();
  });
  deleteDataDialog.value = false;
  data.value = {};
};
//确认删除多个选中的事件对话框
const confirmDeleteSelected = () => {
  deleteDatasDialog.value = true;
};
//删除多个选中的人物
const deleteSelectedDatas = () => {
  //提取对象数组内的属性，组成新数组
  let eids = selectedDatas.value.map(({ eid }) => eid);
  delEventsService(eids).then((res) => {
    ElMessage.success(res.msg);
    getEventDatas();
  });
  deleteDatasDialog.value = false;
  selectedDatas.value = null;
};

const refresh = () => {
  loading.value = true;
  setTimeout(() => {
    getEventDatas();
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
