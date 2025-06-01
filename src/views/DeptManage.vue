<template>
  <div class="dept-manage">
    <div class="header-row">
      <el-tabs v-model="activeTab">
        <el-tab-pane label="部门管理" name="list"></el-tab-pane>
      </el-tabs>
      <el-button type="primary" @click="openAddDialog" icon="el-icon-plus" class="add-btn">+ 新增部门</el-button>
    </div>
    <el-table :data="deptList" border style="width: 60%; min-width: 600px;">
      <el-table-column prop="id" label="序号" width="80" />
      <el-table-column prop="name" label="部门名称" />
      <el-table-column prop="updateTime" label="最后操作时间" width="200">
        <template #default="scope">
          {{ formatTime(scope.row.updateTime) }}
        </template>
      </el-table-column>
      <el-table-column label="操作" width="120">
        <template #default="scope">
          <el-button type="text" @click="openEditDialog(scope.row)">编辑</el-button>
          <el-button type="text" style="color: orange" @click="openDeleteDialog(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 新增/编辑部门弹窗 -->
    <el-dialog :title="dialogTitle" v-model="dialogVisible" width="400px" :close-on-click-modal="false">
      <el-form :model="form" :rules="rules" ref="formRef" label-width="90px">
        <el-form-item label="部门名称" prop="name">
          <el-input v-model="form.name" maxlength="10" show-word-limit placeholder="请输入部门名称，长度为2-10位" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>

    <!-- 删除确认弹窗 -->
    <el-dialog title="提示" v-model="deleteDialogVisible" width="350px" :close-on-click-modal="false">
      <div>您确定要删除该部门吗？</div>
      <template #footer>
        <el-button @click="deleteDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleDelete">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import axios from 'axios'

// const BASE_URL = 'http://zxyf-management.ns-a7nc3xa9.svc.cluster.local:8080' // 修改前
const BASE_URL = '/api' // 修改后

const activeTab = ref('list')
const deptList = ref([])
const dialogVisible = ref(false)
const deleteDialogVisible = ref(false)
const dialogTitle = ref('新建部门')
const form = reactive({ id: null, name: '' })
const formRef = ref()
const rules = {
  name: [
    { required: true, message: '请输入部门名称', trigger: 'blur' },
    { min: 2, max: 10, message: '长度为2-10位', trigger: 'blur' }
  ]
}
let deleteId = null

function formatTime(time) {
  if (!time) return ''
  return time.replace('T', ' ').slice(0, 19)
}

async function fetchList() {
  try {
    const res = await axios.get(`${BASE_URL}/depts`);
    if (res.data.code === 1) {
      deptList.value = res.data.data;
    } else {
      ElMessage.error(res.data.msg || '获取部门列表失败');
    }
  } catch (error) {
    console.error('获取部门列表错误:', error);
    ElMessage.error('获取部门列表失败，请检查网络或接口');
  }
}

function openAddDialog() {
  dialogTitle.value = '新建部门'
  form.id = null
  form.name = ''
  if (formRef.value) { // 确保formRef已赋值
    formRef.value.resetFields();
  }
  dialogVisible.value = true
}

async function openEditDialog(row) {
   try {
    const res = await axios.get(`${BASE_URL}/depts/${row.id}`);
    if (res.data.code === 1) {
      dialogTitle.value = '编辑部门'
      form.id = res.data.data.id;
      form.name = res.data.data.name;
      dialogVisible.value = true;
    } else {
       ElMessage.error(res.data.msg || '获取部门信息失败');
    }
  } catch (error) {
     console.error('获取部门信息错误:', error);
     ElMessage.error('获取部门信息失败，请检查网络或接口');
  }
}

function openDeleteDialog(row) {
  deleteId = row.id
  deleteDialogVisible.value = true
}

async function handleSubmit() {
  try {
    await formRef.value.validate();
    if (form.id) {
      // 修改
      const res = await axios.put(`${BASE_URL}/depts`, { id: form.id, name: form.name });
      if (res.data.code === 1) {
         ElMessage.success('修改成功');
         dialogVisible.value = false;
         fetchList();
      } else {
         ElMessage.error(res.data.msg || '修改失败');
      }
    } else {
      // 新增
      const res = await axios.post(`${BASE_URL}/depts`, { name: form.name });
       if (res.data.code === 1) {
         ElMessage.success('新增成功');
         dialogVisible.value = false;
         fetchList();
       } else {
         ElMessage.error(res.data.msg || '新增失败');
       }
    }
  } catch (error) {
    console.error('提交表单错误:', error);
     // 只有表单校验失败会进入这里，或其他未知错误
     if (!error.response) { // 非接口响应错误，如网络问题
        ElMessage.error('请求失败，请检查网络或接口');
     }
  }
}

async function handleDelete() {
  try {
    const res = await axios.delete(`${BASE_URL}/depts/${deleteId}`);
    if (res.data.code === 1) {
      ElMessage.success('删除成功');
      deleteDialogVisible.value = false;
      fetchList();
    } else {
      ElMessage.error(res.data.msg || '删除失败');
    }
  } catch (error) {
    console.error('删除部门错误:', error);
    ElMessage.error('删除失败，请检查网络或接口');
  }
}

onMounted(fetchList)
</script>

<style scoped>
.dept-manage {
  padding: 0 0 0 10px;
  background: transparent;
}
.header-row {
  display: flex;
  align-items: center;
  margin-bottom: 16px;
}
.add-btn {
  margin-left: 16px;
}
.el-table {
  margin-bottom: 32px;
}
</style> 