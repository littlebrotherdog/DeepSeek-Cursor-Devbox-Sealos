<template>
  <div class="login-root">
    <div class="login-box">
      <div class="login-left">
        <div class="system-title">耿飚💗刘曦媛</div>
        <div class="system-subtitle">超级智能系统</div>
      </div>
      <div class="login-right">
        <div class="login-title">用户登录</div>
        <el-form :model="loginForm" :rules="loginRules" ref="loginFormRef" label-width="0" class="login-form">
          <el-form-item prop="username">
            <el-input 
              v-model="loginForm.username" 
              placeholder="请输入用户名"
              prefix-icon="User"
            ></el-input>
          </el-form-item>
          <el-form-item prop="password">
            <el-input 
              v-model="loginForm.password" 
              placeholder="请输入密码" 
              type="password" 
              show-password
              prefix-icon="Lock"
            ></el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="handleLogin" class="login-btn">登 录</el-button>
          </el-form-item>
        </el-form>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { ElMessage } from 'element-plus'
import axios from 'axios'
import { useRouter } from 'vue-router'
import { User, Lock } from '@element-plus/icons-vue'

const BASE_URL = '/api'
const router = useRouter()

const loginForm = reactive({
  username: '',
  password: ''
})
const loginFormRef = ref()

const loginRules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
}

async function handleLogin() {
  try {
    await loginFormRef.value.validate();
    const res = await axios.post(`${BASE_URL}/login`, loginForm);
    if (res.data.code === 1) {
      ElMessage.success('登录成功！');
      localStorage.setItem('token', res.data.data.token);
      localStorage.setItem('user', JSON.stringify(res.data.data));
      router.push('/home');
    } else {
      ElMessage.error(res.data.msg || '用户名或密码错误');
    }
  } catch (error) {
    if (error === false) {
      // 表单校验未通过
    } else if (!error.response) {
      ElMessage.error('网络请求错误，请检查网络连接！');
    } else {
      ElMessage.error(`登录失败: ${error.response.statusText || '服务器错误'}`);
    }
  }
}

// 退出登录功能（可在主布局等处调用）
function logout() {
  localStorage.removeItem('token')
  localStorage.removeItem('user')
  router.push('/login')
  ElMessage.success('已退出登录')
}
</script>

<style scoped>
.login-root {
  position: fixed;     /* 固定定位，随着页面滚动不变 */
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
  background: linear-gradient(135deg, #1e88e5 0%, #1565c0 100%);
  display: flex;
  overflow: hidden;
  align-items: center;
  justify-content: center;
}

.login-box {
  width: 1000px;
  height: 600px;
  background: #fff;
  display: flex;
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.15);
  border-radius: 12px;
  overflow: hidden;
}

.login-left {
  flex: 1;
  background: linear-gradient(135deg, #1976d2 0%, #1565c0 100%);
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 40px;
  color: #fff;
}

.system-title {
  font-size: 48px;
  font-weight: bold;
  margin-bottom: 20px;
  letter-spacing: 2px;
}

.system-subtitle {
  font-size: 24px;
  opacity: 0.9;
}

.login-right {
  flex: 1;
  padding: 60px 40px;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.login-title {
  font-size: 28px;
  color: #333;
  margin-bottom: 40px;
  font-weight: 500;
}

.login-form {
  width: 100%;
  max-width: 320px;
}

.login-form :deep(.el-input__wrapper) {
  box-shadow: 0 0 0 1px #dcdfe6 inset;
  height: 44px;
}

.login-form :deep(.el-input__wrapper:hover) {
  box-shadow: 0 0 0 1px #409eff inset;
}

.login-form :deep(.el-input__inner) {
  height: 44px;
}

.login-btn {
  width: 100%;
  height: 44px;
  font-size: 16px;
  background: linear-gradient(135deg, #1976d2 0%, #1565c0 100%);
  border: none;
  margin-top: 20px;
}

.login-btn:hover {
  background: linear-gradient(135deg, #1565c0 0%, #0d47a1 100%);
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(25, 118, 210, 0.3);
}

.login-form :deep(.el-form-item) {
  margin-bottom: 24px;
}
</style>