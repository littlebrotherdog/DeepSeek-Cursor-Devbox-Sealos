<template>
  <div class="staff-manage">
    <!-- 搜索条件 -->
    <el-card class="search-card">
      <el-form :inline="true" :model="searchForm">
        <el-form-item label="姓名">
          <el-input v-model="searchForm.name" placeholder="请输入员工姓名"></el-input>
        </el-form-item>
        <el-form-item label="性别">
          <el-select v-model="searchForm.gender" placeholder="请选择">
            <el-option label="全部" value=""></el-option>
            <el-option label="男" :value="1"></el-option>
            <el-option label="女" :value="2"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="入职日期">
          <el-date-picker
            v-model="searchForm.hireDateRange"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            value-format="YYYY-MM-DD"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="fetchList">查询</el-button>
          <el-button @click="resetSearch">清空</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 操作按钮和表格 -->
    <el-card class="table-card">
      <div class="button-row">
        <el-button type="primary" @click="openAddEditDialog">+ 新增员工</el-button>
        <el-button type="danger" @click="handleBatchDelete" :disabled="selectedIds.length === 0">批量删除</el-button>
      </div>
      <el-table :data="staffList" border @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" />
        <el-table-column prop="id" label="序号" width="80" />
        <el-table-column prop="name" label="姓名" width="100" />
        <el-table-column label="性别" width="80">
          <template #default="scope">
            {{ scope.row.gender === 1 ? '男' : '女' }}
          </template>
        </el-table-column>
        <el-table-column label="头像" width="120">
          <template #default="scope">
             <el-image
                style="width: 50px; height: 50px"
                :src="scope.row.image"
                fit="cover"
                alt="头像加载失败"
             >
                <template #error>
                   <div class="image-slot">
                     <el-icon><Picture /></el-icon>
                   </div>
                </template>
             </el-image>
          </template>
        </el-table-column>
         <el-table-column prop="deptName" label="所属部门" width="120" />
         <el-table-column label="职位" width="100">
           <template #default="scope">
             {{ formatPosition(scope.row.position) }}
           </template>
         </el-table-column>
         <el-table-column prop="hireDate" label="入职日期" width="120" />
         <el-table-column prop="updateTime" label="最后操作时间" width="180">
            <template #default="scope">
               {{ formatTime(scope.row.updateTime) }}
            </template>
         </el-table-column>
        <el-table-column label="操作" width="150">
          <template #default="scope">
            <el-button type="text" @click="openAddEditDialog(scope.row)">编辑</el-button>
            <el-button type="text" style="color: orange" @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="pagination.page"
        :page-sizes="[5, 10, 20, 50]"
        :page-size="pagination.pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
        class="pagination"
      />
    </el-card>

    <!-- 新增/编辑员工弹窗 -->
    <el-dialog :title="addEditDialogTitle" v-model="addEditDialogVisible" width="800px" :close-on-click-modal="false">
      <el-form :model="addEditForm" :rules="addEditRules" ref="addEditFormRef" label-width="100px">
        <!-- 第一行：用户名和姓名 -->
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="用户名" prop="username">
              <el-input v-model="addEditForm.username" placeholder="请输入用户名" :disabled="!!addEditForm.id"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="姓名" prop="name">
              <el-input v-model="addEditForm.name" placeholder="请输入姓名"></el-input>
            </el-form-item>
          </el-col>
        </el-row>

        <!-- 第二行：性别和手机号 -->
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="性别" prop="gender">
              <el-select v-model="addEditForm.gender" placeholder="请选择性别" style="width: 100%;">
                <el-option label="男" :value="1"></el-option>
                <el-option label="女" :value="2"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="手机号" prop="phone">
              <el-input v-model="addEditForm.phone" placeholder="请输入手机号"></el-input>
            </el-form-item>
          </el-col>
        </el-row>

        <!-- 第三行：职位和薪资 -->
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="职位" prop="position">
              <el-select v-model="addEditForm.position" placeholder="请选择职位" style="width: 100%;">
                <el-option v-for="pos in positionList" :key="pos.value" :label="pos.label" :value="pos.value"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="薪资" prop="salary">
              <el-input v-model="addEditForm.salary" placeholder="请输入薪资" type="number"></el-input>
            </el-form-item>
          </el-col>
        </el-row>

        <!-- 第四行：所属部门和入职日期 -->
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="所属部门" prop="deptId">
              <el-select v-model="addEditForm.deptId" placeholder="请选择所属部门" style="width: 100%;">
                <el-option v-for="dept in deptListForDropdown" :key="dept.id" :label="dept.name" :value="dept.id"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="入职日期" prop="hireDate">
              <el-date-picker v-model="addEditForm.hireDate" type="date" placeholder="选择日期" value-format="YYYY-MM-DD" style="width: 100%;"></el-date-picker>
            </el-form-item>
          </el-col>
        </el-row>

        <!-- 第五行：头像上传 -->
        <el-form-item label="头像">
          <el-upload
            class="avatar-uploader"
            :action="`${BASE_URL}/upload`"
            :show-file-list="false"
            :on-success="handleAvatarSuccess"
            :before-upload="beforeAvatarUpload"
          >
            <img v-if="addEditForm.image" :src="addEditForm.image" class="avatar" />
            <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
          </el-upload>
        </el-form-item>

        <!-- 第六行：工作经历 -->
        <el-form-item label="工作经历">
          <div class="work-experience-list">
            <div v-for="(expr, index) in addEditForm.exprList" :key="index" class="work-experience-item">
              <el-row :gutter="10">
                <el-col :span="8">
                  <el-form-item
                    :label="'时间'"
                    :prop="'exprList.' + index + '.dateRange'"
                    :rules="{ required: true, message: '请选择时间范围', trigger: 'change' }"
                    label-width="50px"
                  >
                    <el-date-picker
                      v-model="expr.dateRange"
                      type="daterange"
                      range-separator="至"
                      start-placeholder="开始日期"
                      end-placeholder="结束日期"
                      value-format="YYYY-MM-DD"
                      style="width: 100%;"
                    />
                  </el-form-item>
                </el-col>
                <el-col :span="6">
                  <el-form-item
                    :label="'公司'"
                    :prop="'exprList.' + index + '.company'"
                    :rules="{ required: true, message: '请输入公司名称', trigger: 'blur' }"
                    label-width="50px"
                  >
                    <el-input v-model="expr.company" placeholder="公司名称"></el-input>
                  </el-form-item>
                </el-col>
                <el-col :span="6">
                  <el-form-item
                    :label="'职位'"
                    :prop="'exprList.' + index + '.position'"
                    :rules="{ required: true, message: '请输入职位', trigger: 'blur' }"
                    label-width="50px"
                  >
                    <el-input v-model="expr.position" placeholder="职位"></el-input>
                  </el-form-item>
                </el-col>
                <el-col :span="4" style="display: flex; align-items: center;">
                  <el-button type="danger" @click="removeExperience(index)" :icon="Delete" circle></el-button>
                </el-col>
              </el-row>
            </div>
            <el-button type="primary" @click="addExperience" :icon="Plus" plain>新增工作经历</el-button>
          </div>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="addEditDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>

  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import axios from 'axios'
import { Plus, Picture, Delete } from '@element-plus/icons-vue'

const BASE_URL = '/api' // 使用代理地址

const staffList = ref([])
const total = ref(0)
const searchForm = reactive({
  name: '',
  gender: '',
  hireDateRange: [] // [begin, end]
})
const pagination = reactive({
  page: 1,
  pageSize: 10
})
const selectedIds = ref([])

const addEditDialogVisible = ref(false)
const addEditDialogTitle = ref('新增员工')
const addEditForm = reactive({
  id: null,
  username: '',
  name: '',
  phone: '',
  gender: null, // 使用null或空字符串表示未选择
  image: '',
  position: null,
  salary: null,
  hireDate: null,
  deptId: null,
  exprList: [] // 工作经历列表
})
const addEditFormRef = ref() // 表单引用

// 表单验证规则
const addEditRules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  name: [{ required: true, message: '请输入姓名', trigger: 'blur' }],
  phone: [
      { required: true, message: '请输入手机号', trigger: 'blur' },
      { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号', trigger: 'blur' }
  ],
  gender: [{ required: true, message: '请选择性别', trigger: 'change' }],
  position: [{ required: true, message: '请选择职位', trigger: 'change' }],
  salary: [{ required: true, message: '请输入薪资', trigger: 'blur' }],
  hireDate: [{ required: true, message: '请选择入职日期', trigger: 'change' }],
  deptId: [{ required: true, message: '请选择所属部门', trigger: 'change' }],
  // 工作经历的规则已直接写在template中
}


const deptListForDropdown = ref([]) // 部门列表用于下拉框
const positionList = ref([ // 职位列表
    { label: '班主任', value: 1 },
    { label: '讲师', value: 2 },
    { label: '学工主管', value: 3 },
    { label: '教研主管', value: 4 },
    { label: '咨询师', value: 5 },
])

// 格式化性别和职位 (与之前相同)
function formatGender(gender) {
  return gender === 1 ? '男' : '女';
}
function formatPosition(position) {
  const positionMap = {
    1: '班主任', 2: '讲师', 3: '学工主管', 4: '教研主管', 5: '咨询师'
  };
  return positionMap[position] || '';
}
function formatTime(time) {
  if (!time) return ''
  // 根据接口返回的时间格式，这里可能需要调整
   if (time.includes('T')) {
      return time.replace('T', ' ').slice(0, 19);
   }
  return time; // 如果不是ISO 8601格式，直接返回
}


// 获取员工列表 (与之前相同)
async function fetchList() {
  try {
    const params = {
      page: pagination.page,
      pageSize: pagination.pageSize,
      name: searchForm.name,
      gender: searchForm.gender,
      begin: searchForm.hireDateRange ? searchForm.hireDateRange[0] : null,
      end: searchForm.hireDateRange ? searchForm.hireDateRange[1] : null,
    };
    const res = await axios.get(`${BASE_URL}/emps`, { params });
    if (res.data.code === 1) {
      staffList.value = res.data.data.rows;
      total.value = res.data.data.total;
    } else {
      ElMessage.error(res.data.msg || '获取员工列表失败');
    }
  } catch (error) {
    console.error('获取员工列表错误:', error);
    ElMessage.error('获取员工列表失败，请检查网络或接口');
  }
}

// 获取部门列表用于下拉框
async function fetchDeptListForDropdown() {
    try {
        // 部门列表查询接口参数为空
        const res = await axios.get(`${BASE_URL}/depts`);
        if (res.data.code === 1) {
            deptListForDropdown.value = res.data.data;
        } else {
            ElMessage.error(res.data.msg || '获取部门列表失败');
        }
    } catch (error) {
        console.error('获取部门列表错误:', error);
        ElMessage.error('获取部门列表失败，请检查网络或接口');
    }
}


// 清空搜索条件 (与之前相同)
function resetSearch() {
  searchForm.name = '';
  searchForm.gender = '';
  searchForm.hireDateRange = [];
  pagination.page = 1; // 清空搜索后回到第一页
  fetchList();
}

// 处理每页显示数量变化 (与之前相同)
function handleSizeChange(val) {
  pagination.pageSize = val;
  pagination.page = 1; // 改变每页数量后回到第一页
  fetchList();
}

// 处理当前页变化 (与之前相同)
function handleCurrentChange(val) {
  pagination.page = val;
  fetchList();
}

// 处理表格选中变化 (与之前相同)
function handleSelectionChange(selection) {
  selectedIds.value = selection.map(item => item.id);
}

// 单个删除员工 (与之前相同)
async function handleDelete(row) {
  try {
    await ElMessageBox.confirm(`确定要删除员工 ${row.name} 吗？`, '提示', { type: 'warning' });
    const res = await axios.delete(`${BASE_URL}/emps`, { params: { ids: row.id } });
    if (res.data.code === 1) {
      ElMessage.success('删除成功');
      // 如果当前页只有一条数据且不是第一页，则删除后跳转到上一页
      if (staffList.value.length === 1 && pagination.page > 1) {
         pagination.page--;
      }
      fetchList();
    } else {
      ElMessage.error(res.data.msg || '删除失败');
    }
  } catch (error) {
     // 用户取消删除或接口报错
     if (error !== 'cancel') {
       console.error('删除员工错误:', error);
       ElMessage.error('删除失败，请检查网络或接口');
     }
  }
}

// 批量删除员工 (与之前相同)
async function handleBatchDelete() {
   try {
    await ElMessageBox.confirm(`确定要删除选中的 ${selectedIds.value.length} 位员工吗？`, '提示', { type: 'warning' });
    const ids = selectedIds.value.join(',');
    const res = await axios.delete(`${BASE_URL}/emps`, { params: { ids } });
    if (res.data.code === 1) {
      ElMessage.success('批量删除成功');
       // 删除后重新获取列表，并考虑页码调整
       // 如果删除后当前页没有数据且不是第一页，则跳转到上一页
       if (staffList.value.length > 0 && staffList.value.length === selectedIds.value.length && pagination.page > 1) {
          pagination.page--;
       }
      fetchList();
    } else {
      ElMessage.error(res.data.msg || '批量删除失败');
    }
  } catch (error) {
     // 用户取消删除或接口报错
     if (error !== 'cancel') {
       console.error('批量删除员工错误:', error);
       ElMessage.error('批量删除失败，请检查网络或接口');
     }
  }
}

// 打开新增/编辑弹窗
async function openAddEditDialog(row) {
   if (row && row.id) {
      // 编辑
      addEditDialogTitle.value = '编辑员工';
      try {
          const res = await axios.get(`${BASE_URL}/emps/${row.id}`);
          if (res.data.code === 1) {
              // 填充表单数据
              Object.assign(addEditForm, res.data.data);
              // 格式化工作经历的时间范围为数组 [startDate, endDate]
              if (addEditForm.exprList && addEditForm.exprList.length > 0) {
                  addEditForm.exprList = addEditForm.exprList.map(expr => ({
                      ...expr,
                      dateRange: [expr.startDate, expr.endDate]
                  }));
              } else {
                  addEditForm.exprList = []; // 确保是数组
              }

               // 如果有图片URL，也要加载
               if (addEditForm.image) {
                  // 对于编辑，图片字段直接就是URL
               } else {
                   addEditForm.image = ''; // 清空图片字段
               }

              addEditDialogVisible.value = true;
          } else {
              ElMessage.error(res.data.msg || '获取员工信息失败');
          }
      } catch (error) {
          console.error('获取员工信息错误:', error);
          ElMessage.error('获取员工信息失败，请检查网络或接口');
      }
   } else {
      // 新增
      addEditDialogTitle.value = '新增员工';
      // 重置表单
      Object.assign(addEditForm, {
          id: null,
          username: '',
          name: '',
          phone: '',
          gender: null,
          image: '',
          position: null,
          salary: null,
          hireDate: null,
          deptId: null,
          exprList: []
      });
      // 重置表单校验
      if (addEditFormRef.value) {
          addEditFormRef.value.resetFields();
      }
      addEditDialogVisible.value = true;
   }
}

// 提交新增/编辑表单
async function handleSubmit() {
  try {
    await addEditFormRef.value.validate();

    // 处理工作经历数据格式
    const submitForm = { ...addEditForm };
    if (submitForm.exprList && submitForm.exprList.length > 0) {
        submitForm.exprList = submitForm.exprList.map(expr => ({
            ...expr,
            startDate: expr.dateRange ? expr.dateRange[0] : null,
            endDate: expr.dateRange ? expr.dateRange[1] : null,
            dateRange: undefined // 移除dateRange字段
        }));
    }

    if (submitForm.id) {
      // 修改
      const res = await axios.put(`${BASE_URL}/emps`, submitForm);
       if (res.data.code === 1) {
         ElMessage.success('修改成功');
         addEditDialogVisible.value = false;
         fetchList();
       } else {
         ElMessage.error(res.data.msg || '修改失败');
       }
    } else {
      // 新增
       // 新增时，接口文档中没有password字段，但需求中提到默认密码123456
       // 如果后端接口需要，可能需要在请求体中添加 password: '123456'
       // 这里暂时不添加，如果接口报错，可能需要根据实际接口调整
       // delete submitForm.id; // 新增时移除id字段
      const res = await axios.post(`${BASE_URL}/emps`, submitForm);
       if (res.data.code === 1) {
         ElMessage.success('新增成功');
         addEditDialogVisible.value = false;
         fetchList();
       } else {
         ElMessage.error(res.data.msg || '新增失败');
       }
    }
  } catch (error) {
    console.error('提交表单错误:', error);
     // 只有表单校验失败会进入这里，或其他未知错误
     if (error === false) { // Element Plus validate() 拒绝时返回false
        // 表单校验未通过
     } else if (!error.response) { // 非接口响应错误，如网络问题
        ElMessage.error('请求失败，请检查网络或接口');
     } else {
         // 接口返回错误，已在then/catch中处理
     }
  }
}

// 添加工作经历
function addExperience() {
  addEditForm.exprList.push({
    company: '',
    position: '',
    startDate: null,
    endDate: null,
    dateRange: [] // 用于日期选择器绑定
  });
}

// 移除工作经历
function removeExperience(index) {
  addEditForm.exprList.splice(index, 1);
}

// 图片上传成功回调
function handleAvatarSuccess(res, file) {
  // 根据你的图片上传接口返回的数据结构调整
  // 假设接口成功时返回 { code: 1, msg: 'success', data: '图片URL' }
  if (res.code === 1) {
    addEditForm.image = res.data; // 将返回的图片URL赋值给表单字段
    ElMessage.success('图片上传成功！');
  } else {
     ElMessage.error(res.msg || '图片上传失败！');
  }
}

// 图片上传前校验
function beforeAvatarUpload(rawFile) {
  // 校验文件类型和大小
  const allowedTypes = ['image/jpeg', 'image/png', 'image/jpg'];
  const isAllowedType = allowedTypes.includes(rawFile.type);
  const isLt2M = rawFile.size / 1024 / 1024 < 2; // 小于2MB

  if (!isAllowedType) {
    ElMessage.error('图片只能是 JPG, PNG, JPEG 格式!');
    return false;
  }
  if (!isLt2M) {
    ElMessage.error('图片大小不能超过 2MB!');
    return false;
  }
  return true;
}


// 页面加载完成后执行
onMounted(() => {
  fetchList(); // 获取员工列表
  fetchDeptListForDropdown(); // 获取部门列表
});
</script>

<style scoped>
.staff-manage {
  padding: 0 0 0 10px;
  background: transparent;
}
.search-card {
  margin-bottom: 20px;
}
.table-card {
   padding-bottom: 20px; /* 为分页留出空间 */
}
.button-row {
  margin-bottom: 20px;
}
.pagination {
   margin-top: 20px;
   justify-content: flex-end;
}

/* 图片上传样式 */
.avatar-uploader .avatar {
  width: 100px;
  height: 100px;
  display: block;
  object-fit: cover;
}
.avatar-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  transition: var(--el-transition-duration-fast);
}
.avatar-uploader .el-upload:hover {
  border-color: var(--el-color-primary);
}
.el-icon.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 100px;
  height: 100px;
  text-align: center;
}

.work-experience-list {
  margin-bottom: 20px;
}
.work-experience-item {
  background: #f8f9fa;
  padding: 15px;
  margin-bottom: 15px;
  border-radius: 4px;
  border: 1px solid #ebeef5;
}
.work-experience-item .el-form-item {
  margin-bottom: 0;
}
.work-experience-item .el-form-item__label {
  font-size: 13px;
}
.work-experience-item .el-input {
  width: 100%;
}
.work-experience-item .el-date-editor {
  width: 100%;
}

.image-slot {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;
  height: 100%;
  background: var(--el-fill-color-light);
  color: var(--el-text-color-secondary);
  font-size: 14px;
}
.image-slot .el-icon {
  font-size: 30px;
}
</style> 