
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
        currentPageReportTemplate="显示 {first} 到 {last} 条，一共有 {totalRecords} 条势力信息"
        :globalFilterFields="['pid', 'pname', 'paddr']"
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
            <h4 class="m-0">管理势力信息</h4>
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
        <Column field="pid" header="势力ID" sortable style="min-width: 6rem;width: 10rem">
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
          field="pname"
          header="对应事件名"
          sortable
          style="min-width: 4rem;width: 10rem"
        >
          <template #filter="{ filterModel, filterCallback }">
            <InputText
              v-model="filterModel.value"
              type="text"
              @input="filterCallback()"
              class="p-column-filter"
              placeholder="按名查找"
            /> </template
        ></Column>
        <Column
          field="paddr"
          header="各势力的地点信息"
          sortable
          style="min-width: 24rem"
        >
          <template #body="{ data }">
            <DataTable :value="data.paddr" removableSort>
              <Column
                field="name"
                header="君主名"
                style="width: 4rem"
                sortable
              >
                <template #body="{ data }">
                  {{ data.name.name ? data.name.name : data.name }}
                </template>
              </Column>
              <Column field="addr" header="势力范围" sortable></Column>
            </DataTable>
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
      header="势力信息"
      :modal="true"
      class="p-fluid"
    >
      <div class="field">
        <label for="pname">势力对应的事件名</label>
        <Dropdown
          id="pname"
          v-model="data2.pname"
          :options="events"
          @change="changeEvent"
          filter
          optionLabel="name"
          :placeholder="data2.pname ? data2.pname : '请选择对应的事件'"
        >
        </Dropdown>
        <small class="p-error" v-if="submitted && !data2.pname"
          >事件名需要填写</small
        >
      </div>
      <div class="field">
        <label for="paddr">各势力的地点信息</label>

        <DataTable :value="data2.paddr" removableSort>
          <Column field="name" header="君主名" style="min-width: 4rem" sortable>
            <template #body="{ data }">
              <Dropdown
                id="name"
                v-model="data.name"
                :options="actors"
                @change="changeActor"
                filter
                optionLabel="name"
                :placeholder="data.name ? data.name : '请选择对应的人物'"
              >
              </Dropdown>
            </template>
          </Column>
          <Column
            field="addr"
            header="势力范围"
            style="min-width: 30rem"
            sortable
          >
            <template #body="{ data }">
              <el-cascader
                id="addr"
                ref="addrRef"
                placeholder="请选择地点"
                v-model="data.addr"
                :options="addrs"
                :props="addrProps"
                filterable
                collapse-tags
                collapse-tags-tooltip
                :show-all-levels="false"
                :max-collapse-tags="9"
                @change="addrSelectChange"
                style="width: 100%"
              />
            </template>
          </Column>
          <Column
            field="reduce"
            :exportable="false"
            header="操作"
            style="min-width: 4rem"
          >
            <template #body="slotProps">
              <Button
                icon="pi pi-minus"
                outlined
                rounded
                severity="danger"
                @click="removePower(slotProps)"
              />
            </template>
          </Column>
          <template #footer>
            <Button
              label="添加一条势力信息"
              icon="pi pi-plus"
              text
              @click="addPower"
            />
          </template>
        </DataTable>
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
        <span v-if="data2"
          >你确定要删除 <b>{{ data2.pname }} </b>这个势力信息?</span
        >
      </div>
      <template #footer>
        <Button
          label="不是"
          icon="pi pi-times"
          text
          @click="deleteDataDialog = false"
        />
        <Button label="是的" icon="pi pi-check" text @click="deleteData" />
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
        <span>你确定要删除选择的势力信息?</span>
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
  getAllPowerService,
  updatePowerService,
  delPowersService,
  delPowerService,
  savePowerService,
} from "@/api/power";
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
import { getAllEventsNoPageService } from "@/api/event";
import { getAllActorNoPageService } from "@/api/actor";
import { getAllAddrStateToCascadeService } from "@/api/addr";

const datas = ref([]);
const data2 = ref(null);
const loading = ref(false);
const events = ref([]);
const addrs = ref();
const actors = ref([]);
//筛选
const filters = ref({
  global: { value: null, matchMode: FilterMatchMode.CONTAINS },
  pid: { value: null, matchMode: FilterMatchMode.CONTAINS },
  pname: { value: null, matchMode: FilterMatchMode.CONTAINS },
  paddr: { value: null, matchMode: FilterMatchMode.CONTAINS },
  pactor: { value: null, matchMode: FilterMatchMode.CONTAINS },
});

//获取势力内容
const getPowerDatas = () => {
  getAllPowerService().then((res) => {
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

// 获取地点信息
const getAddrs = () => {
  getAllAddrStateToCascadeService().then((res) => {
    let arr = res.data.map((addr) => ({
      name: addr.addrName,
      code: addr.addrName,
      children: addr.children.map((c) => ({
        name: c.addrName,
        code: c.addrName,
      })),
    }));
    addrs.value = arr;
    return arr;
  });
};

const addrProps = {
  multiple: true,
  value: "code",
  label: "name",
  checkStrictly: true,
};

onMounted(() => {
  getPowerDatas();
  getEvents();
  getActors();
  getAddrs();
});

const dt = ref();
const dialog = ref(false);
const deleteDataDialog = ref(false);
const deleteDatasDialog = ref(false);
const selectedDatas = ref();
const submitted = ref(false);
//地点选择器绑定的ref
const addrRef = ref();

//更改选择的势力对应事件名称
const changeEvent = ()=>{
  data2.value.pname = data2.value.pname.name;
}

//更改选择的人物数据
const changeActor = ()=>{
  data2.value.paddr.map((v)=>{
    v.name = v.name.name ? v.name.name : v.name;
    return v;
  })
}

// 更改选中的地点数据
const addrSelectChange = () => {
  data2.value.paddr.map((v) => {
    v.addr = v.addr.map((v2) => {
      if (typeof v2 == "string") {
        return v2;
      } else {
        return v2[1] ? v2[1] : v2[0];
      }
    });
    return v;
  });
};

//添加一条势力
const addPower = () => {
  let newData = {
    addr: [],
    name: "",
  };
  let paddr = data2.value.paddr;
  if (paddr === null || paddr === undefined) {
    data2.value.paddr = [];
    data2.value.paddr.push(newData);
    return;
  }
  let last = paddr[paddr.length - 1];
  if (last.addr.length < 1 || last.name == "") {
    ElMessage.error("请先填写完上一条数据");
    return;
  }

  data2.value.paddr.push(newData);
};

//减少一条势力
const removePower = (prop) => {
  let i = prop.index;
  data2.value.paddr.splice(i, 1);
};

//创建新数据
const openNew = () => {
  data2.value = {};
  submitted.value = false;
  dialog.value = true;
};

//隐藏对话框
const hideDialog = () => {
  data2.value = {};
  dialog.value = false;
  submitted.value = false;
};
//提交新增数据
const saveData = () => {
  if (data2.value.pname == null || data2.value.pname == undefined) {
    ElMessage.error("请选择势力对应的事件名");
    return;
  } else if (data2.value.paddr == null || data2.value.paddr == undefined) {
    ElMessage.error("请填写各势力的地点信息");
    return;
  }
  let last = data2.value.paddr[data2.value.paddr.length - 1];
  if (last.addr.length < 1 || last.addr == "") {
    ElMessage.error("有数据未填写完");
    return;
  }
  submitted.value = true;
  if (data2.value.pname && data2.value.paddr) {
    //新增数据
    if (!data2.value.pid) {
      savePowerService(data2.value).then((res) => {
        ElMessage.success(res.msg);
        getPowerDatas();
      });
    } else {
      updatePowerService(data2.value).then((res) => {
        ElMessage.success(res.msg);
        getPowerDatas();
      });
    }
    dialog.value = false;
    data2.value = {};
  }
};
//编辑数据
const editData = (prod) => {
  data2.value = { ...prod };
  dialog.value = true;
};
//确认删除单个势力对话框
const confirmDeleteData = (prod) => {
  data2.value = prod;
  deleteDataDialog.value = true;
};

//删除单个势力数据
const deleteData = () => {
  console.log("选中的数据", data2.value.pid);
  delPowerService(data2.value.pid).then((res) => {
    ElMessage.success(res.msg);
    getPowerDatas();
  });
  deleteDataDialog.value = false;
  data2.value = {};
};
//确认删除多个选中的势力对话框
const confirmDeleteSelected = () => {
  deleteDatasDialog.value = true;
};
//删除多个选中的势力
const deleteSelectedDatas = () => {
  //提取对象数组内的属性，组成新数组
  let pids = selectedDatas.value.map(({ pid }) => pid);
  delPowersService(pids).then((res) => {
    ElMessage.success(res.msg);
    getPowerDatas();
  });
  deleteDatasDialog.value = false;
  selectedDatas.value = null;
};

const refresh = () => {
  loading.value = true;
  setTimeout(() => {
    getPowerDatas();
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
